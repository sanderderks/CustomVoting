package me.sd_master92.customvoting.constants

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.database.PlayerTable
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.VoteTopStand
import org.bukkit.entity.Player

interface Voter
{
    val uuid: String
    val name: String
    val votes: Int
    val monthlyVotes: Int
    val last: Long
    val isOpUser: Boolean
    val queue: List<String>

    fun setVotes(n: Int, update: Boolean)
    fun addVote(): Boolean
    fun addQueue(site: String): Boolean
    fun clearMonthlyVotes()
    fun clearQueue(): Boolean
    fun setIsOpUser(isOpUser: Boolean): Boolean

    companion object
    {
        private var TOP_VOTERS: List<Voter> = listOf()

        fun init(plugin: CV)
        {
            VoteFile.init(plugin)
            PlayerTable.init(plugin)
            getTopVoters(plugin, true)
        }

        fun getTopVoters(plugin: CV, update: Boolean? = null): List<Voter>
        {
            if (update == true)
            {
                val type = if (plugin.hasDatabaseConnection()) PlayerTable else VoteFile
                val topVoters = type.getAll(plugin)
                val useMonthly = plugin.config.getBoolean(Settings.MONTHLY_VOTES.path)

                topVoters.sortWith { x: Voter, y: Voter ->
                    var compare = if (useMonthly)
                    {
                        y.monthlyVotes.compareTo(x.monthlyVotes)
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
                PlayerTable(plugin, player)
            } else
            {
                VoteFile(player, plugin)
            }
        }

        fun getByName(plugin: CV, name: String): Voter?
        {
            return getTopVoters(plugin).firstOrNull { voter -> voter.name == name }
        }
    }
}