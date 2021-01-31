package me.sd_master92.customvoting.listeners;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.services.VoteService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;

public class PlayerListener implements Listener
{
    private final Main plugin;
    private final VoteService voteService;

    public PlayerListener(Main plugin)
    {
        this.plugin = plugin;
        voteService = new VoteService(plugin);
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
}
