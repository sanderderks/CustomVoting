package me.sd_master92.customvoting.constants.interfaces

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.VoteSortType
import me.sd_master92.customvoting.constants.models.VoteHistory
import me.sd_master92.customvoting.constants.models.VoteSiteUUID
import me.sd_master92.customvoting.database.PlayerTable
import me.sd_master92.customvoting.getPlayerNameWithPrefix
import me.sd_master92.customvoting.getPlayerNameWithoutPrefix
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.stands.VoteTopStand
import org.bukkit.entity.Player
import java.util.*

interface Voter
{
    val uuid: UUID
    val name: String
    val votes: Int
    val votesMonthly: Int
    val votesWeekly: Int
    val votesDaily: Int
    val last: Long
    val power: Boolean
    val history: List<VoteHistory>
    val streakDaily: Int

    fun setVotes(n: Int, update: Boolean)
    fun addVote(): Boolean
    fun addHistory(site: VoteSiteUUID, queued: Boolean): Boolean
    fun clearMonthlyVotes()
    fun clearWeeklyVotes()
    fun clearDailyVotes()
    fun clearQueue(): Boolean
    fun setPower(power: Boolean): Boolean
    fun addStreak(): Boolean
    fun clearStreak(): Boolean

    companion object
    {
        private var TOP_VOTERS: List<Voter> = listOf()

        fun init(plugin: CV)
        {
            VoteFile.init(plugin)
            getTopVoters(plugin, true)
        }

        fun getTopVoters(plugin: CV, update: Boolean? = null): List<Voter>
        {
            if (update == true)
            {
                val type = if (plugin.hasDatabaseConnection()) PlayerTable else VoteFile
                val topVoters = type.getAll(plugin)
                val sortType = VoteSortType.valueOf(plugin.config.getNumber(Setting.VOTES_SORT_TYPE.path))

                topVoters.sortWith { x: Voter, y: Voter ->
                    var compare = when (sortType)
                    {
                        VoteSortType.ALL     -> y.votes.compareTo(x.votes)
                        VoteSortType.MONTHLY -> y.votesMonthly.compareTo(x.votesMonthly)
                        VoteSortType.WEEKLY  -> y.votesWeekly.compareTo(x.votesWeekly)
                        VoteSortType.DAILY   -> y.votesDaily.compareTo(x.votesDaily)
                    }
                    if (compare == 0)
                    {
                        compare = x.last.compareTo(y.last)
                    }
                    compare
                }
                TOP_VOTERS = topVoters

                VoteTopSign.updateAll(plugin)
                VoteTopStand.updateAll(plugin)
            }
            return TOP_VOTERS
        }

        fun getTopVoter(plugin: CV, top: Int): Voter?
        {
            var n = top
            n--
            val topVoters = getTopVoters(plugin)
            return if (n >= 0 && n < topVoters.size)
            {
                topVoters[n]
            } else null
        }

        fun get(plugin: CV, player: Player): Voter
        {
            return if (plugin.hasDatabaseConnection())
            {
                PlayerTable.get(plugin, player)
            } else
            {
                VoteFile.get(plugin, player)
            }
        }

        fun getByName(plugin: CV, name: String): Voter?
        {
            val topVoters = getTopVoters(plugin)
            return topVoters.firstOrNull { voter -> voter.name == name }
                ?: topVoters.firstOrNull { voter -> voter.name == name.getPlayerNameWithPrefix(plugin) }
                ?: topVoters.firstOrNull { voter -> voter.name == name.getPlayerNameWithoutPrefix(plugin) }
        }
    }
}