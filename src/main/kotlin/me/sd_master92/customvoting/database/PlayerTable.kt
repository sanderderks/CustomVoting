package me.sd_master92.customvoting.database

import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.runBlocking
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.interfaces.TopVoter
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.constants.models.VoteHistory
import me.sd_master92.customvoting.constants.models.VoteSiteUUID
import me.sd_master92.customvoting.dayDifference
import org.bukkit.entity.Player
import java.util.*

class PlayerTable(private val plugin: CV, val uuid: UUID) : Voter
{
    private val players: PlayerDatabase? = plugin.playerDatabase

    private constructor(plugin: CV, player: Player) : this(plugin, player.uniqueId)
    {
        plugin.launch {
            players?.setName(player.uniqueId, player.name)
        }
    }

    private suspend fun register()
    {
        players?.getVotes(uuid)
        ALL[uuid] = this
    }

    override suspend fun getUuid(): UUID
    {
        return uuid
    }

    override suspend fun getName(): String
    {
        return players?.getName(uuid) ?: PMessage.PLAYER_NAME_UNKNOWN.toString()
    }

    override suspend fun setName(name: String): Boolean
    {
        return players?.setName(uuid, name) ?: false
    }

    override suspend fun getVotes(): Int
    {
        return players?.getVotes(uuid) ?: 0
    }

    override suspend fun getVotesMonthly(): Int
    {
        return players?.getMonthlyVotes(uuid) ?: 0
    }

    override suspend fun getVotesWeekly(): Int
    {
        return players?.getWeeklyVotes(uuid) ?: 0
    }

    override suspend fun getVotesDaily(): Int
    {
        return players?.getDailyVotes(uuid) ?: 0
    }

    override suspend fun getPower(): Boolean
    {
        return players?.getPower(uuid) == true
    }

    override suspend fun getLast(): Long
    {
        return getHistory().maxByOrNull { it.time }?.time ?: 0
    }

    override suspend fun getHistory(): List<VoteHistory>
    {
        return players?.getHistory(uuid) ?: listOf()
    }

    override suspend fun getStreakDaily(): Int
    {
        return players?.getStreak(uuid) ?: 0
    }

    override suspend fun setStreakDaily(n: Int): Boolean
    {
        return players?.setStreak(uuid, n) ?: false
    }

    override suspend fun setVotes(n: Int, update: Boolean)
    {
        plugin.launch {
            players?.setVotes(uuid, n)
            players?.setMonthlyVotes(uuid, 0)
            players?.setWeeklyVotes(uuid, 0)
            players?.setDailyVotes(uuid, 0)

            if (update)
            {
                Voter.getTopVoters(plugin, true)
            }
        }
    }

    override suspend fun addVote(site: VoteSiteUUID, queued: Boolean): Boolean
    {
        val votesBefore = getVotes()
        addStreak()
        addHistory(site, queued)
        players?.setVotes(uuid, getVotes() + 1)
        players?.setMonthlyVotes(uuid, getVotesMonthly() + 1)
        players?.setWeeklyVotes(uuid, getVotesWeekly() + 1)
        players?.setDailyVotes(uuid, getVotesDaily() + 1)

        Voter.getTopVoters(plugin, true)

        return votesBefore < getVotes()
    }

    override suspend fun setPower(power: Boolean): Boolean
    {
        return players?.setPower(uuid, power) ?: false
    }

    override suspend fun clearQueue(): Boolean
    {
        return players?.clearQueue(uuid) ?: false
    }

    override suspend fun addHistory(site: VoteSiteUUID, queued: Boolean): Boolean
    {
        return players?.addHistory(uuid, site, queued) ?: false
    }

    override suspend fun addStreak(): Boolean
    {
        val diff = getLast().dayDifference()
        if (!plugin.config.getBoolean(Setting.VOTE_STREAK_CONSECUTIVE.path) || diff == 1 || getVotes() == 0)
        {
            return players?.setStreak(uuid, getStreakDaily() + 1) ?: false
        } else if (diff > 1)
        {
            clearStreak()
        }
        return false
    }

    override suspend fun clearStreak(): Boolean
    {
        return players?.setStreak(uuid, 0) ?: false
    }

    override suspend fun clearMonthlyVotes()
    {
        players?.setMonthlyVotes(uuid, 0)
    }

    override suspend fun clearWeeklyVotes()
    {
        players?.setWeeklyVotes(uuid, 0) ?: false
    }

    override suspend fun clearDailyVotes()
    {
        players?.setDailyVotes(uuid, 0) ?: false
    }

    companion object : TopVoter
    {
        private var ALL: MutableMap<UUID, PlayerTable> = HashMap()

        fun init(plugin: CV)
        {
            plugin.launch {
                val result = plugin.playerDatabase?.playersTable?.getAllAsync()
                if (result != null)
                {
                    while (result.next())
                    {
                        try
                        {
                            PlayerTable(plugin, UUID.fromString(result.getString(PlayerTableColumn.UUID.columnName)))
                        } catch (_: Exception)
                        {
                        }
                    }
                }
            }
        }

        override fun getAll(): MutableList<Voter>
        {
            return ALL.values.toMutableList()
        }

        suspend fun get(plugin: CV, player: Player): PlayerTable
        {
            return if (plugin.config.getBoolean(Setting.UUID_STORAGE.path)) getByUuid(plugin, player) else getByName(
                plugin,
                player
            )
        }

        private fun getByUuid(plugin: CV, player: Player): PlayerTable
        {
            return ALL.getOrElse(player.uniqueId) {
                val voter = PlayerTable(plugin, player)
                ALL[player.uniqueId] = voter
                return voter
            }
        }

        private suspend fun getByName(plugin: CV, player: Player): PlayerTable
        {
            var voter = ALL.values.firstOrNull { file -> file.getName() == player.name }
            if (voter == null)
            {
                voter = PlayerTable(plugin, player)
                ALL[player.uniqueId] = voter
            }
            return voter
        }
    }

    init
    {
        runBlocking {
            register()
        }
    }
}