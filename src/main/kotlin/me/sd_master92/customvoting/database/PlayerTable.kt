package me.sd_master92.customvoting.database

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.interfaces.TopVoter
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.constants.models.VoteHistory
import me.sd_master92.customvoting.constants.models.VoteSiteUUID
import me.sd_master92.customvoting.dayDifferenceToday
import me.sd_master92.customvoting.monthDifferenceToday
import me.sd_master92.customvoting.weekDifferenceToday
import org.bukkit.entity.Player

class PlayerTable(private val plugin: CV, override val uuid: String) : Voter
{
    private val players: PlayerDatabase? = plugin.playerDatabase

    override val name: String
        get() = players?.getName(uuid) ?: PMessage.PLAYER_NAME_UNKNOWN.toString()
    override val votes: Int
        get() = players?.getVotes(uuid) ?: 0
    override val votesMonthly: Int
        get()
        {
            return if (last.monthDifferenceToday() > 0)
            {
                clearMonthlyVotes()
                0
            } else
            {
                players?.getMonthlyVotes(uuid) ?: 0
            }
        }
    override val votesWeekly: Int
        get()
        {
            return if (last.weekDifferenceToday() > 0)
            {
                clearWeeklyVotes()
                0
            } else
            {
                players?.getWeeklyVotes(uuid) ?: 0
            }
        }
    override val votesDaily: Int
        get()
        {
            return if (last.dayDifferenceToday() > 0)
            {
                clearDailyVotes()
                0
            } else
            {
                players?.getDailyVotes(uuid) ?: 0
            }
        }
    override val power: Boolean
        get() = players?.getPower(uuid) == true
    override val last: Long
        get() = history.maxByOrNull { it.time }?.time ?: 0
    override val history: List<VoteHistory>
        get() = players?.getHistory(uuid) ?: listOf()
    override val streakDaily: Int
        get() = players?.getStreak(uuid) ?: 0

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
        players?.setWeeklyVotes(uuid, 0)
        players?.setDailyVotes(uuid, 0)

        if (update)
        {
            Voter.getTopVoters(plugin, true)
        }
    }

    override fun addVote(): Boolean
    {
        val votesBefore = votes
        addStreak()
        players?.setVotes(uuid, votes + 1)
        players?.setMonthlyVotes(uuid, votesMonthly + 1)
        players?.setWeeklyVotes(uuid, votesWeekly + 1)
        players?.setDailyVotes(uuid, votesDaily + 1)

        Voter.getTopVoters(plugin, true)

        return votesBefore < votes
    }

    override fun setPower(power: Boolean): Boolean
    {
        return players?.setPower(uuid, power) ?: false
    }

    override fun clearQueue(): Boolean
    {
        return players?.clearQueue(uuid) ?: false
    }

    override fun addHistory(site: VoteSiteUUID, queued: Boolean): Boolean
    {
        return players?.addHistory(uuid, site, queued) ?: false
    }

    override fun addStreak(): Boolean
    {
        val diff = last.dayDifferenceToday()
        if (!plugin.config.getBoolean(Setting.VOTE_STREAK_CONSECUTIVE.path) || diff == 1 || votes == 0)
        {
            return players?.setStreak(uuid, streakDaily + 1) ?: false
        } else if (diff > 1)
        {
            clearStreak()
        }
        return false
    }

    override fun clearStreak(): Boolean
    {
        return players?.setStreak(uuid, 0) ?: false
    }

    override fun clearMonthlyVotes()
    {
        players?.setMonthlyVotes(uuid, 0)
    }

    override fun clearWeeklyVotes()
    {
        players?.setWeeklyVotes(uuid, 0) ?: false
    }

    override fun clearDailyVotes()
    {
        players?.setDailyVotes(uuid, 0) ?: false
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
                    PlayerTable(plugin, result.getString(PlayerTableColumn.UUID.columnName))
                }
            }
        }

        override fun getAll(plugin: CV): MutableList<Voter>
        {
            return ALL.values.toMutableList()
        }

        fun get(plugin: CV, player: Player): PlayerTable
        {
            return if (plugin.config.getBoolean(Setting.UUID_STORAGE.path)) getByUuid(plugin, player) else getByName(
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