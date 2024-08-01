package me.sd_master92.customvoting

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.sd_master92.core.file.PlayerFile
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.interfaces.TopVoter
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.constants.models.VoteHistory
import me.sd_master92.customvoting.constants.models.VoteSiteUUID
import org.bukkit.entity.Player
import java.util.*

class VoteFile : Voter
{
    private val plugin: CV
    private val playerFile: PlayerFile

    private constructor(uuid: UUID, plugin: CV)
    {
        playerFile = PlayerFile.getByUuid(plugin, uuid)
        this.plugin = plugin
        runBlocking {
            register()
        }
    }

    private constructor(player: Player, plugin: CV)
    {
        playerFile = player.getPlayerFile(plugin)
        this.plugin = plugin
        runBlocking {
            register()
        }
    }

    private suspend fun register()
    {
        if (getVotes() == 0)
        {
            setVotes(0, false)
        }
    }

    override suspend fun getUuid(): UUID
    {
        return playerFile.uuid
    }

    override suspend fun getName(): String
    {
        return playerFile.name
    }

    override suspend fun setName(name: String): Boolean
    {
        return playerFile.setName(name)
    }

    override suspend fun getVotes(): Int
    {
        return playerFile.getNumber(VOTES)
    }

    override suspend fun getVotesMonthly(): Int
    {
        return playerFile.getNumber(VOTES_MONTHLY)
    }

    override suspend fun getVotesWeekly(): Int
    {
        return playerFile.getNumber(VOTES_WEEKLY)
    }

    override suspend fun getVotesDaily(): Int
    {
        return playerFile.getNumber(VOTES_DAILY)
    }

    override suspend fun getLast(): Long
    {
        return getHistory().maxByOrNull { it.time }?.time ?: 0
    }

    override suspend fun getPower(): Boolean
    {
        return playerFile.getBoolean(POWER)
    }

    override suspend fun getHistory(): List<VoteHistory>
    {
        val history = mutableListOf<VoteHistory>()
        for (key in playerFile.getConfigurationSection(Data.VOTE_HISTORY.path)?.getKeys(false) ?: listOf())
        {
            val site = playerFile.getString(Data.VOTE_HISTORY.path + ".$key.site")
            val time = playerFile.getTimeStamp(Data.VOTE_HISTORY.path + ".$key.timestamp")
            val queued = playerFile.getBoolean(Data.VOTE_HISTORY.path + ".$key.queued")
            if (site != null)
            {
                history.add(VoteHistory(key.toInt(), getUuid(), VoteSiteUUID(site), time, queued))
            }
        }
        return history
    }

    override suspend fun getStreakDaily(): Int
    {
        return playerFile.getNumber(STREAK_DAILY)
    }

    override suspend fun setStreakDaily(n: Int): Boolean
    {
        playerFile.set(STREAK_DAILY, n)
        return playerFile.saveConfig()
    }

    suspend fun delete(): Boolean
    {
        if (playerFile.delete())
        {
            ALL.remove(getUuid())
            Voter.getTopVoters(plugin, true)
            return true
        }
        return false
    }

    override suspend fun setVotes(n: Int, update: Boolean)
    {
        playerFile.setNumber(VOTES, n)
        playerFile.setNumber(VOTES_MONTHLY, 0)
        playerFile.setNumber(VOTES_WEEKLY, 0)
        playerFile.setNumber(VOTES_DAILY, 0)

        if (n == 0)
        {
            clearStreak()
        }

        if (update)
        {
            Voter.getTopVoters(plugin, true)
        }
    }

    override suspend fun clearMonthlyVotes()
    {
        playerFile.setNumber(VOTES_MONTHLY, 0)
    }

    override suspend fun clearWeeklyVotes()
    {
        playerFile.setNumber(VOTES_WEEKLY, 0)
    }

    override suspend fun clearDailyVotes()
    {
        playerFile.setNumber(VOTES_DAILY, 0)
    }

    override suspend fun addVote(site: VoteSiteUUID, queued: Boolean): Boolean
    {
        val beforeVotes = getVotes()
        addStreak()
        addHistory(site, queued)
        playerFile.addNumber(VOTES)
        playerFile.addNumber(VOTES_MONTHLY)
        playerFile.addNumber(VOTES_WEEKLY)
        playerFile.addNumber(VOTES_DAILY)

        Voter.getTopVoters(plugin, true)

        return beforeVotes < getVotes()
    }

