package me.sd_master92.customvoting.database;

import me.sd_master92.customvoting.constants.Voter;

public class PlayerData implements Voter
{
    private final String uuid;
    private final String name;
    private final int votes;
    private final long last;
    private final int queue;

    public PlayerData(String uuid, String name, int votes, long last, int queue)
    {
        this.uuid = uuid;
        this.name = name;
        this.votes = votes;
        this.last = last;
        this.queue = queue;
    }

    public String getName()
    {
        return name;
    }

    public int getQueue()
    {
        return queue;
    }

    public String getUuid()
    {
        return uuid;
    }

    public int getVotes()
    {
        return votes;
    }

    public long getLast()
    {
        return last;
    }
}
