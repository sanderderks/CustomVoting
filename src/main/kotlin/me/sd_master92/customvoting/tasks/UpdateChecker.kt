package me.sd_master92.customvoting.tasks

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Settings
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class UpdateChecker(plugin: CV)
{
    companion object
    {
        fun checkUpdates(plugin: CV, player: Player)
        {
            if (player.isOp && plugin.config.getBoolean(Settings.INGAME_UPDATES.path) && !plugin.isUpToDate())
            {
                object : BukkitRunnable()
                {
                    override fun run()
                    {
                        plugin.sendDownloadUrl(player)
                        player.sendMessage("")
                        player.sendMessage(ChatColor.GRAY.toString() + "Updates can be turned off in the /votesettings")
                    }
                }.runTaskLater(plugin, 20 * 5)
            }
        }
    }

    init
    {
        object : BukkitRunnable()
        {
            override fun run()
            {
                for (player in Bukkit.getOnlinePlayers())
                {
                    checkUpdates(plugin, player)
                }
            }
        }.runTaskTimer(plugin, 60, 20 * 60 * 60)
    }
}