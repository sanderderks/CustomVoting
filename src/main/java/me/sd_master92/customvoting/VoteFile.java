package me.sd_master92.customvoting;

import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.services.VoteTopService;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VoteFile extends PlayerFile
{
    private final Main plugin;
    private final VoteTopService voteTopService;

    public VoteFile(String uuid, Main plugin)
    {
        super(uuid, plugin);
        this.plugin = plugin;
        voteTopService = new VoteTopService(plugin);
        register();
    }

    public VoteFile(Player player, Main plugin)
    {
        super(player, plugin);
        this.plugin = plugin;
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

    public static List<VoteFile> getTopVoters(Main plugin)
    {
        List<VoteFile> topVoters = new ArrayList<>();
        for (PlayerFile playerFile : PlayerFile.getAll(plugin))
        {
            topVoters.add(new VoteFile(playerFile.getUuid(), plugin));
        }
        topVoters.sort((x, y) ->
        {
            int compare = Integer.compare(y.getVotes(), x.getVotes());
            if (compare == 0)
            {
                compare = Long.compare(x.getTimeStamp("last"), y.getTimeStamp("last"));
            }
            return compare;
        });
        return topVoters;
    }

    public static VoteFile getTopVoter(Main plugin, int n)
    {
        List<VoteFile> topVoters = getTopVoters(plugin);
        if (n >= 0 && n < topVoters.size())
        {
            return topVoters.get(n);
        }
        return null;
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

    public List<String> getQueue()
    {
        return plugin.getData().getStringList("queue." + getUuid());
    }

    public boolean clearQueue()
    {
        return plugin.getData().delete("queue." + getUuid());
    }
}
