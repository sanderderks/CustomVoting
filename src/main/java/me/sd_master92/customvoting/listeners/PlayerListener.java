package me.sd_master92.customvoting.listeners;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.Settings;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import me.sd_master92.customvoting.database.PlayerRow;
import me.sd_master92.customvoting.gui.VotePartyRewards;
import me.sd_master92.customvoting.subjects.CustomVote;
import me.sd_master92.customvoting.subjects.VoteParty;
import me.sd_master92.customvoting.subjects.VoteTopSign;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerListener implements Listener
{
    public static List<UUID> moneyInput = new ArrayList<>();
    public static List<UUID> commandInput = new ArrayList<>();
    private final Main plugin;

    public PlayerListener(Main plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        if (plugin.useDatabase())
        {
            List<String> queue = new ArrayList<>();
            PlayerRow playerRow = new PlayerRow(plugin, player);
            for (int i = 0; i < playerRow.getQueue(); i++)
            {
                queue.add("unknown.com");
            }
            executeQueue(queue, player);
            playerRow.clearQueue();
        } else
        {
            VoteFile voteFile = new VoteFile(player, plugin);
            executeQueue(voteFile.getQueue(), player);
            voteFile.clearQueue();
        }

        if (player.isOp() && plugin.getConfig().getBoolean(Settings.INGAME_UPDATES) && !plugin.isUpToDate())
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    plugin.sendDownloadUrl(player);
                    player.sendMessage("");
                    player.sendMessage(ChatColor.GRAY + "Updates can be turned off in the /votesettings");
                }
            }.runTaskLater(plugin, 20 * 5);
        }
    }

    private void executeQueue(List<String> queue, Player player)
    {
        if (!queue.isEmpty())
        {
            plugin.print(queue.size() + " queued votes found for " + player.getName() + ". Forwarding in 10 seconds.." +
                    ".");
            Iterator<String> iterator = queue.iterator();
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (iterator.hasNext())
                    {
                        CustomVote.create(plugin, player.getName(), iterator.next());
                    } else
                    {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 200L, 20L);
        }
    }

    @EventHandler
    public void onCommandProcess(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        if (moneyInput.contains(player.getUniqueId()))
        {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Enter a number");
        }
        if (commandInput.contains(player.getUniqueId()))
        {
            event.setCancelled(true);
            String command = event.getMessage();
            checkCommand(command, player);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if (moneyInput.contains(player.getUniqueId()))
        {
            event.setCancelled(true);
            if (event.getMessage().equalsIgnoreCase("cancel"))
            {
                moneyInput.remove(player.getUniqueId());
                SoundType.FAILURE.play(plugin, player);
            } else
            {
                try
                {
                    double input = Double.parseDouble(event.getMessage());
                    if (input < 0 || input > 1000000)
                    {
                        player.sendMessage(ChatColor.RED + "Enter a number between 0 and 1.000.000");
                    } else
                    {
                        plugin.getConfig().set(Settings.VOTE_REWARD_MONEY, input);
                        plugin.getConfig().saveConfig();
                        moneyInput.remove(player.getUniqueId());
                        SoundType.SUCCESS.play(plugin, player);
                        player.sendMessage(ChatColor.GREEN + "Successfully updated the money reward!");
                    }
                } catch (Exception e)
                {
                    player.sendMessage(ChatColor.RED + "Enter a number");
                }
            }
        }
        if (commandInput.contains(player.getUniqueId()))
        {
            event.setCancelled(true);
            if (event.getMessage().equalsIgnoreCase("cancel"))
            {
                commandInput.remove(player.getUniqueId());
                SoundType.FAILURE.play(plugin, player);
            } else
            {
                checkCommand(event.getMessage(), player);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (block.getType() == Material.ENDER_CHEST)
        {
            Map<String, Location> chests = plugin.getData().getLocations(Data.VOTE_PARTY);
            for (String key : chests.keySet())
            {
                if (chests.get(key).equals(event.getBlock().getLocation()))
                {
                    if (player.hasPermission("çustomvoting.voteparty"))
                    {
                        if (plugin.getData().deleteLocation(Data.VOTE_PARTY + "." + key))
                        {
                            plugin.getData().deleteItems(Data.VOTE_PARTY + "." + key);
                            event.setDropItems(false);
                            event.getPlayer().sendMessage(ChatColor.RED + "Vote Party Chest #" + key + " deleted.");
                        }
                    } else
                    {
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "You do not have permission to break this block.");
                    }
                }
            }
        } else if (block.getState() instanceof Sign)
        {
            VoteTopSign voteTop = VoteTopSign.get(block.getLocation());
            if (voteTop != null)
            {
                voteTop.delete(player);
            }
        } else
        {
            List<Location> locations = new ArrayList<>();
            for (BlockFace face : new BlockFace[]{BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH,
                    BlockFace.WEST})
            {
                if (block.getRelative(face).getState() instanceof Sign)
                {
                    locations.add(block.getRelative(face).getLocation());
                }
            }
            if (!locations.isEmpty())
            {
                for (Location loc : locations)
                {
                    VoteTopSign voteTop = VoteTopSign.get(loc);
                    if (voteTop != null)
                    {
                        voteTop.delete(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (event.getItemInHand().equals(VoteParty.VOTE_PARTY_ITEM))
        {
            Player player = event.getPlayer();
            if (player.hasPermission("customvoting.voteparty"))
            {
                Set<String> chests = plugin.getData().getLocations(Data.VOTE_PARTY).keySet();
                int i = 1;
                while (chests.contains("" + i))
                {
                    i++;
                }
                plugin.getData().setLocation(Data.VOTE_PARTY + "." + i, event.getBlock().getLocation());
                SoundType.SUCCESS.play(plugin, player);
                player.sendMessage(ChatColor.GREEN + "Vote Party Chest #" + i + " registered.");
                player.getInventory().setItemInMainHand(VoteParty.VOTE_PARTY_ITEM);
            } else
            {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You do not have permission to place this block.");
            }
        }
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENDER_CHEST && !player.isSneaking())
        {
            Location loc = event.getClickedBlock().getLocation();
            Map<String, Location> chests = plugin.getData().getLocations(Data.VOTE_PARTY);
            for (String key : chests.keySet())
            {
                if (chests.get(key).equals(loc))
                {
                    event.setCancelled(true);
                    if (player.hasPermission("customvoting.voteparty"))
                    {
                        SoundType.OPEN.play(plugin, player);
                        player.openInventory(new VotePartyRewards(plugin, key).getInventory());
                    } else
                    {
                        player.sendMessage(ChatColor.RED + "You do not have permission to open this chest.");
                    }
                }
            }
        }
    }

    private void checkCommand(String command, Player player)
    {
        for (String forbidden : plugin.getConfig().getStringList(Settings.FORBIDDEN_COMMANDS))
        {
            if (command.toLowerCase().contains(forbidden))
            {
                player.sendMessage(ChatColor.RED + "This command is forbidden.");
                return;
            }
        }
        if (command.startsWith("/"))
        {
            command = command.substring(1);
        }
        List<String> commands = plugin.getData().getStringList(Data.VOTE_COMMANDS);
        if (commands.contains(command))
        {
            commands.remove(command);
            player.sendMessage(ChatColor.RED + "Removed /" + command + " from commands");
        } else
        {
            commands.add(command);
            player.sendMessage(ChatColor.GREEN + "Added /" + command + " to commands");
        }
        plugin.getData().set(Data.VOTE_COMMANDS, commands);
        plugin.getData().saveConfig();
        commandInput.remove(player.getUniqueId());
    }
}