    override suspend fun setPower(power: Boolean): Boolean
    {
        playerFile.set(POWER, power)
        return playerFile.saveConfig()
    }

    override suspend fun addHistory(site: VoteSiteUUID, queued: Boolean): Boolean
    {
        val key = getHistory().size
        playerFile.set(Data.VOTE_HISTORY.path + ".$key.site", site.toString())
        playerFile.setTimeStamp(Data.VOTE_HISTORY.path + ".$key.timestamp")
        playerFile.set(Data.VOTE_HISTORY.path + ".$key.queued", queued)
        return playerFile.saveConfig()
    }

    override suspend fun clearQueue(): Boolean
    {
        for (vote in getHistory().filter { it.queued })
        {
            playerFile.set(Data.VOTE_HISTORY.path + ".${vote.id}.queued", false)
        }
        return playerFile.saveConfig()
    }

    override suspend fun addStreak(): Boolean
    {
        val diff = getLast().dayDifference()
        if (!plugin.config.getBoolean(Setting.VOTE_STREAK_CONSECUTIVE.path) || diff == 1 || getVotes() == 0)
        {
            return playerFile.addNumber(STREAK_DAILY, 1)
        } else if (diff > 1)
        {
            clearStreak()
        }
        return false
    }

    override suspend fun clearStreak(): Boolean
    {
        return playerFile.setNumber(STREAK_DAILY, 0)
    }

    companion object : TopVoter
    {
        private const val VOTES = "votes"
        private const val VOTES_MONTHLY = "votes_monthly"
        private const val VOTES_WEEKLY = "votes_weekly"
        private const val VOTES_DAILY = "votes_daily"
        private const val STREAK_DAILY = "streak_daily"
        private const val POWER = "power"

        private var ALL: MutableMap<UUID, VoteFile> = HashMap()

        suspend fun init(plugin: CV)
        {
            PlayerFile.init(plugin)
            ALL = withContext(Dispatchers.IO) {
                PlayerFile.getAll().values.map { playerFile -> VoteFile(playerFile.uuid, plugin) }
                    .associateBy { file -> file.getUuid() }.toMutableMap()
            }
            migrateAll()
        }

        suspend fun mergeDuplicates(): Int
        {
            val voters = getAll()
            var deleted = 0
            for (voter in voters.filter { it ->
                it.getUuid() !in voters.distinctBy { it.getName() }.map { it.getUuid() }
            })
            {
                if (voter is VoteFile && voter.delete())
                {
                    deleted++
                }
            }
            return deleted
        }

        override fun getAll(): MutableList<Voter>
        {
            return ALL.values.toMutableList()
        }

        suspend fun get(plugin: CV, player: Player): VoteFile
        {
            return if (plugin.config.getBoolean(Setting.UUID_STORAGE.path)) getByUuid(plugin, player) else getByName(
                plugin,
                player
            )
        }

        private fun getByUuid(plugin: CV, player: Player): VoteFile
        {
            return ALL.getOrElse(player.uniqueId) {
                val voter = VoteFile(player, plugin)
                ALL[player.uniqueId] = voter
                return voter
            }
        }

        fun getByUuid(plugin: CV, uniqueId: UUID): VoteFile
        {
            return ALL.getOrElse(uniqueId) {
                val voter = VoteFile(uniqueId, plugin)
                ALL[uniqueId] = voter
                return voter
            }
        }

        private suspend fun getByName(plugin: CV, player: Player): VoteFile
        {
            val ignoreCase = plugin.config.getBoolean(Setting.IGNORE_PLAYERNAME_CASING.path)
            var voter = ALL.values.firstOrNull { file -> file.getName().equals(player.name, ignoreCase) }
            if (voter == null)
            {
                voter = VoteFile(player, plugin)
                ALL[player.uniqueId] = voter
            }
            return voter
        }

        private fun migrateAll()
        {
            val keyMigrations = mapOf(
                Pair("period", "monthly_votes"),
                Pair("monthly_votes", VOTES_MONTHLY),
                Pair("opUser", POWER)
            )

            for (playerFile in PlayerFile.getAll().values)
            {
                playerFile.keyMigrations(keyMigrations)

                playerFile.delete("last")
            }
        }
    }
}