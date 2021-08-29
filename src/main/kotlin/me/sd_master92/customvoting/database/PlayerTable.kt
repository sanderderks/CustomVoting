package me.sd_master92.customvoting.database

import me.sd_master92.customfile.database.CustomColumn
import me.sd_master92.customfile.database.CustomDatabase
import me.sd_master92.customfile.database.CustomTable
import me.sd_master92.customvoting.Main
import java.sql.ResultSet

class PlayerTable(private val plugin: Main, database: CustomDatabase)
{
    val table: CustomTable
    private fun addPlayer(uuid: String): Boolean
    {
        return table.insertData(arrayOf("uuid", "votes", "last", "queue"), arrayOf("\"" + uuid + "\"", "0",
                "0", "0"))
    }

    val all: ResultSet
        get() = table.all

    fun getUuid(name: String): String
    {
        val result = table.getData("name=\"$name\"")
        try
        {
            if (result.first())
            {
                return result.getString("uuid")
            }
        } catch (e: Exception)
        {
            plugin.error("Could not retrieve uuid of $name from database")
        }
        return "Unknown"
    }

    fun getName(uuid: String): String
    {
        val result = table.getData("uuid=\"$uuid\"")
        try
        {
            if (result.first())
            {
                return result.getString("name")
            }
        } catch (e: Exception)
        {
            plugin.error("Could not retrieve name of $uuid from database")
        }
        return "Unknown"
    }

    fun setName(uuid: String, name: String): Boolean
    {
        return table.updateData("uuid=\"$uuid\"", "name=\"$name\"")
    }

    fun getVotes(uuid: String): Int
    {
        val result = table.getData("uuid=\"$uuid\"")
        try
        {
            if (result.first())
            {
                return result.getInt("votes")
            } else
            {
                addPlayer(uuid)
            }
        } catch (e: Exception)
        {
            plugin.error("Could not retrieve votes of $uuid from database")
        }
        return 0
    }

    fun setVotes(uuid: String, votes: Int): Boolean
    {
        return table.updateData("uuid=\"$uuid\"", "votes=$votes")
    }

    fun getLast(uuid: String): Long
    {
        val result = table.getData("uuid=\"$uuid\"")
        try
        {
            if (result.first())
            {
                return result.getLong("last")
            } else
            {
                addPlayer(uuid)
            }
        } catch (e: Exception)
        {
            plugin.error("Could not retrieve last timestamp of $uuid from database")
        }
        return 0
    }

    fun setLast(uuid: String): Boolean
    {
        return table.updateData("uuid=\"$uuid\"", "last=" + System.currentTimeMillis())
    }

    fun getQueue(uuid: String): Int
    {
        val result = table.getData("uuid=\"$uuid\"")
        try
        {
            if (result.first())
            {
                return result.getInt("queue")
            } else
            {
                addPlayer(uuid)
            }
        } catch (e: Exception)
        {
            plugin.error("Could not retrieve queue of $uuid from database")
        }
        return 0
    }

    fun setQueue(uuid: String, queue: Int): Boolean
    {
        return table.updateData("uuid=\"$uuid\"", "queue=$queue")
    }

    companion object
    {
        fun getTopVoters(plugin: Main): List<PlayerData>
        {
            val topVoters: MutableList<PlayerData> = ArrayList()
            try
            {
                val all = plugin.playerTable?.all
                if(all != null)
                {
                    while (all.next())
                    {
                        topVoters.add(PlayerData(all.getString("uuid"), all.getString("name"), all.getInt("votes"),
                                all.getLong("last"),
                                all.getInt("queue")))
                    }
                }
            } catch (e: Exception)
            {
                return topVoters
            }
            topVoters.sortWith { x: PlayerData, y: PlayerData ->
                var compare = y.votes.compareTo(x.votes)
                if (compare == 0)
                {
                    compare = x.last.compareTo(y.last)
                }
                compare
            }
            return topVoters
        }

        fun getTopVoter(plugin: Main, _n: Int): PlayerData?
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
                plugin.print("| could not create table 'players'")
                plugin.print("|")
                plugin.print("|___database disabled")
            } else
            {
                table.getColumn("name").create(CustomColumn.DataType.VARCHAR)
                table.getColumn("votes").create(CustomColumn.DataType.INT)
                table.getColumn("last").create(CustomColumn.DataType.LONG)
                table.getColumn("queue").create(CustomColumn.DataType.INT)
                plugin.print("| successfully created table 'players'")
                plugin.print("|")
                plugin.print("|___successfully connected to database")
            }
        } else
        {
            plugin.print("| successfully located table 'players'")
            plugin.print("|")
            plugin.print("|___successfully connected to database")
        }
    }
}