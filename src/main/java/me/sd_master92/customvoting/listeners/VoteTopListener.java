package me.sd_master92.customvoting.listeners;

import me.sd_master92.customvoting.Main;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class VoteTopListener implements Listener
{
    private final Main plugin;

    public VoteTopListener(Main plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event)
    {
        if (event.getBlock().getState() instanceof Sign)
        {
            String line0 = event.getLine(0);
            String line1 = event.getLine(1);
            if (line0 != null && line1 != null && line0.toLowerCase().equalsIgnoreCase("[votes]") && line1.toLowerCase().contains("top"))
            {
                event.setCancelled(true);
                try
                {
                    line1 = line1.trim().replace("top", "");
                    int top = Integer.parseInt(line1);
                    plugin.getVoteTopService().updateSign(event.getBlock().getLocation(), top);
                } catch (Exception ignored)
                {
                }
            }
        }
    }
}
