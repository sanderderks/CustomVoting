package me.sd_master92.customvoting.tasks

import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.interfaces.Voter
import java.util.*

class ResetChecker(private val plugin: CV)
{
    private var lastResetDay: Int = -1
    private var lastResetWeek: Int = -1
    private var lastResetMonth: Int = -1

    init
    {
        TaskTimer.repeat(plugin, 20 * 60 * 60, 20 * 3) {
            val calendar = Calendar.getInstance()

            if (calendar[Calendar.MONTH] != lastResetMonth)
            {
                lastResetMonth = calendar[Calendar.MONTH]
                for (voter in Voter.getTopVoters(plugin))
                {
                    voter.clearMonthlyVotes()
                }
            }

            if (calendar[Calendar.WEEK_OF_YEAR] != lastResetWeek)
            {
                lastResetWeek = calendar[Calendar.WEEK_OF_YEAR]
                for (voter in Voter.getTopVoters(plugin))
                {
                    voter.clearWeeklyVotes()
                }
            }

            if (calendar[Calendar.DAY_OF_MONTH] != lastResetDay)
            {
                lastResetDay = calendar[Calendar.DAY_OF_MONTH]
                for (voter in Voter.getTopVoters(plugin))
                {
                    voter.clearDailyVotes()
                }
            }
        }.run()
    }
}