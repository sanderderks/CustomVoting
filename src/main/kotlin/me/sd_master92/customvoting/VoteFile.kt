package me.sd_master92.customvoting

import me.sd_master92.core.file.PlayerFile
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.interfaces.TopVoter
import me.sd_master92.customvoting.constants.interfaces.Voter
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
        get() = playerFile.getNumber(VOTES)
    override val votesMonthly: Int
        get()
        {
            return if (last.monthDifferenceToday() > 0)
            {
                clearMonthlyVotes()
                0
            } else
            {
                playerFile.getNumber(VOTES_MONTHLY)
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
                playerFile.getNumber(VOTES_WEEKLY)
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
                playerFile.getNumber(VOTES_DAILY)
            }
        }
    override val last: Long
        get() = playerFile.getTimeStamp(VOTE_LAST)
    override val power: Boolean
        get() = playerFile.getBoolean(POWER)
    override val queue: List<String>
        get() = playerFile.getStringList(Data.VOTE_QUEUE.path)
    override val streakDaily: Int
        get() = playerFile.getNumber(STREAK_DAILY)

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
        playerFile.setTimeStamp(VOTE_LAST)
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

    override fun clearMonthlyVotes()
    {
        playerFile.setNumber(VOTES_MONTHLY, 0)
    }

    override fun clearWeeklyVotes()
    {
        playerFile.setNumber(VOTES_WEEKLY, 0)
    }

    override fun clearDailyVotes()
    {
        playerFile.setNumber(VOTES_DAILY, 0)
    }

    override fun addVote(): Boolean
    {
        val beforeVotes = votes
        addStreak()
        playerFile.setTimeStamp(VOTE_LAST)
        playerFile.addNumber(VOTES)
        playerFile.addNumber(VOTES_MONTHLY)
        playerFile.addNumber(VOTES_WEEKLY)
        playerFile.addNumber(VOTES_DAILY)

        Voter.getTopVoters(plugin, true)

        return beforeVotes < votes
    }

    override fun setPower(power: Boolean): Boolean
    {
        playerFile.set(POWER, power)
        return playerFile.saveConfig()
    }

    override fun clearQueue(): Boolean
    {
        return playerFile.delete(Data.VOTE_QUEUE.path)
    }

    override fun addQueue(site: String): Boolean
    {
        val path = Data.VOTE_QUEUE.path
        val queue = playerFile.getStringList(path)
        queue.add(site)
        playerFile[path] = queue
        return plugin.data.saveConfig()
    }

    override fun addStreak(): Boolean
    {
        val diff = last.dayDifferenceToday()
        if (diff == 1 || votes == 0)
        {
            return playerFile.addNumber(STREAK_DAILY, 1)
        } else if (diff > 1)
        {
            clearStreak()
        }
        return false
    }

    override fun clearStreak(): Boolean
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
        private const val VOTE_LAST = "last"
        private const val POWER = "power"

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
            val keyMigrations = mapOf(
                Pair("period", "monthly_votes"),
                Pair("monthly_votes", VOTES_MONTHLY),
                Pair("opUser", POWER)
            )

            for (playerFile in PlayerFile.getAll().values)
            {
                playerFile.keyMigrations(keyMigrations)
            }
        }
    }
}