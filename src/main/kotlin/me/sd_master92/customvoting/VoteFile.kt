package me.sd_master92.customvoting

import me.sd_master92.customfile.PlayerFile
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.VoteTopStand
import org.bukkit.entity.Player

class VoteFile : Voter
{
    private val plugin: CV
    private val playerFile: PlayerFile
    override val uuid: String
    override val name: String

    constructor(uuid: String, plugin: CV)
    {
        playerFile = PlayerFile.get(plugin, uuid)
        this.plugin = plugin
        this.uuid = uuid
        this.name = playerFile.name
        register()
    }

    constructor(player: Player, plugin: CV)
    {
        playerFile = PlayerFile.get(plugin, player)
        this.plugin = plugin
        this.uuid = player.uniqueId.toString()
        this.name = player.name
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
        get() = playerFile.getNumber("votes")
    override val period: Int
        get() = playerFile.getNumber("period")

    val last: Long
        get() = playerFile.getTimeStamp("last")

    fun setVotes(n: Int, update: Boolean)
    {
        playerFile.setTimeStamp("last")
        playerFile.setNumber("votes", n)
        playerFile.setNumber("period", n)
        if (update)
        {
            VoteTopSign.updateAll(plugin)
            VoteTopStand.updateAll(plugin)
        }
    }

    fun clearPeriod()
    {
        playerFile.setNumber("period", 0)
        VoteTopSign.updateAll(plugin)
        VoteTopStand.updateAll(plugin)
    }

    fun addVote(update: Boolean)
    {
        playerFile.setTimeStamp("last")
        playerFile.addNumber("votes", 1)
        playerFile.addNumber("period", 1)
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
        fun getTopVoters(plugin: CV): List<VoteFile>
        {
            val topVoters: MutableList<VoteFile> = ArrayList()
            for (playerFile in PlayerFile.getAll().values)
            {
                topVoters.add(VoteFile(playerFile.uuid, plugin))
            }

            if (plugin.config.getBoolean(Settings.MONTHLY_PERIOD))
            {
                topVoters.sortWith { x: VoteFile, y: VoteFile ->
                    var compare = y.period.compareTo(x.period)
                    if (compare == 0)
                    {
                        compare = x.last.compareTo(y.last)
                    }
                    compare
                }
            } else
            {
                topVoters.sortWith { x: VoteFile, y: VoteFile ->
                    var compare = y.votes.compareTo(x.votes)
                    if (compare == 0)
                    {
                        compare = x.last.compareTo(y.last)
                    }
                    compare
                }
            }
            return topVoters
        }

        fun getTopVoter(plugin: CV, n_: Int): VoteFile?
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