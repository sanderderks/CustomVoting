package me.sd_master92.customvoting.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.sd_master92.customvoting.API;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class VotifierListener implements Listener
{
    private final Main plugin;

    public VotifierListener(Main plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVotifierEvent(VotifierEvent event)
    {
        Vote vote = event.getVote();
        Player player = Bukkit.getPlayer(vote.getUsername());
        if(player != null)
        {
            new VoteFile(player.getUniqueId().toString(), plugin).addVote();
            API.broadcastVote(vote, plugin);
        } else
        {
            // offline vote
        }
    }
}
