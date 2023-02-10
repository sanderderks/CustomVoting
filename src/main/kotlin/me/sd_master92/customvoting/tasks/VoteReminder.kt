package me.sd_master92.customvoting.tasks

import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.sendTexts
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class VoteReminder(private val plugin: CV)
{
    companion object
    {
        fun remindPlayer(plugin: CV, player: Player)
        {
            if (!plugin.config.getBoolean(Setting.DISABLED_MESSAGE_VOTE_REMINDER.path))
            {
                TaskTimer.delay(plugin, 20 * 10)
                {
                    val voter = Voter.get(plugin, player)

                    val now = Calendar.getInstance()
                    val next = Calendar.getInstance()
                    next.timeInMillis = voter.last
                    next.add(Calendar.DAY_OF_MONTH, 1)

                    if (voter.votes == 0 || now >= next)
                    {
                        player.sendTexts(plugin, Message.VOTE_REMINDER)
                        SoundType.NOTIFY.play(plugin, player)
                    }
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
                remindPlayer(plugin, player)
            }
        }.run()
    }
}