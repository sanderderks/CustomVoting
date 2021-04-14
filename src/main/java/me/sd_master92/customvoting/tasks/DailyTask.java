package me.sd_master92.customvoting.tasks;

import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.constants.Settings;
import me.sd_master92.customvoting.subjects.VoteTopSign;
import me.sd_master92.customvoting.subjects.VoteTopStand;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;

public class DailyTask
{
    private final Main plugin;

    public DailyTask(Main plugin)
    {
        this.plugin = plugin;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(plugin.getSettings().getBoolean(Settings.MONTHLY_RESET))
                {
                    reloadVoteTop();
                    checkMonthlyReset();
                }
            }
        }.runTaskTimer(plugin, 60, 20 * 60 * 60);
    }

    private void reloadVoteTop()
    {
        VoteTopSign.updateAll(plugin);
        VoteTopStand.updateAll(plugin);
    }

    private void checkMonthlyReset()
    {
        if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == 1)
        {
            for(PlayerFile playerFile : PlayerFile.getAll(plugin))
            {
                VoteFile voteFile = new VoteFile(playerFile.getUuid(), plugin);
                voteFile.setVotes(0, true);
            }
            plugin.getServer().broadcastMessage(plugin.getMessages().getMessage(Messages.MONTHLY_RESET));
        }
    }
}
