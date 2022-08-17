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
        return playersTable.insertData(
            arrayOf("uuid", "votes", "last", "monthly_votes", "is_op"),
            arrayOf(uuid, 0, 0, 0, 0)
        )
    }

    fun getName(uuid: String): String
    {
        val result = playersTable.getData("uuid", uuid)
        try
        {
            if (result.next())
            {
                return result.getString("name")
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve name of $uuid from database", e)
        }
        return "Unknown"
    }

    fun setName(uuid: String, name: String): Boolean
    {
        return playersTable.updateData("uuid", uuid, "name", name)
    }

    fun getVotes(uuid: String): Int
    {
        val result = playersTable.getData("uuid", uuid)
        try
        {
            if (result.next())
            {
                return result.getInt("votes")
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
        return playersTable.updateData("uuid", uuid, "votes", votes)
    }

    fun getMonthlyVotes(uuid: String): Int
    {
        val result = playersTable.getData("uuid", uuid)
        try
        {
            if (result.next())
            {
                return result.getInt("monthly_votes")
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
        return playersTable.updateData("uuid", uuid, "monthly_votes", votes)
    }

    fun getLast(uuid: String): Long
    {
        val result = playersTable.getData("uuid", uuid)
        try
        {
            if (result.next())
            {
                return result.getLong("last")
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
        return playersTable.updateData("uuid", uuid, "last", System.currentTimeMillis())
    }

    fun getQueue(uuid: String): List<String>
    {
        val result = queueTable.getData("uuid", uuid)
        val queue = mutableListOf<String>()
        try
        {
            while (result.next())
            {
                queue.add(result.getString("site"))
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve queue of $uuid from database", e)
        }
        return queue
    }

    fun addQueue(uuid: String, site: String): Boolean
    {
        return queueTable.insertData(arrayOf("uuid", "site"), arrayOf(uuid, site))
    }

    fun clearQueue(uuid: String): Boolean
    {
        return queueTable.deleteData("uuid", uuid)
    }

    fun getIsOp(uuid: String): Boolean
    {
        val result = playersTable.getData("uuid", uuid)
        try
        {
            if (result.next())
            {
                return result.getBoolean("is_op")
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve op status of $uuid from database", e)
        }
        return false
    }

    fun setIsOp(uuid: String, isOp: Boolean): Boolean
    {
        return playersTable.updateData("uuid", uuid, "is_op", if (isOp) 1 else 0)
    }

    private fun migratePlayers()
    {
        val monthlyVotesColumn = playersTable.getColumn("monthly_votes")
        if (!monthlyVotesColumn.exists())
        {
            val periodColumn = playersTable.getColumn("period")
            if (periodColumn.exists())
            {
                val statement =
                    playersTable.database.connection!!.prepareStatement("ALTER TABLE ${playersTable.name} CHANGE period monthly_votes ${CustomColumn.DataType.INT}")
                playersTable.database.execute(statement)
            } else
            {
                if (monthlyVotesColumn.create(CustomColumn.DataType.INT))
                {
                    plugin.infoLog("| successfully added column 'monthly_votes'!")
                } else
                {
                    plugin.errorLog("| could not create column 'monthly_votes'!")
                }
            }
        }
        val isOpColumn = playersTable.getColumn("is_op")
        if (!isOpColumn.exists())
        {
            if (isOpColumn.create(CustomColumn.DataType.BOOLEAN))
            {
                plugin.infoLog("| successfully added column 'is_op'!")
            } else
            {
                plugin.errorLog("| could not create column 'is_op'!")
            }
        }
    }

    init
    {
        playersTable = database.getTable("players")
        if (!playersTable.exists())
        {
            if (!playersTable.create("uuid", CustomColumn.DataType.VARCHAR_PRIMARY))
            {
                plugin.errorLog("| could not create table 'players'")
                plugin.errorLog("|")
            } else
            {
                playersTable.getColumn("name").create(CustomColumn.DataType.VARCHAR)
                playersTable.getColumn("votes").create(CustomColumn.DataType.INT)
                playersTable.getColumn("last").create(CustomColumn.DataType.LONG)
                playersTable.getColumn("monthly_votes").create(CustomColumn.DataType.INT)
                plugin.infoLog("| successfully created table 'players'")
                plugin.infoLog("|")
                PlayerTable.init(plugin)
            }
        } else
        {
            plugin.infoLog("| successfully located table 'players'")
            plugin.infoLog("|")
            migratePlayers()
            PlayerTable.init(plugin)
        }
        queueTable = database.getTable("queue")
        if (!queueTable.exists())
        {
            if (!queueTable.create("id", CustomColumn.DataType.INT_PRIMARY))
            {
                plugin.errorLog("| could not create table 'queue'")
                plugin.errorLog("|")
            } else
            {
                queueTable.getColumn("uuid").create(CustomColumn.DataType.VARCHAR)
                queueTable.getColumn("site").create(CustomColumn.DataType.VARCHAR)
                plugin.infoLog("| successfully created table 'queue'")
                plugin.infoLog("|")
            }
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