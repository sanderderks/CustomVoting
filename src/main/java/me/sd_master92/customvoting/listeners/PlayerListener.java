package me.sd_master92.customvoting.listeners;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import me.sd_master92.customvoting.services.GUIService;
import me.sd_master92.customvoting.services.VotePartyService;
import me.sd_master92.customvoting.services.VoteService;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlayerListener implements Listener
{
    private final Main plugin;
    private final VoteService voteService;
    private final GUIService guiService;

    public PlayerListener(Main plugin)
    {
        this.plugin = plugin;
        voteService = new VoteService(plugin);
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
                        voteService.fakeVote(player.getName(), iterator.next());
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
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (event.getBlock().getType() == Material.ENDER_CHEST)
        {
            Map<String, Location> chests = plugin.getData().getLocations("voteparty");
            for (String key : chests.keySet())
            {
                if (chests.get(key).equals(event.getBlock().getLocation()))
                {
                    Player player = event.getPlayer();
                    if (player.hasPermission("çustomvoting.voteparty"))
                    {
                        if (plugin.getData().deleteLocation("voteparty." + key))
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
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (event.getItemInHand().equals(VotePartyService.VOTE_PARTY_ITEM))
        {
            Player player = event.getPlayer();
            if (player.hasPermission("customvoting.voteparty"))
            {
                Set<String> chests = plugin.getData().getLocations("voteparty").keySet();
                int i = 1;
                while (chests.contains("" + i))
                {
                    i++;
                }
                plugin.getData().setLocation("voteparty." + i, event.getBlock().getLocation());
                SoundType.SUCCESS.play(plugin, player.getLocation());
                player.sendMessage(ChatColor.GREEN + "Vote Party Chest #" + i + " registered.");
                player.getInventory().setItemInMainHand(VotePartyService.VOTE_PARTY_ITEM);
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
            Map<String, Location> chests = plugin.getData().getLocations("voteparty");
            for (String key : chests.keySet())
            {
                if (chests.get(key).equals(loc))
                {
                    event.setCancelled(true);
                    if (player.hasPermission("customvoting.voteparty"))
                    {
                        SoundType.OPEN.play(plugin, player.getLocation());
                        player.openInventory(guiService.getVotePartyRewards(key));
                    } else
                    {
                        player.sendMessage(ChatColor.RED + "You do not have permission to open this chest.");
                    }
                }
            }
        }
    }
}
