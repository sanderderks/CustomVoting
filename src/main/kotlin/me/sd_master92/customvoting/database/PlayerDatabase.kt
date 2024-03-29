package me.sd_master92.customvoting.database

import com.github.shynixn.mccoroutine.bukkit.launch
import me.sd_master92.core.database.CustomColumn
import me.sd_master92.core.database.CustomDatabase
import me.sd_master92.core.database.CustomTable
import me.sd_master92.core.errorLog
import me.sd_master92.core.infoLog
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.models.VoteHistory
import me.sd_master92.customvoting.constants.models.VoteSiteUUID
import java.util.*

class PlayerDatabase(private val plugin: CV, private val database: CustomDatabase)
{
    val playersTable: CustomTable = database.getTable(PLAYERS_TABLE)
    private val historyTable: CustomTable = database.getTable(HISTORY_TABLE)

    private suspend fun addPlayer(uuid: UUID): Boolean
    {
        val defaultData = mutableMapOf<String, Any>()
        for (column in PlayerTableColumn.columns(uuid))
        {
            defaultData[column.columnName] = column.defaultValue!!
        }
        return playersTable.insertDataAsync(defaultData.keys.toTypedArray(), defaultData.values.toTypedArray())
    }

    suspend fun getName(uuid: UUID): String
    {
        val result = playersTable.getDataAsync(PlayerTableColumn.UUID.columnName, uuid)
        try
        {
            if (result != null)
            {
                if (result.next())
                {
                    return result.getString(PlayerTableColumn.NAME.columnName)
                }
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve name of $uuid from database", e)
        }
        return "Unknown"
    }

    suspend fun setName(uuid: UUID, name: String): Boolean
    {
        return playersTable.updateDataAsync(PlayerTableColumn.UUID.columnName, uuid, PlayerTableColumn.NAME.columnName, name)
    }

    suspend fun getVotes(uuid: UUID): Int
    {
        val result = playersTable.getDataAsync(PlayerTableColumn.UUID.columnName, uuid)
        try
        {
            if (result != null)
            {
                if (result.next())
                {
                    return result.getInt(PlayerTableColumn.VOTES.columnName)
                } else
                {
                    addPlayer(uuid)
                }
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve votes of $uuid from database", e)
        }
        return 0
    }

    suspend fun setVotes(uuid: UUID, votes: Int): Boolean
    {
        return playersTable.updateDataAsync(
            PlayerTableColumn.UUID.columnName,
            uuid,
            PlayerTableColumn.VOTES.columnName,
            votes
        )
    }

    suspend fun getMonthlyVotes(uuid: UUID): Int
    {
        val result = playersTable.getDataAsync(PlayerTableColumn.UUID.columnName, uuid)
        try
        {
            if (result != null)
            {
                if (result.next())
                {
                    return result.getInt(PlayerTableColumn.MONTHLY_VOTES.columnName)
                } else
                {
                    addPlayer(uuid)
                }
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve period votes of $uuid from database", e)
        }
        return 0
    }

    suspend fun setMonthlyVotes(uuid: UUID, votes: Int): Boolean
    {
        return playersTable.updateDataAsync(
            PlayerTableColumn.UUID.columnName,
            uuid,
            PlayerTableColumn.MONTHLY_VOTES.columnName,
            votes
        )
    }

    suspend fun getWeeklyVotes(uuid: UUID): Int
    {
        val result = playersTable.getDataAsync(PlayerTableColumn.UUID.columnName, uuid)
        try
        {
            if (result != null)
            {
                if (result.next())
                {
                    return result.getInt(PlayerTableColumn.WEEKLY_VOTES.columnName)
                }
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve weekly votes of $uuid from database", e)
        }
        return 0
    }

    suspend fun setWeeklyVotes(uuid: UUID, votes: Int): Boolean
    {
        return playersTable.updateDataAsync(
            PlayerTableColumn.UUID.columnName,
            uuid,
            PlayerTableColumn.WEEKLY_VOTES.columnName,
            votes
        )
    }

    suspend fun getDailyVotes(uuid: UUID): Int
    {
        val result = playersTable.getDataAsync(PlayerTableColumn.UUID.columnName, uuid)
        try
        {
            if (result != null)
            {
                if (result.next())
                {
                    return result.getInt(PlayerTableColumn.DAILY_VOTES.columnName)
                }
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve daily votes of $uuid from database", e)
        }
        return 0
    }

    suspend fun setDailyVotes(uuid: UUID, votes: Int): Boolean
    {
        return playersTable.updateDataAsync(
            PlayerTableColumn.UUID.columnName,
            uuid,
            PlayerTableColumn.DAILY_VOTES.columnName,
            votes
        )
    }

    suspend fun getHistory(uuid: UUID): List<VoteHistory>
    {
        val result = historyTable.getDataAsync(HistoryTableColumn.UUID.columnName, uuid)
        val voteHistory = mutableListOf<VoteHistory>()
        try
        {
            if (result != null)
            {
                while (result.next())
                {
                    val id = result.getInt(HistoryTableColumn.ID.columnName)
                    val site = result.getString(HistoryTableColumn.SITE.columnName)
                    val time = result.getLong(HistoryTableColumn.TIME.columnName)
                    val queued = result.getBoolean(HistoryTableColumn.QUEUED.columnName)

                    val vote = VoteHistory(id, uuid, VoteSiteUUID(site), time, queued)
                    voteHistory.add(vote)
                }
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve history of $uuid from database", e)
        }
        return voteHistory
    }

    suspend fun addHistory(uuid: UUID, site: VoteSiteUUID, queued: Boolean, time: Long? = null): Boolean
    {
        return historyTable.insertDataAsync(
            arrayOf(
                HistoryTableColumn.UUID.columnName,
                HistoryTableColumn.SITE.columnName,
                HistoryTableColumn.TIME.columnName,
                HistoryTableColumn.QUEUED.columnName
            ),
            arrayOf(uuid, site.toString(), time ?: Date().time, if (queued) 1 else 0)
        )
    }

    suspend fun clearQueue(uuid: UUID): Boolean
    {
        return historyTable.updateDataAsync(
            HistoryTableColumn.UUID.columnName,
            uuid,
            HistoryTableColumn.QUEUED.columnName,
            0
        )
    }

    suspend fun getPower(uuid: UUID): Boolean
    {
        val result = playersTable.getDataAsync(PlayerTableColumn.UUID.columnName, uuid)
        try
        {
            if (result != null)
            {
                if (result.next())
                {
                    return result.getBoolean(PlayerTableColumn.POWER.columnName)
                }
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve op status of $uuid from database", e)
        }
        return false
    }

    suspend fun setPower(uuid: UUID, power: Boolean): Boolean
    {
        return playersTable.updateDataAsync(
            PlayerTableColumn.UUID.columnName,
            uuid,
            PlayerTableColumn.POWER.columnName,
            if (power) 1 else 0
        )
    }

    suspend fun getStreak(uuid: UUID): Int
    {
        val result = playersTable.getDataAsync(PlayerTableColumn.UUID.columnName, uuid)
        try
        {
            if (result != null)
            {
                if (result.next())
                {
                    return result.getInt(PlayerTableColumn.DAILY_VOTE_STREAK.columnName)
                }
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve streak of $uuid from database", e)
        }
        return 0
    }

    suspend  fun setStreak(uuid: UUID, streak: Int): Boolean
    {
        return playersTable.updateDataAsync(
            PlayerTableColumn.UUID.columnName,
            uuid,
            PlayerTableColumn.DAILY_VOTE_STREAK.columnName,
            streak
        )
    }

    private suspend fun migratePlayers()
    {
        rename(playersTable, "period", PlayerTableColumn.MONTHLY_VOTES.columnName, CustomColumn.DataType.INT)
        rename(playersTable, "monthly_votes", PlayerTableColumn.MONTHLY_VOTES.columnName, CustomColumn.DataType.INT)
        rename(playersTable, "is_op", PlayerTableColumn.POWER.columnName, CustomColumn.DataType.BOOLEAN)
        for (column in PlayerTableColumn.columns())
        {
            create(playersTable, column.columnName, column.dataType)
        }
        delete(playersTable, "queue")
        delete(playersTable, "last")
        delete(playersTable, "last_site")
    }

    private suspend  fun migrateQueue()
    {
        val queueTable = database.getTable("queue")
        if (queueTable.existsAsync())
        {
            val result = queueTable.getAllAsync()
            try
            {
                if (result != null)
                {
                    while (result.next())
                    {
                        val uuid = UUID.fromString(result.getString("uuid"))
                        val site = VoteSiteUUID(result.getString("site"))
                        val time = result.getLong("timestamp")
                        addHistory(uuid, site, true, time)
                    }
                }
                queueTable.deleteAsync(queueTable.name)
            } catch (e: Exception)
            {
                plugin.errorLog("Could not retrieve queue from database", e)
            }
        }
    }

    private fun migrateHistory()
    {
    }

    private suspend fun create(table: CustomTable, name: String, dataType: CustomColumn.DataType)
    {
        val column = table.getColumn(name)
        if (!column.exists())
        {
            if (column.create(dataType))
            {
                plugin.infoLog("| successfully created column '$name'!")
            } else
            {
                plugin.errorLog("| could not create column '$name'!")
            }
        }
    }

    private suspend fun rename(table: CustomTable, oldName: String, newName: String, dataType: CustomColumn.DataType)
    {
        if (!table.getColumn(newName).exists())
        {
            val column = table.getColumn(oldName)
            if (column.exists())
            {
                if (column.renameOrCreate(newName, dataType))
                {
                    plugin.infoLog("| successfully created column '$newName'!")
                } else
                {
                    plugin.errorLog("| could not create column '$newName'!")
                }
            }
        }
    }

    private suspend fun delete(table: CustomTable, name: String)
    {
        val column = table.getColumn(name)
        if (column.exists())
        {
            if (table.getColumn(name).delete())
            {
                plugin.infoLog("| successfully deleted column '$name'!")
            } else
            {
                plugin.errorLog("| could not delete column '$name'!")
            }
        }
    }

    companion object
    {
        const val PLAYERS_TABLE = "players"
        const val HISTORY_TABLE = "history"
    }

    init
    {
        plugin.launch {
            if (!playersTable.existsAsync())
            {
                PlayerTableColumn.create(plugin, playersTable)
            } else
            {
                plugin.infoLog("| successfully located table '$PLAYERS_TABLE'")
                plugin.infoLog("|")
                migratePlayers()
            }
            if (!historyTable.existsAsync())
            {
                HistoryTableColumn.create(plugin, historyTable)
            } else
            {
                plugin.infoLog("| successfully located table '$HISTORY_TABLE'")
                plugin.infoLog("|")
                migrateHistory()
                migrateQueue()
            }
            if (playersTable.existsAsync() && historyTable.existsAsync())
            {
                plugin.infoLog("|___successfully connected to database")
            } else
            {
                plugin.errorLog("|___database disabled")
            }
        }
    }
}