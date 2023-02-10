package me.sd_master92.customvoting.constants.interfaces

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.database.PlayerTable
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.VoteTopStand
import org.bukkit.entity.Player

interface Voter
{
    val uuid: String
    val name: String
    val votes: Int
    val votesDaily: Int
    val votesMonthly: Int
    val last: Long
    val power: Boolean
    val queue: List<String>
    val streakDaily: Int

    fun setVotes(n: Int, update: Boolean)
    fun addVote(): Boolean
    fun addQueue(site: String): Boolean
    fun clearMonthlyVotes()
    fun clearQueue(): Boolean
    fun setPower(power: Boolean): Boolean
    fun addStreak(): Boolean
    fun clearStreak(): Boolean
    fun addDailyVote(): Boolean
    fun clearDailyVotes(): Boolean

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
                val useMonthly = plugin.config.getBoolean(Setting.MONTHLY_VOTES.path)

                topVoters.sortWith { x: Voter, y: Voter ->
                    var compare = if (useMonthly)
                    {
                        y.votesMonthly.compareTo(x.votesMonthly)
                    } else
                    {
                        y.votes.compareTo(x.votes)
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
            return getTopVoters(plugin).firstOrNull { voter -> voter.name == name }
        }
    }
}