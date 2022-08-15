package me.sd_master92.customvoting.constants

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.database.PlayerTable
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
    fun addVote(update: Boolean): Boolean
    fun addQueue(site: String): Boolean
    fun clearMonthlyVotes()
    fun clearQueue(): Boolean
    fun setIsOpUser(isOpUser: Boolean): Boolean

    companion object
    {
        fun getTopVoters(plugin: CV): List<Voter>
        {
            val type = if (plugin.hasDatabaseConnection()) PlayerTable else VoteFile
            val topVoters = type.getAll(plugin)
            topVoters.sortWith { x: Voter, y: Voter ->
                var compare = if (plugin.config.getBoolean(Settings.MONTHLY_VOTES.path))
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
            return topVoters
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

        fun getByUuid(plugin: CV, player: Player): Voter
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