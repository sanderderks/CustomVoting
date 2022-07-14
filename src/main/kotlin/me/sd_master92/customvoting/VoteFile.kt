package me.sd_master92.customvoting

import me.sd_master92.core.file.PlayerFile
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.VoteTopStand
import org.bukkit.entity.Player

class VoteFile : Voter
{
    private val plugin: CV
    private val playerFile: PlayerFile
    override val uuid: String
    override val name: String

    override val votes: Int
        get() = playerFile.getNumber("votes")
    override val monthlyVotes: Int
        get() = playerFile.getNumber("monthly_votes")
    override val last: Long
        get() = playerFile.getTimeStamp("last")
    val isOpUser: Boolean
        get() = playerFile.getBoolean("opUser")

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

    fun setVotes(n: Int, update: Boolean)
    {
        playerFile.setTimeStamp("last")
        playerFile.setNumber("votes", n)
        playerFile.setNumber("monthly_votes", n)
        if (update)
        {
            VoteTopSign.updateAll(plugin)
            VoteTopStand.updateAll(plugin)
        }
    }

    fun clearMonthlyVotes()
    {
        playerFile.setNumber("monthly_votes", 0)
        VoteTopSign.updateAll(plugin)
        VoteTopStand.updateAll(plugin)
    }

    fun addVote(update: Boolean): Boolean
    {
        val beforeVotes = votes
        playerFile.setTimeStamp("last")
        playerFile.addNumber("votes", 1)
        playerFile.addNumber("monthly_votes", 1)
        if (update)
        {
            VoteTopSign.updateAll(plugin)
            VoteTopStand.updateAll(plugin)
        }
        return beforeVotes < votes
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

    fun setIsOpUser(isOpUser: Boolean): Boolean
    {
        playerFile.set("opUser", isOpUser)
        return playerFile.saveConfig()
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

            topVoters.sortWith { x: VoteFile, y: VoteFile ->
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

        fun migrateAll()
        {
            for (playerFile in PlayerFile.getAll().values)
            {
                if (playerFile.contains("period"))
                {
                    playerFile.set("monthly_votes", playerFile.get("period"))
                    playerFile.delete("period")
                }
            }
        }
    }
}