package me.sd_master92.customvoting;

import me.sd_master92.customfile.PlayerFile;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class VoteFile extends PlayerFile
{
    public VoteFile(String uuid, Plugin plugin)
    {
        super(uuid, plugin);
        register();
    }

    public VoteFile(Player player, Plugin plugin)
    {
        super(player, plugin);
        register();
    }

    public boolean register()
    {
        if (getVotes() == 0)
        {
            return setVotes(0);
        }
        return false;
    }

    public int getVotes()
    {
        return getNumber("votes");
    }

    public boolean setVotes(int n)
    {
        return setNumber("votes", n);
    }

    public boolean addVote()
    {
        return addNumber("votes", 1);
    }
}
