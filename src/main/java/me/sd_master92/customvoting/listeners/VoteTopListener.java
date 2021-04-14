package me.sd_master92.customvoting.listeners;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.subjects.VoteTopSign;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
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
            if (event.getPlayer().hasPermission("customvoting.votetop.signs"))
            {
                String line0 = event.getLine(0);
                String line1 = event.getLine(1);
                if (line0 != null && line0.equalsIgnoreCase("[votes]"))
                {
                    event.setCancelled(true);
                    Location loc = event.getBlock().getLocation();
                    Player player = event.getPlayer();
                    if (line1 != null && line1.toLowerCase().contains("top"))
                    {
                        try
                        {
                            line1 = line1.trim().replace("top", "");
                            int top = Integer.parseInt(line1);
                            new VoteTopSign(plugin, top, loc, player);
                        } catch (Exception ignored)
                        {

                        }
                    } else
                    {
                        new VoteTopSign(plugin, 0, loc, player);
                    }
                }
            }
        }
    }
}
