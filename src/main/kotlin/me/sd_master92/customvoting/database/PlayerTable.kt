package me.sd_master92.customvoting.database

import me.sd_master92.core.database.CustomColumn
import me.sd_master92.core.database.CustomDatabase
import me.sd_master92.core.database.CustomTable
import me.sd_master92.customvoting.CV

class PlayerTable(private val plugin: CV, database: CustomDatabase)
{
    val table: CustomTable

    private fun addPlayer(uuid: String): Boolean
    {
        return table.insertData(
            arrayOf("uuid", "votes", "last", "queue", "monthly_votes", "is_op"),
            arrayOf(uuid, 0, 0, 0, 0, false)
        )
    }

    fun getUuid(name: String): String
    {
        val result = table.getData("name", name)
        try
        {
            if (result.next())
            {
                return result.getString("uuid")
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve uuid of $name from database", e)
        }
        return "Unknown"
    }

    fun getName(uuid: String): String
    {
        val result = table.getData("uuid", uuid)
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
        return table.updateData("uuid", uuid, "name", name)
    }

    fun getVotes(uuid: String): Int
    {
        val result = table.getData("uuid", uuid)
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
        return table.updateData("uuid", uuid, "votes", votes)
    }

    fun getMonthlyVotes(uuid: String): Int
    {
        val result = table.getData("uuid", uuid)
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
        return table.updateData("uuid", uuid, "monthly_votes", votes)
    }

    fun getLast(uuid: String): Long
    {
        val result = table.getData("uuid", uuid)
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
        return table.updateData("uuid", uuid, "last", System.currentTimeMillis())
    }

    fun getQueue(uuid: String): Int
    {
        val result = table.getData("uuid", uuid)
        try
        {
            if (result.next())
            {
                return result.getInt("queue")
            } else
            {
                addPlayer(uuid)
            }
        } catch (e: Exception)
        {
            plugin.errorLog("Could not retrieve queue of $uuid from database", e)
        }
        return 0
    }

    fun setQueue(uuid: String, queue: Int): Boolean
    {
        return table.updateData("uuid", uuid, "queue", queue)
    }

    fun getIsOp(uuid: String): Boolean
    {
        val result = table.getData("uuid", uuid)
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
        return table.updateData("uuid", uuid, "is_op", isOp)
    }

    private fun migrate()
    {
        val monthlyVotesColumn = table.getColumn("monthly_votes")
        if (!monthlyVotesColumn.exists())
        {
            val periodColumn = table.getColumn("period")
            if (periodColumn.exists())
            {
                val statement =
                    table.database.connection!!.prepareStatement("ALTER TABLE ${table.name} RENAME COLUMN period TO monthly_votes")
                table.database.query(statement)
            } else
            {
                if (monthlyVotesColumn.create(CustomColumn.DataType.INT))
                {
                    plugin.errorLog("| successfully added column 'monthly_votes'!")
                } else
                {
                    plugin.errorLog("| could not create column 'monthly_votes'!")
                }
            }
        }
        val isOpColumn = table.getColumn("is_op")
        if (!isOpColumn.exists())
        {
            if (isOpColumn.create(CustomColumn.DataType.BOOLEAN))
            {
                plugin.errorLog("| successfully added column 'is_op'!")
            } else
            {
                plugin.errorLog("| could not create column 'is_op'!")
            }
        }
    }

    init
    {
        table = database.getTable("players")
        if (!table.exists())
        {
            if (!table.create("uuid", CustomColumn.DataType.VARCHAR_PRIMARY))
            {
                plugin.errorLog("| could not create table 'players'")
                plugin.errorLog("|")
                plugin.errorLog("|___database disabled")
            } else
            {
                table.getColumn("name").create(CustomColumn.DataType.VARCHAR)
                table.getColumn("votes").create(CustomColumn.DataType.INT)
                table.getColumn("last").create(CustomColumn.DataType.LONG)
                table.getColumn("queue").create(CustomColumn.DataType.INT)
                table.getColumn("monthly_votes").create(CustomColumn.DataType.INT)
                plugin.infoLog("| successfully created table 'players'")
                plugin.infoLog("|")
                plugin.infoLog("|___successfully connected to database")
            }
        } else
        {
            plugin.infoLog("| successfully located table 'players'")
            plugin.infoLog("|")
            migrate()
            plugin.infoLog("|")
            plugin.infoLog("|___successfully connected to database")
        }
    }
}