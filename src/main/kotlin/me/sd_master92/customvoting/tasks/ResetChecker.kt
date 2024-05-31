package me.sd_master92.customvoting.tasks

import com.github.shynixn.mccoroutine.bukkit.launch
import me.sd_master92.core.errorLog
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.dayDifferenceToday
import me.sd_master92.customvoting.monthDifferenceToday
import me.sd_master92.customvoting.weekDifferenceToday
import java.util.*

class ResetChecker(private val plugin: CV)
{
    private var lastResetMonth
        get() = plugin.config.getNumber(Setting.LAST_VOTE_RESET_MONTH.path)
        set(value)
        {
            plugin.config.setNumber(Setting.LAST_VOTE_RESET_MONTH.path, value)
        }
    private var lastResetWeek
        get() = plugin.config.getNumber(Setting.LAST_VOTE_RESET_WEEK.path)
        set(value)
        {
            plugin.config.setNumber(Setting.LAST_VOTE_RESET_WEEK.path, value)
        }
    private var lastResetDay
        get() = plugin.config.getNumber(Setting.LAST_VOTE_RESET_DAY.path)
        set(value)
        {
            plugin.config.setNumber(Setting.LAST_VOTE_RESET_DAY.path, value)
        }

    init
    {
        TaskTimer.repeat(plugin, 20 * 60 * 60, 20 * 3) {
            plugin.launch {
                val calendar = Calendar.getInstance()
                val currentMonth = calendar[Calendar.MONTH]
                val currentWeek = calendar[Calendar.WEEK_OF_YEAR]
                val currentDay = calendar[Calendar.DAY_OF_MONTH]

                if (lastResetMonth < 0)
                {
                    lastResetMonth = currentMonth
                } else if (lastResetMonth != currentMonth)
                {
                    lastResetMonth = currentMonth
                    performActionForVoters { voter ->
                        plugin.launch {
                            voter.clearMonthlyVotes()
                        }
                    }
                    plugin.errorLog("monthly votes reset, because a new month has started")
                }

                if (lastResetWeek < 0)
                {
                    lastResetWeek = currentWeek
                } else if (lastResetWeek != currentWeek)
                {
                    lastResetWeek = currentWeek
                    performActionForVoters { voter ->
                        plugin.launch {
                            voter.clearWeeklyVotes()
                        }
                    }
                    plugin.errorLog("weekly votes reset, because a new week has started")
                }

                if (lastResetDay < 0)
                {
                    lastResetDay = currentDay
                } else if (lastResetDay != currentDay)
                {
                    lastResetDay = currentDay
                    performActionForVoters { voter ->
                        plugin.launch {
                            voter.clearDailyVotes()
                        }
                    }
                    plugin.errorLog("daily votes reset, because a new day has started")
                }

                performActionForVoters { voter ->
                    plugin.launch {
                        val lastVote = voter.getLast()
                        if (voter.getVotesMonthly() > 0 && lastVote.monthDifferenceToday() > 0)
                        {
                            voter.clearMonthlyVotes()
                        }
                        if (voter.getVotesWeekly() > 0 && lastVote.weekDifferenceToday() > 0)
                        {
                            voter.clearWeeklyVotes()
                        }
                        if (voter.getVotesDaily() > 0 && lastVote.dayDifferenceToday() > 0)
                        {
                            voter.clearDailyVotes()
                        }
                    }
                }
            }
        }.run()
    }

    private suspend fun performActionForVoters(action: (Voter) -> Unit)
    {
        Voter.getTopVoters(plugin).forEach(action)
    }
}