package me.sd_master92.customvoting.tasks

import com.github.shynixn.mccoroutine.bukkit.launch
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.constants.models.VoterData
import java.util.*

class PlaceholderChecker
{
    companion object
    {
        var SERVER_VOTES = 0
            private set
        var VOTE_PARTY_TOTAL = 0
            private set
        var VOTE_PARTY_CURRENT = 0
            private set
        var VOTE_PARTY_UNTIL = 0
            private set
        var VOTERS = mutableMapOf<UUID, VoterData>()

        fun updateAll(plugin: CV, voters: List<Voter>)
        {
            TaskTimer.delay(plugin, 20 * 3)
            {
                plugin.launch {
                    VOTE_PARTY_TOTAL = plugin.config.getInt(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path)
                    VOTE_PARTY_CURRENT = plugin.data.getInt(Data.CURRENT_VOTES.path)
                    VOTE_PARTY_UNTIL = VOTE_PARTY_TOTAL - VOTE_PARTY_CURRENT
                    for(voter in voters)
                    {
                        VOTERS[voter.getUuid()] = VoterData(
                            voter.getName(),
                            voter.getVotes(),
                            voter.getVotesMonthly(),
                            voter.getVotesWeekly(),
                            voter.getVotesDaily(),
                            voter.getStreakDaily()
                        )
                    }
                    SERVER_VOTES = VOTERS.values.sumOf { it.votes }
                }
            }.run()
        }
    }
}