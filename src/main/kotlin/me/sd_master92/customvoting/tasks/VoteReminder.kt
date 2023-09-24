package me.sd_master92.customvoting.tasks

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
                    val voter = Voter.get(plugin, player)
                    val history = mutableMapOf<String, Long>()

                    for (vote in voter.history)
                    {
                        val lastVoteTime = history[vote.site] ?: 0
                        if (vote.time > lastVoteTime)
                        {
                            history[vote.site] = vote.time
                        }
                    }

                    val currentTime = System.currentTimeMillis()
                    val firstSiteToVoteAgain = history.entries
                        .filter { (site, lastVoteTime) ->
                            val siteInterval = VoteSite.get(plugin, site)?.interval ?: 24
                            currentTime - lastVoteTime >= (siteInterval * 60 * 60 * 1000)
                        }
                        .minByOrNull { (_, lastVoteTime) -> lastVoteTime }
                        ?.key

                    if (voter.votes == 0 || firstSiteToVoteAgain != null)
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