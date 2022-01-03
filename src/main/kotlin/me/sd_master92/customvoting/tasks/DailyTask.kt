package me.sd_master92.customvoting.tasks

import me.sd_master92.customfile.PlayerFile
import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.commands.ReloadCommand
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.database.PlayerRow
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class DailyTask(private val plugin: Main)
{
    private fun checkMonthlyReset()
    {
        if (Calendar.getInstance()[Calendar.DAY_OF_MONTH] == 1)
        {
            for (playerFile in PlayerFile.getAll(plugin))
            {
                if (plugin.hasDatabaseConnection())
                {
                    val playerRow = PlayerRow(plugin, playerFile.uuid)
                    playerRow.setVotes(0, true)
                } else
                {
                    val voteFile = VoteFile(playerFile.uuid, plugin)
                    voteFile.setVotes(0, true)
                }
            }
            plugin.server.broadcastMessage(Messages.MONTHLY_RESET.getMessage(plugin))
        }
    }

    private fun checkMonthlyPeriod()
    {
        if (Calendar.getInstance()[Calendar.DAY_OF_MONTH] == 1)
        {
            for (playerFile in PlayerFile.getAll(plugin))
            {
                if (plugin.hasDatabaseConnection())
                {
                    val playerRow = PlayerRow(plugin, playerFile.uuid)
                    playerRow.clearPeriod()
                } else
                {
                    val voteFile = VoteFile(playerFile.uuid, plugin)
                    voteFile.clearPeriod()
                }
            }
            plugin.server.broadcastMessage(Messages.MONTHLY_RESET.getMessage(plugin))
        }
    }

    init
    {
        object : BukkitRunnable()
        {
            override fun run()
            {
                ReloadCommand.reload(plugin)
                if (plugin.config.getBoolean(Settings.MONTHLY_RESET))
                {
                    checkMonthlyReset()
                }
                if (plugin.config.getBoolean(Settings.MONTHLY_PERIOD))
                {
                    checkMonthlyPeriod()
                }
            }
        }.runTaskTimer(plugin, 60, (20 * 60 * 60).toLong())
    }
}