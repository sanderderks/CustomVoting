package me.sd_master92.customvoting.tasks

import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Settings
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class UpdateChecker(plugin: CV)
{
    companion object
    {
        fun checkUpdates(plugin: CV, player: Player)
        {
            if (player.isOp && plugin.config.getBoolean(Settings.INGAME_UPDATES.path) && !plugin.isUpToDate())
            {
                TaskTimer.delay(plugin, 20 * 5)
                {
                    plugin.sendDownloadUrl(player)
                    player.sendMessage("")
                    player.sendMessage(ChatColor.GRAY.toString() + "Updates can be turned off in the /votesettings")
                }.run()
            }
        }
    }

    init
    {
        TaskTimer.repeat(plugin, 20 * 60 * 60, 20 * 3)
        {
            for (player in Bukkit.getOnlinePlayers())
            {
                checkUpdates(plugin, player)
            }
        }.run()
    }
}