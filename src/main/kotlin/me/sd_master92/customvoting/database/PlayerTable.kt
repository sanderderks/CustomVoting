package me.sd_master92.customvoting.database

import me.sd_master92.core.database.CustomColumn
import me.sd_master92.core.database.CustomDatabase
import me.sd_master92.core.database.CustomTable
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import java.sql.ResultSet

class PlayerTable(private val plugin: CV, database: CustomDatabase)
{
    val table: CustomTable
    private fun addPlayer(uuid: String): Boolean
    {
        return table.insertData(
            arrayOf("uuid", "votes", "last", "queue", "period"), arrayOf(uuid, 0, 0, 0, 0)
        )
    }

    val all: ResultSet
        get() = table.getAll()

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

    fun getPeriod(uuid: String): Int
    {
        val result = table.getData("uuid", uuid)
        try
        {
            if (result.next())
            {
                return result.getInt("period")
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

    fun setPeriod(uuid: String, votes: Int): Boolean
    {
        return table.updateData("uuid", uuid, "period", votes)
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

    private fun migrate()
    {
        if (!table.getColumn("period").exists())
        {
            if (!table.create("period", CustomColumn.DataType.INT))
            {
                plugin.errorLog("| could not create column 'period'!")
            }
        }
    }

    companion object
    {
        fun getTopVoters(plugin: CV): List<PlayerData>
        {
            val topVoters: MutableList<PlayerData> = ArrayList()
            try
            {
                val all = plugin.playerTable?.all
                if (all != null)
                {
                    while (all.next())
                    {
                        topVoters.add(
                            PlayerData(
                                all.getString("uuid"), all.getString("name"), all.getInt("votes"),
                                all.getLong("last"),
                                all.getInt("queue"),
                                all.getInt("period")
                            )
                        )
                    }
                }
            } catch (e: Exception)
            {
                return topVoters
            }

            if (plugin.config.getBoolean(Settings.MONTHLY_PERIOD))
            {
                topVoters.sortWith { x: PlayerData, y: PlayerData ->
                    var compare = y.period.compareTo(x.period)
                    if (compare == 0)
                    {
                        compare = x.last.compareTo(y.last)
                    }
                    compare
                }
            } else
            {
                topVoters.sortWith { x: PlayerData, y: PlayerData ->
                    var compare = y.votes.compareTo(x.votes)
                    if (compare == 0)
                    {
                        compare = x.last.compareTo(y.last)
                    }
                    compare
                }
            }
            return topVoters
        }

        fun getTopVoter(plugin: CV, _n: Int): PlayerData?
        {
            var n = _n
            n--
            val topVoters = getTopVoters(plugin)
            return if (n >= 0 && n < topVoters.size)
            {
                topVoters[n]
            } else null
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
                table.getColumn("period").create(CustomColumn.DataType.INT)
                plugin.infoLog("| successfully created table 'players'")
                plugin.infoLog("|")
                plugin.infoLog("|___successfully connected to database")
            }
        } else
        {
            plugin.infoLog("| successfully located table 'players'")
            plugin.infoLog("|")
            plugin.infoLog("|___successfully connected to database")
            migrate()
        }
    }
}