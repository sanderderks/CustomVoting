package me.sd_master92.customvoting.database

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.VoteTopStand
import org.bukkit.entity.Player

class PlayerRow(private val plugin: Main, override val uniqueId: String) : Voter
{
    private val players: PlayerTable? = plugin.playerTable

    constructor(plugin: Main, player: Player) : this(plugin, player.uniqueId.toString())
    {
        players?.setName(player.uniqueId.toString(), player.name)
    }

    private fun register()
    {
        players?.getVotes(uniqueId)
    }

    override val userName: String
        get() = players?.getName(uniqueId) ?: "Unknown"
    override val votes: Int
        get() = players?.getVotes(uniqueId) ?: 0
    override val period: Int
        get() = players?.getPeriod(uniqueId) ?: 0

    fun setVotes(n: Int, update: Boolean)
    {
        players?.setVotes(uniqueId, n)
        players?.setPeriod(uniqueId, 0)
        players?.setLast(uniqueId)
        if (update)
        {
            VoteTopSign.updateAll(plugin)
            VoteTopStand.updateAll(plugin)
        }
    }

    fun clearPeriod()
    {
        players?.setPeriod(uniqueId, 0)
        VoteTopSign.updateAll(plugin)
        VoteTopStand.updateAll(plugin)
    }

    fun addVote(update: Boolean)
    {
        players?.setVotes(uniqueId, votes + 1)
        players?.setPeriod(uniqueId, period + 1)
        players?.setLast(uniqueId)
        if (update)
        {
            VoteTopSign.updateAll(plugin)
            VoteTopStand.updateAll(plugin)
        }
    }

    val last: Long
        get() = players?.getLast(uniqueId) ?: 0

    val queue: Int
        get() = players?.getQueue(uniqueId) ?: 0

    fun clearQueue(): Boolean
    {
        return players?.setQueue(uniqueId, 0) ?: false
    }

    fun addQueue(): Boolean
    {
        return players?.setQueue(uniqueId, queue + 1) ?: false
    }

    init
    {
        register()
    }
}