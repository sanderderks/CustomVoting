package me.sd_master92.customvoting.listeners;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.Settings;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import me.sd_master92.customvoting.services.GUIService;
import me.sd_master92.customvoting.subjects.CustomVote;
import me.sd_master92.customvoting.subjects.VoteParty;
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
    public static List<UUID> chatInput = new ArrayList<>();
    private final Main plugin;
    private final GUIService guiService;

    public PlayerListener(Main plugin)
    {
        this.plugin = plugin;
        guiService = new GUIService(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        VoteFile voteFile = new VoteFile(player, plugin);
        List<String> queue = voteFile.getQueue();
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
            voteFile.clearQueue();
        }
    }

    @EventHandler
    public void onCommandProcess(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        if(chatInput.contains(player.getUniqueId()))
        {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Enter a number");
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if(chatInput.contains(player.getUniqueId()))
        {
            event.setCancelled(true);
            if(event.getMessage().equalsIgnoreCase("cancel"))
            {
                chatInput.remove(player.getUniqueId());
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
                        plugin.getSettings().set(Settings.VOTE_REWARD_MONEY, input);
                        plugin.getSettings().saveConfig();
                        chatInput.remove(player.getUniqueId());
                        SoundType.SUCCESS.play(plugin, player);
                        player.sendMessage(ChatColor.GREEN + "Successfully updated the money reward!");
                    }
                } catch (Exception e)
                {
                    player.sendMessage(ChatColor.RED + "Enter a number");
                }
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
                            plugin.getData().deleteItems("voteparty." + key);
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
            checkAndDeleteVoteSign(player, block.getLocation());
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
                    checkAndDeleteVoteSign(player, loc);
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
                        player.openInventory(guiService.getVotePartyRewards(key));
                    } else
                    {
                        player.sendMessage(ChatColor.RED + "You do not have permission to open this chest.");
                    }
                }
            }
        }
    }

    private void checkAndDeleteVoteSign(Player player, Location loc)
    {
        Map<String, Location> locations = plugin.getData().getLocations(Data.VOTE_TOP_SIGNS);
        for (String key : locations.keySet())
        {
            if (loc.equals(locations.get(key)))
            {
                if (plugin.getData().deleteLocation(Data.VOTE_TOP_SIGNS + "." + key))
                {
                    player.sendMessage(ChatColor.RED + "Unregistered Vote Sign #" + key);
                }
            }
        }
    }
}
