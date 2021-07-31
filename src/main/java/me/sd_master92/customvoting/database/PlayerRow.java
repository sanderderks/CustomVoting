package me.sd_master92.customvoting.database;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Voter;
import me.sd_master92.customvoting.subjects.VoteTopSign;
import me.sd_master92.customvoting.subjects.VoteTopStand;
import org.bukkit.entity.Player;

public class PlayerRow implements Voter
{
    private final Main plugin;
    private final PlayerTable players;
    private final String uuid;

    public PlayerRow(Main plugin, String uuid)
    {
        this.plugin = plugin;
        players = plugin.getPlayerTable();
        this.uuid = uuid;
        register();
    }

    public PlayerRow(Main plugin, Player player)
    {
        this(plugin, player.getUniqueId().toString());
        players.setName(player.getUniqueId().toString(), player.getName());
    }

    private void register()
    {
        players.getVotes(uuid);
    }

    public String getName()
    {
        return players.getName(uuid);
    }

    public int getVotes()
    {
        return players.getVotes(uuid);
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setVotes(int n, boolean update)
    {
        players.setVotes(uuid, n);
        players.setLast(uuid);
        if (update)
        {
            VoteTopSign.updateAll(plugin);
            VoteTopStand.updateAll(plugin);
        }
    }

    public void addVote(boolean update)
    {
        players.setVotes(uuid, getVotes() + 1);
        players.setLast(uuid);
        if (update)
        {
            VoteTopSign.updateAll(plugin);
            VoteTopStand.updateAll(plugin);
        }
    }

    public int getQueue()
    {
        return players.getQueue(uuid);
    }

    public boolean clearQueue()
    {
        return players.setQueue(uuid, 0);
    }

    public boolean addQueue()
    {
        return players.setQueue(uuid, getQueue() + 1);
    }
}