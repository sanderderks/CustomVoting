package me.sd_master92.customvoting.database;

import me.sd_master92.customfile.database.CustomColumn;
import me.sd_master92.customfile.database.CustomDatabase;
import me.sd_master92.customfile.database.CustomTable;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.extensions.CustomPlaceholders;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlayerTable
{
    private final Main plugin;
    private final CustomTable players;

    public PlayerTable(Main plugin, CustomDatabase database)
    {
        this.plugin = plugin;
        players = database.getTable("players");
        if (!players.exists())
        {
            if (!players.create("uuid", CustomColumn.DataType.VARCHAR_PRIMARY))
            {
                plugin.print("| could not create table 'players'");
                plugin.print("|");
                plugin.print("|___database disabled");
                return;
            } else
            {
                players.getColumn("name").create(CustomColumn.DataType.VARCHAR);
                players.getColumn("votes").create(CustomColumn.DataType.INT);
                players.getColumn("last").create(CustomColumn.DataType.LONG);
                players.getColumn("queue").create(CustomColumn.DataType.INT);
                plugin.print("| successfully created table 'players'");
            }
        } else
        {
            plugin.print("| successfully located table 'players'");
        }
        plugin.print("|");
        plugin.print("|___successfully connected to database");
    }

    public static List<PlayerData> getTopVoters(Main plugin)
    {
        List<PlayerData> topVoters = new ArrayList<>();
        try
        {
            ResultSet all = plugin.getPlayerTable().getAll();
            while (all.next())
            {
                topVoters.add(new PlayerData(all.getString("uuid"), all.getString("name"), all.getInt("votes"),
                        all.getLong("last"),
                        all.getInt("queue")));
            }
        } catch (Exception e)
        {
            return topVoters;
        }

        topVoters.sort((x, y) ->
        {
            int compare = Integer.compare(y.getVotes(), x.getVotes());
            if (compare == 0)
            {
                compare = Long.compare(x.getLast(), y.getLast());
            }
            return compare;
        });

        if (plugin.usePlaceholders())
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    for (int i = 0; i < topVoters.size(); i++)
                    {
                        CustomPlaceholders.setPlayerVotes(i + 1);
                    }
                }
            }.runTask(plugin);
        }

        return topVoters;
    }

    public static PlayerData getTopVoter(Main plugin, int n)
    {
        n--;
        List<PlayerData> topVoters = getTopVoters(plugin);
        if (n >= 0 && n < topVoters.size())
        {
            return topVoters.get(n);
        }
        return null;
    }

    public CustomTable getTable()
    {
        return players;
    }

    public boolean addPlayer(String uuid)
    {
        return players.insertData(new String[]{"uuid", "votes", "last", "queue"}, new String[]{"\"" + uuid + "\"", "0",
                "0", "0"});
    }

    public ResultSet getAll()
    {
        return players.getAll();
    }

    public String getUuid(String name)
    {
        ResultSet result = players.getData("name=\"" + name + "\"");
        try
        {
            if (result.first())
            {
                return result.getString("uuid");
            }
        } catch (Exception e)
        {
            plugin.error("Could not retrieve uuid of " + name + " from database");
        }
        return "Unknown";
    }

    public String getName(String uuid)
    {
        ResultSet result = players.getData("uuid=\"" + uuid + "\"");
        try
        {
            if (result.first())
            {
                return result.getString("name");
            }
        } catch (Exception e)
        {
            plugin.error("Could not retrieve name of " + uuid + " from database");
        }
        return "Unknown";
    }

    public boolean setName(String uuid, String name)
    {
        return players.updateData("uuid=\"" + uuid + "\"", "name=\"" + name + "\"");
    }

    public int getVotes(String uuid)
    {
        ResultSet result = players.getData("uuid=\"" + uuid + "\"");
        try
        {
            if (result.first())
            {
                return result.getInt("votes");
            } else
            {
                addPlayer(uuid);
            }
        } catch (Exception e)
        {
            plugin.error("Could not retrieve votes of " + uuid + " from database");
        }
        return 0;
    }

    public boolean setVotes(String uuid, int votes)
    {
        return players.updateData("uuid=\"" + uuid + "\"", "votes=" + votes);
    }

    public long getLast(String uuid)
    {
        ResultSet result = players.getData("uuid=\"" + uuid + "\"");
        try
        {
            if (result.first())
            {
                return result.getLong("last");
            } else
            {
                addPlayer(uuid);
            }
        } catch (Exception e)
        {
            plugin.error("Could not retrieve last timestamp of " + uuid + " from database");
        }
        return 0;
    }

    public boolean setLast(String uuid)
    {
        return players.updateData("uuid=\"" + uuid + "\"", "last=" + System.currentTimeMillis());
    }

    public int getQueue(String uuid)
    {
        ResultSet result = players.getData("uuid=\"" + uuid + "\"");
        try
        {
            if (result.first())
            {
                return result.getInt("queue");
            } else
            {
                addPlayer(uuid);
            }
        } catch (Exception e)
        {
            plugin.error("Could not retrieve queue of " + uuid + " from database");
        }
        return 0;
    }

    public boolean setQueue(String uuid, int queue)
    {
        return players.updateData("uuid=\"" + uuid + "\"", "queue=" + queue);
    }
}
