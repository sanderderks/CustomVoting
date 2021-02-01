package me.sd_master92.customvoting.listeners;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.services.VoteTopService;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class VoteTopListener implements Listener
{
    private final Main plugin;
    private final VoteTopService voteTopService;

    public VoteTopListener(Main plugin)
    {
        this.plugin = plugin;
        voteTopService = new VoteTopService(plugin);
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event)
    {
        if (event.getBlock().getState() instanceof Sign)
        {
            if(event.getPlayer().hasPermission("customvoting.votetop.signs"))
            {
                String line0 = event.getLine(0);
                String line1 = event.getLine(1);
                if (line0 != null && line0.toLowerCase().equalsIgnoreCase("[votes]"))
                {
                    event.setCancelled(true);
                    Location loc = event.getBlock().getLocation();
                    if(line1 != null && line1.toLowerCase().contains("top"))
                    {
                        try
                        {
                            line1 = line1.trim().replace("top", "");
                            int top = Integer.parseInt(line1);
                            voteTopService.updateSign(loc, top);
                        } catch (Exception ignored)
                        {
                            Player player = event.getPlayer();
                            player.sendMessage(plugin.getMessages().getMessage(Messages.EXCEPTION));
                        }
                    } else
                    {
                        voteTopService.updateSign(loc);
                    }
                }
            }
        }
    }
}
