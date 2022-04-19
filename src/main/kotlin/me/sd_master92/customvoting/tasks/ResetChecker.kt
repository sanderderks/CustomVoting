package me.sd_master92.customvoting.tasks

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.commands.ReloadCommand
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ResetChecker(private val plugin: CV)
{
    companion object
    {
        var FIRST_OF_MONTH = false
    }

    init
    {
        object : BukkitRunnable()
        {
            override fun run()
            {
                ReloadCommand.reload(plugin)
                if (Calendar.getInstance()[Calendar.DAY_OF_MONTH] == 1)
                {
                    FIRST_OF_MONTH = true
                    if (plugin.config.getBoolean(Settings.MONTHLY_RESET))
                    {
                        for (player in Bukkit.getOnlinePlayers())
                        {
                            if (player.isOp)
                            {
                                player.sendMessage(ChatColor.GREEN.toString() + "A new month has started and votes can be reset in the /votesettings.")
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 60, 20 * 60 * 60)
    }
}