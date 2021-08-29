package me.sd_master92.customvoting.database

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.VoteTopStand
import org.bukkit.entity.Player

class PlayerRow(private val plugin: Main, override val uniqueId: String) : Voter
{
    private val players: PlayerTable = plugin.playerTable

    constructor(plugin: Main, player: Player) : this(plugin, player.uniqueId.toString())
    {
        players.setName(player.uniqueId.toString(), player.name)
    }

    private fun register()
    {
        players.getVotes(uniqueId)
    }

    override val userName: String
        get() = players.getName(uniqueId)
    override val votes: Int
        get() = players.getVotes(uniqueId)

    fun setVotes(n: Int, update: Boolean)
    {
        players.setVotes(uniqueId, n)
        players.setLast(uniqueId)
        if (update)
        {
            VoteTopSign.updateAll(plugin)
            VoteTopStand.updateAll(plugin)
        }
    }

    fun addVote(update: Boolean)
    {
        players.setVotes(uniqueId, votes + 1)
        players.setLast(uniqueId)
        if (update)
        {
            VoteTopSign.updateAll(plugin)
            VoteTopStand.updateAll(plugin)
        }
    }

    val queue: Int
        get() = players.getQueue(uniqueId)

    fun clearQueue(): Boolean
    {
        return players.setQueue(uniqueId, 0)
    }

    fun addQueue(): Boolean
    {
        return players.setQueue(uniqueId, queue + 1)
    }

    init
    {
        register()
    }
}