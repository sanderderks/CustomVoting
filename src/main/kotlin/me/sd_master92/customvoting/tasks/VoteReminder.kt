package me.sd_master92.customvoting.tasks

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.database.PlayerTable
import me.sd_master92.customvoting.sendTexts
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class VoteReminder(private val plugin: CV)
{
    companion object
    {
        fun remindPlayer(plugin: CV, player: Player)
        {
            if (!plugin.config.getBoolean(Settings.DISABLED_MESSAGE_VOTE_REMINDER.path))
            {
                object : BukkitRunnable()
                {
                    override fun run()
                    {
                        val voter = if (plugin.hasDatabaseConnection())
                        {
                            PlayerTable(plugin, player.uniqueId.toString())
                        } else
                        {
                            VoteFile(player.uniqueId.toString(), plugin)
                        }

                        val now = Calendar.getInstance()
                        val next = Calendar.getInstance()
                        next.timeInMillis = voter.last
                        next.add(Calendar.DAY_OF_MONTH, 1)

                        if (voter.votes == 0 || now >= next)
                        {
                            player.sendTexts(plugin, Messages.VOTE_REMINDER)
                            SoundType.NOTIFY.play(plugin, player)
                        }
                    }
                }.runTaskLater(plugin, 20 * 10)
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
                    remindPlayer(plugin, player)
                }
            }
        }.runTaskTimer(plugin, 60, 20 * 60 * 60)
    }
}