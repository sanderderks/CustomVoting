package me.sd_master92.customvoting.listeners;

import com.vexsoftware.votifier.model.VotifierEvent;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.subjects.CustomVote;
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
        new CustomVote(plugin, event.getVote());
    }
}
