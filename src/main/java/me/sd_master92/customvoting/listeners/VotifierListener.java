package me.sd_master92.customvoting.listeners;

import com.vexsoftware.votifier.model.VotifierEvent;
import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class VotifierListener implements Listener
{
    Main plugin;
    PlayerFile players;

    public VotifierListener(Main plugin, PlayerFile players)
    {
        this.plugin = plugin;
        this.players = players;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVotifierEvent(VotifierEvent event)
    {
        System.out.println("Received vote: " + event.getVote().getUsername());
    }
}
