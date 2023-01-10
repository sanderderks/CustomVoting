package me.sd_master92.customvoting.database

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.TopVoter
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Settings
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
    override val queue: List<String>
        get() = players?.getQueue(uuid) ?: listOf()

    private constructor(plugin: CV, player: Player) : this(plugin, player.uniqueId.toString())
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
            Voter.getTopVoters(plugin, true)
        }
    }

    override fun clearMonthlyVotes()
    {
        players?.setMonthlyVotes(uuid, 0)

        Voter.getTopVoters(plugin, true)
    }

    override fun addVote(): Boolean
    {
        val votesBefore = votes
        players?.setVotes(uuid, votes + 1)
        players?.setMonthlyVotes(uuid, monthlyVotes + 1)
        players?.setLast(uuid)

        Voter.getTopVoters(plugin, true)

        return votesBefore < votes
    }

    override fun setIsOpUser(isOpUser: Boolean): Boolean
    {
        return players?.setIsOp(uuid, isOpUser) ?: false
    }

    override fun clearQueue(): Boolean
    {
        return players?.clearQueue(uuid) ?: false
    }

    override fun addQueue(site: String): Boolean
    {
        return players?.addQueue(uuid, site) ?: false
    }

    companion object : TopVoter
    {
        private var ALL: MutableMap<String, PlayerTable> = HashMap()

        fun init(plugin: CV)
        {
            val result = plugin.playerDatabase?.playersTable?.getAll()
            if (result != null)
            {
                while (result.next())
                {
                    val voter = PlayerTable(plugin, result.getString("uuid"))
                    ALL[voter.uuid] = voter
                }
            }
        }

        override fun getAll(plugin: CV): MutableList<Voter>
        {
            return ALL.values.toMutableList()
        }

        fun get(plugin: CV, player: Player): PlayerTable
        {
            return if (plugin.config.getBoolean(Settings.UUID_STORAGE.path)) getByUuid(plugin, player) else getByName(
                plugin,
                player
            )
        }

        private fun getByUuid(plugin: CV, player: Player): PlayerTable
        {
            return ALL.getOrDefault(player.uniqueId.toString(), PlayerTable(plugin, player))
        }

        private fun getByName(plugin: CV, player: Player): PlayerTable
        {
            return ALL.values.firstOrNull { file -> file.name == player.name } ?: PlayerTable(plugin, player)
        }
    }

    init
    {
        register()
    }
}