package me.sd_master92.customvoting.database

import me.sd_master92.core.database.CustomColumn
import me.sd_master92.core.database.CustomDatabase
import me.sd_master92.core.database.CustomTable
import me.sd_master92.core.errorLog
import me.sd_master92.core.infoLog
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.models.VoteHistory
import java.util.*

class PlayerDatabase(private val plugin: CV, private val database: CustomDatabase)
{
    val playersTable: CustomTable = database.getTable(PLAYERS_TABLE)
    private val historyTable: CustomTable = database.getTable(HISTORY_TABLE)

    private fun addPlayer(uuid: String): Boolean
    {
        val defaultData = mutableMapOf<String, Any>()
        for (column in PlayerTableColumn.columns(uuid))
        {
            defaultData[column.columnName] = column.defaultValue!!
        }
        return playersTable.insertData(defaultData.keys.toTypedArray(), defaultData.values.toTypedArray())
    }

    fun getName(uuid: String): String
    {
        val result = playersTable.getData(PlayerTableColumn.UUID.columnName, uuid)
        try
        {
            if (result.next())
            {
                return result.getString(PlayerTableColumn.NAME.columnName)
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve name of $uuid from database", e)
        }
        return "Unknown"
    }

    fun setName(uuid: String, name: String): Boolean
    {
        return playersTable.updateData(PlayerTableColumn.UUID.columnName, uuid, PlayerTableColumn.NAME.columnName, name)
    }

    fun getVotes(uuid: String): Int
    {
        val result = playersTable.getData(PlayerTableColumn.UUID.columnName, uuid)
        try
        {
            if (result.next())
            {
                return result.getInt(PlayerTableColumn.VOTES.columnName)
            } else
            {
                addPlayer(uuid)
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve votes of $uuid from database", e)
        }
        return 0
    }

    fun setVotes(uuid: String, votes: Int): Boolean
    {
        return playersTable.updateData(
            PlayerTableColumn.UUID.columnName,
            uuid,
            PlayerTableColumn.VOTES.columnName,
            votes
        )
    }

    fun getMonthlyVotes(uuid: String): Int
    {
        val result = playersTable.getData(PlayerTableColumn.UUID.columnName, uuid)
        try
        {
            if (result.next())
            {
                return result.getInt(PlayerTableColumn.MONTHLY_VOTES.columnName)
            } else
            {
                addPlayer(uuid)
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve period votes of $uuid from database", e)
        }
        return 0
    }

    fun setMonthlyVotes(uuid: String, votes: Int): Boolean
    {
        return playersTable.updateData(
            PlayerTableColumn.UUID.columnName,
            uuid,
            PlayerTableColumn.MONTHLY_VOTES.columnName,
            votes
        )
    }

    fun getWeeklyVotes(uuid: String): Int
    {
        val result = playersTable.getData(PlayerTableColumn.UUID.columnName, uuid)
        try
        {
            if (result.next())
            {
                return result.getInt(PlayerTableColumn.WEEKLY_VOTES.columnName)
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve weekly votes of $uuid from database", e)
        }
        return 0
    }

    fun setWeeklyVotes(uuid: String, votes: Int): Boolean
    {
        return playersTable.updateData(
            PlayerTableColumn.UUID.columnName,
            uuid,
            PlayerTableColumn.WEEKLY_VOTES.columnName,
            votes
        )
    }

    fun getDailyVotes(uuid: String): Int
    {
        val result = playersTable.getData(PlayerTableColumn.UUID.columnName, uuid)
        try
        {
            if (result.next())
            {
                return result.getInt(PlayerTableColumn.DAILY_VOTES.columnName)
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve daily votes of $uuid from database", e)
        }
        return 0
    }

    fun setDailyVotes(uuid: String, votes: Int): Boolean
    {
        return playersTable.updateData(
            PlayerTableColumn.UUID.columnName,
            uuid,
            PlayerTableColumn.DAILY_VOTES.columnName,
            votes
        )
    }

    fun getHistory(uuid: String): List<VoteHistory>
    {
        val result = historyTable.getData(HistoryTableColumn.UUID.columnName, uuid)
        val voteHistory = mutableListOf<VoteHistory>()
        try
        {
            while (result.next())
            {
                val id = result.getInt(HistoryTableColumn.ID.columnName)
                val site = result.getString(HistoryTableColumn.SITE.columnName)
                val time = result.getLong(HistoryTableColumn.TIME.columnName)
                val queued = result.getBoolean(HistoryTableColumn.QUEUED.columnName)

                val vote = VoteHistory(id, uuid, site, time, queued)
                voteHistory.add(vote)
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve history of $uuid from database", e)
        }
        return voteHistory
    }

    fun addHistory(uuid: String, site: String, queued: Boolean, time: Long? = null): Boolean
    {
        return historyTable.insertData(
            arrayOf(
                HistoryTableColumn.UUID.columnName,
                HistoryTableColumn.SITE.columnName,
                HistoryTableColumn.TIME.columnName,
                HistoryTableColumn.QUEUED.columnName
            ),
            arrayOf(uuid, site, time ?: Date().time, if (queued) 1 else 0)
        )
    }

    fun clearQueue(uuid: String): Boolean
    {
        return historyTable.updateData(
            HistoryTableColumn.UUID.columnName,
            uuid,
            HistoryTableColumn.QUEUED.columnName,
            0
        )
    }

    fun getPower(uuid: String): Boolean
    {
        val result = playersTable.getData(PlayerTableColumn.UUID.columnName, uuid)
        try
        {
            if (result.next())
            {
                return result.getBoolean(PlayerTableColumn.POWER.columnName)
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve op status of $uuid from database", e)
        }
        return false
    }

    fun setPower(uuid: String, power: Boolean): Boolean
    {
        return playersTable.updateData(
            PlayerTableColumn.UUID.columnName,
            uuid,
            PlayerTableColumn.POWER.columnName,
            if (power) 1 else 0
        )
    }

    fun getStreak(uuid: String): Int
    {
        val result = playersTable.getData(PlayerTableColumn.UUID.columnName, uuid)
        try
        {
            if (result.next())
            {
                return result.getInt(PlayerTableColumn.DAILY_VOTE_STREAK.columnName)
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve streak of $uuid from database", e)
        }
        return 0
    }

    fun setStreak(uuid: String, streak: Int): Boolean
    {
        return playersTable.updateData(
            PlayerTableColumn.UUID.columnName,
            uuid,
            PlayerTableColumn.DAILY_VOTE_STREAK.columnName,
            streak
        )
    }

    private fun migratePlayers()
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

    private fun migrateQueue()
    {
        val queueTable = database.getTable("queue")
        if (queueTable.exists())
        {
            val result = queueTable.getAll()
            try
            {
                while (result.next())
                {
                    val uuid = result.getString("uuid")
                    val site = result.getString("site")
                    val time = result.getLong("timestamp")
                    addHistory(uuid, site, true, time)
                }
                queueTable.delete(queueTable.name)
            } catch (e: Exception)
            {
                plugin.errorLog("Could not retrieve queue from database", e)
            }
        }
    }

    private fun migrateHistory()
    {
    }

    private fun create(table: CustomTable, name: String, dataType: CustomColumn.DataType)
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

    private fun rename(table: CustomTable, oldName: String, newName: String, dataType: CustomColumn.DataType)
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

    private fun delete(table: CustomTable, name: String)
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
        if (!playersTable.exists())
        {
            PlayerTableColumn.create(plugin, playersTable)
        } else
        {
            plugin.infoLog("| successfully located table '$PLAYERS_TABLE'")
            plugin.infoLog("|")
            migratePlayers()
        }
        if (!historyTable.exists())
        {
            HistoryTableColumn.create(plugin, historyTable)
        } else
        {
            plugin.infoLog("| successfully located table '$HISTORY_TABLE'")
            plugin.infoLog("|")
            migrateHistory()
            migrateQueue()
        }
        if (playersTable.exists() && historyTable.exists())
        {
            plugin.infoLog("|___successfully connected to database")
        } else
        {
            plugin.errorLog("|___database disabled")
        }
    }
}