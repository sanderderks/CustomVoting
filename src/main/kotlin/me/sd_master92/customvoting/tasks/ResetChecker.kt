package me.sd_master92.customvoting.tasks

import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.commands.ReloadCommand
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.constants.enumerations.Strings
import org.bukkit.Bukkit
import java.util.*

class ResetChecker(private val plugin: CV)
{
    companion object
    {
        var FIRST_OF_MONTH = false
    }

    init
    {
        TaskTimer.repeat(plugin, 20 * 60 * 60, 20 * 3)
        {
            ReloadCommand.reload(plugin)
            if (Calendar.getInstance()[Calendar.DAY_OF_MONTH] == 1)
            {
                FIRST_OF_MONTH = true
                if (plugin.config.getBoolean(Settings.MONTHLY_RESET.path))
                {
                    for (player in Bukkit.getOnlinePlayers())
                    {
                        if (player.isOp)
                        {
                            player.sendMessage(Strings.RESET_VOTES_MESSAGE_CONFIRM.toString())
                        }
                    }
                }
            }
        }.run()
    }
}