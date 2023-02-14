package me.sd_master92.customvoting.database

import me.sd_master92.core.database.CustomColumn
import me.sd_master92.core.database.CustomDatabase
import me.sd_master92.core.database.CustomTable
import me.sd_master92.core.errorLog
import me.sd_master92.core.infoLog
import me.sd_master92.customvoting.CV

class PlayerDatabase(private val plugin: CV, database: CustomDatabase)
{
    private val queueTable: CustomTable
    val playersTable: CustomTable

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

    fun getLast(uuid: String): Long
    {
        val result = playersTable.getData(PlayerTableColumn.UUID.columnName, uuid)
        try
        {
            if (result.next())
            {
                return result.getLong(PlayerTableColumn.LAST_VOTE.columnName)
            } else
            {
                addPlayer(uuid)
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve last timestamp of $uuid from database", e)
        }
        return 0
    }

    fun setLast(uuid: String): Boolean
    {
        return playersTable.updateData(
            PlayerTableColumn.UUID.columnName,
            uuid,
            PlayerTableColumn.LAST_VOTE.columnName,
            System.currentTimeMillis()
        )
    }

    fun getQueue(uuid: String): List<String>
    {
        val result = queueTable.getData(QueueTableColumn.UUID.columnName, uuid)
        val queue = mutableListOf<String>()
        try
        {
            while (result.next())
            {
                queue.add(result.getString(QueueTableColumn.SITE.columnName))
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve queue of $uuid from database", e)
        }
        return queue
    }

    fun addQueue(uuid: String, site: String): Boolean
    {
        return queueTable.insertData(
            arrayOf(QueueTableColumn.UUID.columnName, QueueTableColumn.SITE.columnName),
            arrayOf(uuid, site)
        )
    }

    fun clearQueue(uuid: String): Boolean
    {
        return queueTable.deleteData(QueueTableColumn.UUID.columnName, uuid)
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
        rename("period", PlayerTableColumn.MONTHLY_VOTES.columnName, CustomColumn.DataType.INT)
        rename("monthly_votes", PlayerTableColumn.MONTHLY_VOTES.columnName, CustomColumn.DataType.INT)
        rename("is_op", PlayerTableColumn.POWER.columnName, CustomColumn.DataType.BOOLEAN)
        for (column in PlayerTableColumn.columns())
        {
            create(column.columnName, column.dataType)
        }
        delete("queue")
    }

    private fun create(name: String, dataType: CustomColumn.DataType)
    {
        if (playersTable.getColumn(name).createIFNotExists(dataType))
        {
            plugin.infoLog("| successfully added column '$name'!")
        } else
        {
            plugin.errorLog("| could not create column '$name'!")
        }
    }

    private fun rename(oldName: String, newName: String, dataType: CustomColumn.DataType)
    {
        if (!playersTable.getColumn(newName).exists())
        {
            val column = playersTable.getColumn(oldName)
            if (column.exists())
            {
                if (column.renameOrCreate(newName, dataType))
                {
                    plugin.infoLog("| successfully added column '$newName'!")
                } else
                {
                    plugin.errorLog("| could not create column '$newName'!")
                }
            }
        }
    }

    private fun delete(name: String)
    {
        if (playersTable.getColumn(name).delete())
        {
            plugin.infoLog("| successfully deleted column '$name'!")
        } else
        {
            plugin.errorLog("| could not delete column '$name'!")
        }
    }

    companion object
    {
        const val PLAYERS_TABLE = "players"
        const val QUEUE_TABLE = "queue"
    }

    init
    {
        playersTable = database.getTable(PLAYERS_TABLE)
        if (!playersTable.exists())
        {
            PlayerTableColumn.create(plugin, playersTable)
        } else
        {
            plugin.infoLog("| successfully located table '$PLAYERS_TABLE'")
            plugin.infoLog("|")
            migratePlayers()
            PlayerTable.init(plugin)
        }
        queueTable = database.getTable(QUEUE_TABLE)
        if (!queueTable.exists())
        {
            QueueTableColumn.create(plugin, queueTable)
        } else
        {
            plugin.infoLog("| successfully located table '$QUEUE_TABLE'")
            plugin.infoLog("|")
        }
        if (playersTable.exists() && queueTable.exists())
        {
            plugin.infoLog("|___successfully connected to database")
        } else
        {
            plugin.errorLog("|___database disabled")
        }
    }
}