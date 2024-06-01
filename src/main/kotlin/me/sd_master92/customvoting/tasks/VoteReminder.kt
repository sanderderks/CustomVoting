package me.sd_master92.customvoting.tasks

import com.github.shynixn.mccoroutine.bukkit.launch
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.sendTexts
import me.sd_master92.customvoting.subjects.VoteSite
import org.bukkit.Bukkit
import org.bukkit.entity.Player

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
                    plugin.launch {
                        val voter = Voter.get(plugin, player)
                        val history = VoteSite.getAllActive(plugin).associateBy({ it.uniqueId }, { 0L }).toMutableMap()

                        for (vote in voter.getHistory())
                        {
                            if (history[vote.site] != null)
                            {
                                val lastVoteTime = history[vote.site] ?: 0
                                if (vote.time > lastVoteTime)
                                {
                                    history[vote.site] = vote.time
                                }
                            }
                        }

                        val currentTime = System.currentTimeMillis()
                        val firstSiteToVoteAgain = history.entries
                            .filter { (site, lastVoteTime) ->
                                currentTime >= (VoteSite.get(plugin, site)?.getNextVoteTime(lastVoteTime)
                                    ?: currentTime)
                            }
                            .minByOrNull { (_, lastVoteTime) -> lastVoteTime }
                            ?.key

                        if (firstSiteToVoteAgain != null)
                        {
                            player.sendTexts(
                                plugin,
                                Message.VOTE_REMINDER,
                                mapOf(
                                    Pair(
                                        "%SERVICE%",
                                        firstSiteToVoteAgain.serviceName
                                    )
                                )
                            )
                            SoundType.NOTIFY.play(plugin, player)
                        }
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