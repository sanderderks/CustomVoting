package me.sd_master92.customvoting.tasks

import me.sd_master92.customfile.PlayerFile
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.commands.ReloadCommand
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.database.PlayerRow
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class DailyTask(private val plugin: CV)
{
    private fun checkMonthlyReset()
    {
        if (plugin.config.getBoolean(Settings.MONTHLY_RESET))
        {
            if (Calendar.getInstance()[Calendar.DAY_OF_MONTH] == 1)
            {
                for (playerFile in PlayerFile.getAll())
                {
                    if (plugin.hasDatabaseConnection())
                    {
                        PlayerRow(plugin, playerFile.value.uuid).setVotes(0, true)
                    } else
                    {
                        VoteFile(playerFile.value.uuid, plugin).setVotes(0, true)
                    }
                }
                plugin.server.broadcastMessage(Messages.MONTHLY_RESET.getMessage(plugin))
            }
        }
    }

    private fun checkMonthlyPeriod()
    {
        if (plugin.config.getBoolean(Settings.MONTHLY_PERIOD))
        {
            if (Calendar.getInstance()[Calendar.DAY_OF_MONTH] == 1)
            {
                for (playerFile in PlayerFile.getAll())
                {
                    if (plugin.hasDatabaseConnection())
                    {
                        PlayerRow(plugin, playerFile.value.uuid).clearPeriod()
                    } else
                    {
                        VoteFile(playerFile.value.uuid, plugin).clearPeriod()
                    }
                }
                plugin.server.broadcastMessage(Messages.MONTHLY_RESET.getMessage(plugin))
            }
        }
    }

    init
    {
        object : BukkitRunnable()
        {
            override fun run()
            {
                ReloadCommand.reload(plugin)
                checkMonthlyReset()
                checkMonthlyPeriod()
            }
        }.runTaskTimer(plugin, 60, (20 * 60 * 60).toLong())
    }
}