package me.sd_master92.customvoting.constants.interfaces

import me.sd_master92.customvoting.*
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.VoteSortType
import me.sd_master92.customvoting.constants.models.VoteHistory
import me.sd_master92.customvoting.constants.models.VoteSiteUUID
import me.sd_master92.customvoting.database.PlayerTable
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.stands.VoteTopStand
import me.sd_master92.customvoting.tasks.PlaceholderChecker
import org.bukkit.entity.Player
import java.util.*

interface Voter
{
    suspend fun getUuid(): UUID
    suspend fun getName(): String
    suspend fun getVotes(): Int
    suspend fun getVotesMonthly(): Int
    suspend fun getVotesWeekly(): Int
    suspend fun getVotesDaily(): Int
    suspend fun setVotes(n: Int, update: Boolean)
    suspend fun addVote(): Boolean
    suspend fun getLast(): Long
    suspend fun getHistory(): List<VoteHistory>
    suspend fun addHistory(site: VoteSiteUUID, queued: Boolean): Boolean
    suspend fun clearMonthlyVotes()
    suspend fun clearWeeklyVotes()
    suspend fun clearDailyVotes()
    suspend fun clearQueue(): Boolean
    suspend fun getPower(): Boolean
    suspend fun setPower(power: Boolean): Boolean
    suspend fun getStreakDaily(): Int
    suspend fun addStreak(): Boolean
    suspend fun clearStreak(): Boolean

    companion object
    {
        private var TOP_VOTERS: List<Voter> = listOf()

        suspend fun init(plugin: CV)
        {
            VoteFile.init(plugin)
            getTopVoters(plugin, true)
        }

        suspend fun getTopVoters(plugin: CV, update: Boolean? = null): List<Voter>
        {
            if (update == true)
            {
                val type = if (plugin.hasDatabaseConnection()) PlayerTable else VoteFile
                val topVoters = type.getAll()
                val sortType = VoteSortType.valueOf(plugin.config.getNumber(Setting.VOTES_SORT_TYPE.path))

                topVoters.sortWithAsync { x: Voter, y: Voter ->
                    var compare = when (sortType)
                    {
                        VoteSortType.ALL     -> y.getVotes().compareTo(x.getVotes())
                        VoteSortType.MONTHLY -> y.getVotesMonthly().compareTo(x.getVotesMonthly())
                        VoteSortType.WEEKLY  -> y.getVotesWeekly().compareTo(x.getVotesWeekly())
                        VoteSortType.DAILY   -> y.getVotesDaily().compareTo(x.getVotesDaily())
                    }
                    if (compare == 0)
                    {
                        compare = x.getLast().compareTo(y.getLast())
                    }
                    compare
                }
                TOP_VOTERS = topVoters

                VoteTopSign.updateAll(plugin)
                VoteTopStand.updateAll(plugin)
                PlaceholderChecker.updateAll(plugin, TOP_VOTERS)
            }
            return TOP_VOTERS
        }

        suspend fun getTopVoter(plugin: CV, top: Int): Voter?
        {
            var n = top
            n--
            val topVoters = getTopVoters(plugin)
            return if (n >= 0 && n < topVoters.size)
            {
                topVoters[n]
            } else null
        }

        suspend fun get(plugin: CV, player: Player): Voter
        {
            return if (plugin.hasDatabaseConnection())
            {
                PlayerTable.get(plugin, player)
            } else
            {
                VoteFile.get(plugin, player)
            }
        }

        suspend fun getByName(plugin: CV, name: String): Voter?
        {
            val topVoters = getTopVoters(plugin)
            return topVoters.firstOrNull { voter -> voter.getName() == name }
                ?: topVoters.firstOrNull { voter -> voter.getName() == name.getPlayerNameWithPrefix(plugin) }
                ?: topVoters.firstOrNull { voter -> voter.getName() == name.getPlayerNameWithoutPrefix(plugin) }
        }
    }
}