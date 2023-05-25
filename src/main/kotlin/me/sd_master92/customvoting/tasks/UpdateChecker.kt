package me.sd_master92.customvoting.tasks

import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class UpdateChecker(plugin: CV)
{
    companion object
    {
        fun checkUpdates(plugin: CV, player: Player)
        {
            if (player.isOp && plugin.config.getBoolean(Setting.INGAME_UPDATES.path) && !plugin.versionStatus()
                    .isValid()
            )
            {
                TaskTimer.delay(plugin, 20 * 5)
                {
                    plugin.sendDownloadUrl(player)
                    player.sendMessage("")
                    player.sendMessage(PMessage.PLUGIN_UPDATE_MESSAGE_DISABLE.toString())
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