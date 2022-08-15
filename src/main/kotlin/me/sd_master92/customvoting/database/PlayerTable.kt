package me.sd_master92.customvoting.database

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.TopVoter
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.VoteTopStand
import org.bukkit.entity.Player

class PlayerTable(private val plugin: CV, override val uuid: String) : Voter
{
    private val players: PlayerDatabase? = plugin.playerDatabase

    override val name: String
        get() = players?.getName(uuid) ?: "Unknown"
    override val votes: Int
        get() = players?.getVotes(uuid) ?: 0
    override val monthlyVotes: Int
        get() = players?.getMonthlyVotes(uuid) ?: 0
    override val isOpUser: Boolean
        get() = players?.getIsOp(uuid) == true
    override val last: Long
        get() = players?.getLast(uuid) ?: 0
    val queue: Int
        get() = players?.getQueue(uuid) ?: 0

    constructor(plugin: CV, player: Player) : this(plugin, player.uniqueId.toString())
    {
        players?.setName(player.uniqueId.toString(), player.name)
    }

    private fun register()
    {
        players?.getVotes(uuid)
        ALL[uuid] = this
    }

    override fun setVotes(n: Int, update: Boolean)
    {
        players?.setVotes(uuid, n)
        players?.setMonthlyVotes(uuid, 0)
        players?.setLast(uuid)
        if (update)
        {
            VoteTopSign.updateAll(plugin)
            VoteTopStand.updateAll(plugin)
        }
    }

    override fun clearMonthlyVotes()
    {
        players?.setMonthlyVotes(uuid, 0)
        VoteTopSign.updateAll(plugin)
        VoteTopStand.updateAll(plugin)
    }

    override fun addVote(update: Boolean): Boolean
    {
        val votesBefore = votes
        players?.setVotes(uuid, votes + 1)
        players?.setMonthlyVotes(uuid, monthlyVotes + 1)
        players?.setLast(uuid)
        if (update)
        {
            VoteTopSign.updateAll(plugin)
            VoteTopStand.updateAll(plugin)
        }
        return votesBefore < votes
    }

    override fun setIsOpUser(isOpUser: Boolean): Boolean
    {
        return players?.setIsOp(uuid, isOpUser) ?: false
    }

    fun clearQueue(): Boolean
    {
        return players?.setQueue(uuid, 0) ?: false
    }

    fun addQueue(): Boolean
    {
        return players?.setQueue(uuid, queue + 1) ?: false
    }

    companion object : TopVoter
    {
        private var ALL: MutableMap<String, PlayerTable> = HashMap()
        private var initialized = false

        fun init(plugin: CV)
        {
            if (!initialized)
            {
                val result = plugin.playerDatabase?.table?.getAll()
                if (result != null)
                {
                    while (result.next())
                    {
                        val voter = PlayerTable(plugin, result.getString("uuid"))
                        ALL[voter.uuid] = voter
                    }
                }
                initialized = true
            }
        }

        override fun getAll(plugin: CV): MutableList<Voter>
        {
            return ALL.values.toMutableList()
        }
    }

    init
    {
        register()
    }
}