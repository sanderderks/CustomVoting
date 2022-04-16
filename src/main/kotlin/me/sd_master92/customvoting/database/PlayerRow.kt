package me.sd_master92.customvoting.database

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.VoteTopStand
import org.bukkit.entity.Player

class PlayerRow(private val plugin: CV, override val uuid: String) : Voter
{
    private val players: PlayerTable? = plugin.playerTable

    constructor(plugin: CV, player: Player) : this(plugin, player.uniqueId.toString())
    {
        players?.setName(player.uniqueId.toString(), player.name)
    }

    private fun register()
    {
        players?.getVotes(uuid)
    }

    override val name: String
        get() = players?.getName(uuid) ?: "Unknown"
    override val votes: Int
        get() = players?.getVotes(uuid) ?: 0
    override val period: Int
        get() = players?.getPeriod(uuid) ?: 0

    fun setVotes(n: Int, update: Boolean)
    {
        players?.setVotes(uuid, n)
        players?.setPeriod(uuid, 0)
        players?.setLast(uuid)
        if (update)
        {
            VoteTopSign.updateAll(plugin)
            VoteTopStand.updateAll(plugin)
        }
    }

    fun clearPeriod()
    {
        players?.setPeriod(uuid, 0)
        VoteTopSign.updateAll(plugin)
        VoteTopStand.updateAll(plugin)
    }

    fun addVote(update: Boolean)
    {
        players?.setVotes(uuid, votes + 1)
        players?.setPeriod(uuid, period + 1)
        players?.setLast(uuid)
        if (update)
        {
            VoteTopSign.updateAll(plugin)
            VoteTopStand.updateAll(plugin)
        }
    }

    override val last: Long
        get() = players?.getLast(uuid) ?: 0

    val queue: Int
        get() = players?.getQueue(uuid) ?: 0

    fun clearQueue(): Boolean
    {
        return players?.setQueue(uuid, 0) ?: false
    }

    fun addQueue(): Boolean
    {
        return players?.setQueue(uuid, queue + 1) ?: false
    }

    init
    {
        register()
    }
}