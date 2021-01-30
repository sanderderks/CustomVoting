package me.sd_master92.customvoting;

import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.services.VoteTopService;
import org.bukkit.entity.Player;

public class VoteFile extends PlayerFile
{
    private final VoteTopService voteTopService;

    public VoteFile(String uuid, Main plugin)
    {
        super(uuid, plugin);
        voteTopService = new VoteTopService(plugin);
        register();
    }

    public VoteFile(Player player, Main plugin)
    {
        super(player, plugin);
        voteTopService = new VoteTopService(plugin);
        register();
    }

    private void register()
    {
        if (getVotes() == 0)
        {
            setVotes(0, false);
        }
    }

    public int getVotes()
    {
        return getNumber("votes");
    }

    public void setVotes(int n, boolean updateSigns)
    {
        setNumber("votes", n);
        if(updateSigns)
        {
            voteTopService.updateSigns();
        }
    }

    public void addVote(boolean updateSigns)
    {
        setTimeStamp("last");
        addNumber("votes", 1);
        if(updateSigns)
        {
            voteTopService.updateSigns();
        }
    }
}
