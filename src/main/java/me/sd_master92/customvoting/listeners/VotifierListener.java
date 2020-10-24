package me.sd_master92.customvoting.listeners;

import com.vexsoftware.votifier.model.VotifierEvent;
import me.sd_master92.customvoting.API;
import me.sd_master92.customvoting.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class VotifierListener implements Listener
{
    Main plugin;

    public VotifierListener(Main plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVotifierEvent(VotifierEvent event)
    {
        API.broadcastVote(event.getVote(), plugin);
    }
}
