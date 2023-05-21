package me.sd_master92.customvoting.tasks

import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.interfaces.Voter
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
                performActionForVoters(Voter::clearMonthlyVotes)
            }

            if (lastResetWeek < 0)
            {
                lastResetWeek = currentWeek
            } else if (lastResetWeek != currentWeek)
            {
                lastResetWeek = currentWeek
                performActionForVoters(Voter::clearWeeklyVotes)
            }

            if (lastResetDay < 0)
            {
                lastResetDay = currentDay
            } else if (lastResetDay != currentDay)
            {
                lastResetDay = currentDay
                performActionForVoters(Voter::clearDailyVotes)
            }
        }.run()
    }

    private fun performActionForVoters(action: (Voter) -> Unit)
    {
        Voter.getTopVoters(plugin).forEach(action)
    }
}