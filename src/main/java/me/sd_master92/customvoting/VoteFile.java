package me.sd_master92.customvoting;

import me.sd_master92.customfile.PlayerFile;
import org.bukkit.entity.Player;

public class VoteFile extends PlayerFile
{
    private Main plugin;

    public VoteFile(String uuid, Main plugin)
    {
        super(uuid, plugin);
        this.plugin = plugin;
        register();
    }

    public VoteFile(Player player, Main plugin)
    {
        super(player, plugin);
        this.plugin = plugin;
        register();
    }

    private void register()
    {
        if (getVotes() == 0)
        {
            setVotes(0, false);
        }
        setTimeStamp("last");
    }

    public int getVotes()
    {
        return getNumber("votes");
    }

    public void setVotes(int n, boolean updateSigns)
    {
        setTimeStamp("last");
        setNumber("votes", n);
        if(updateSigns)
        {
            API.updateSigns(plugin);
        }
    }

    public void addVote(boolean updateSigns)
    {
        setTimeStamp("last");
        addNumber("votes", 1);
        if(updateSigns)
        {
            API.updateSigns(plugin);
        }
    }
}
