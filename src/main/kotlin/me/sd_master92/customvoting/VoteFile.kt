package me.sd_master92.customvoting

import me.sd_master92.customfile.PlayerFile
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.VoteTopStand
import org.bukkit.entity.Player

class VoteFile : PlayerFile, Voter
{
    private val plugin: Main
    override val uniqueId: String
    override val userName: String

    constructor(uuid: String, plugin: Main) : super(uuid, plugin)
    {
        this.plugin = plugin
        this.uniqueId = uuid
        this.userName = name
        register()
    }

    constructor(player: Player, plugin: Main) : super(player, plugin)
    {
        this.plugin = plugin
        this.uniqueId = player.uniqueId.toString()
        this.userName = player.name
        register()
    }

    private fun register()
    {
        if (votes == 0)
        {
            setVotes(0, false)
        }
    }

    override val votes: Int
        get() = getNumber("votes")

    fun setVotes(n: Int, update: Boolean)
    {
        setTimeStamp("last")
        setNumber("votes", n)
        if (update)
        {
            VoteTopSign.updateAll(plugin)
            VoteTopStand.updateAll(plugin)
        }
    }

    fun addVote(update: Boolean)
    {
        setTimeStamp("last")
        addNumber("votes", 1)
        if (update)
        {
            VoteTopSign.updateAll(plugin)
            VoteTopStand.updateAll(plugin)
        }
    }

    val queue: List<String>
        get() = plugin.data.getStringList(Data.VOTE_QUEUE + "." + uuid)

    fun clearQueue(): Boolean
    {
        return plugin.data.delete(Data.VOTE_QUEUE + "." + uuid)
    }

    fun addQueue(service: String): Boolean
    {
        val path = Data.VOTE_QUEUE + "." + uuid
        val queue = plugin.data.getStringList(path)
        queue.add(service)
        plugin.data[path] = queue
        return plugin.data.saveConfig()
    }

    companion object
    {
        fun getTopVoters(plugin: Main): List<VoteFile>
        {
            val topVoters: MutableList<VoteFile> = ArrayList()
            for (playerFile in getAll(plugin))
            {
                topVoters.add(VoteFile(playerFile.uuid, plugin))
            }
            topVoters.sortWith { x: VoteFile, y: VoteFile ->
                var compare = y.votes.compareTo(x.votes)
                if (compare == 0)
                {
                    compare = x.getTimeStamp("last").compareTo(y.getTimeStamp("last"))
                }
                compare
            }
            return topVoters
        }

        fun getTopVoter(plugin: Main, n_: Int): VoteFile?
        {
            var n = n_
            n--
            val topVoters = getTopVoters(plugin)
            return if (n >= 0 && n < topVoters.size)
            {
                topVoters[n]
            } else null
        }
    }
}