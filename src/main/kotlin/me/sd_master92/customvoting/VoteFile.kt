package me.sd_master92.customvoting

import me.sd_master92.core.file.PlayerFile
import me.sd_master92.customvoting.constants.TopVoter
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.Setting
import org.bukkit.entity.Player

class VoteFile : Voter
{
    private val plugin: CV
    private val playerFile: PlayerFile

    override val uuid: String
        get() = playerFile.uuid
    override val name: String
        get() = playerFile.name
    override val votes: Int
        get() = playerFile.getNumber("votes")
    override val monthlyVotes: Int
        get() = playerFile.getNumber("monthly_votes")
    override val last: Long
        get() = playerFile.getTimeStamp("last")
    override val isOpUser: Boolean
        get() = playerFile.getBoolean("opUser")
    override val queue: List<String>
        get() = plugin.data.getStringList(Data.VOTE_QUEUE.path + ".$uuid")

    private constructor(uuid: String, plugin: CV)
    {
        playerFile = PlayerFile.getByUuid(plugin, uuid)
        this.plugin = plugin
        register()
    }

    private constructor(player: Player, plugin: CV)
    {
        playerFile = player.getPlayerFile(plugin)
        this.plugin = plugin
        register()
    }

    private fun register()
    {
        if (votes == 0)
        {
            setVotes(0, false)
        }
    }

    fun delete(): Boolean
    {
        if (playerFile.delete())
        {
            ALL.remove(uuid)
            Voter.getTopVoters(plugin, true)
            return true
        }
        return false
    }

    override fun setVotes(n: Int, update: Boolean)
    {
        playerFile.setTimeStamp("last")
        playerFile.setNumber("votes", n)
        playerFile.setNumber("monthly_votes", n)

        if (update)
        {
            Voter.getTopVoters(plugin, true)
        }
    }

    override fun clearMonthlyVotes()
    {
        playerFile.setNumber("monthly_votes", 0)

        Voter.getTopVoters(plugin, true)
    }

    override fun addVote(): Boolean
    {
        val beforeVotes = votes
        playerFile.setTimeStamp("last")
        playerFile.addNumber("votes", 1)
        playerFile.addNumber("monthly_votes", 1)

        Voter.getTopVoters(plugin, true)

        return beforeVotes < votes
    }

    override fun setIsOpUser(isOpUser: Boolean): Boolean
    {
        playerFile.set("opUser", isOpUser)
        return playerFile.saveConfig()
    }

    override fun clearQueue(): Boolean
    {
        return plugin.data.delete(Data.VOTE_QUEUE.path + ".$uuid")
    }

    override fun addQueue(site: String): Boolean
    {
        val path = Data.VOTE_QUEUE.path + ".$uuid"
        val queue = plugin.data.getStringList(path)
        queue.add(site)
        plugin.data[path] = queue
        return plugin.data.saveConfig()
    }

    companion object : TopVoter
    {
        private var ALL: MutableMap<String, VoteFile> = HashMap()

        fun init(plugin: CV)
        {
            PlayerFile.init(plugin)
            ALL = PlayerFile.getAll().values.map { playerFile -> VoteFile(playerFile.uuid, plugin) }
                .associateBy { file -> file.uuid }.toMutableMap()
            migrateAll()
        }

        fun mergeDuplicates(plugin: CV): Int
        {
            val voters = getAll(plugin)
            var deleted = 0
            for (voter in voters.filter { it -> it.uuid !in voters.distinctBy { it.name }.map { it.uuid } })
            {
                if (voter is VoteFile && voter.delete())
                {
                    deleted++
                }
            }
            return deleted
        }

        override fun getAll(plugin: CV): MutableList<Voter>
        {
            return ALL.values.toMutableList()
        }

        fun get(plugin: CV, player: Player): VoteFile
        {
            return if (plugin.config.getBoolean(Setting.UUID_STORAGE.path)) getByUuid(plugin, player) else getByName(
                plugin,
                player
            )
        }

        private fun getByUuid(plugin: CV, player: Player): VoteFile
        {
            return ALL.getOrDefault(player.uniqueId.toString(), VoteFile(player, plugin))
        }

        private fun getByName(plugin: CV, player: Player): VoteFile
        {
            return ALL.values.firstOrNull { file -> file.name == player.name } ?: VoteFile(player, plugin)
        }

        private fun migrateAll()
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