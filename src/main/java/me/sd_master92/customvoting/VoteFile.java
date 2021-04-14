package me.sd_master92.customvoting;

import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.subjects.VoteTopSign;
import me.sd_master92.customvoting.subjects.VoteTopStand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VoteFile extends PlayerFile
{
    private final Main plugin;

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
        n--;
        List<VoteFile> topVoters = getTopVoters(plugin);
        if (n >= 0 && n < topVoters.size())
        {
            return topVoters.get(n);
        }
        return null;
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

    public void setVotes(int n, boolean update)
    {
        setTimeStamp("last");
        setNumber("votes", n);
        if(update)
        {
            VoteTopSign.updateAll(plugin);
            VoteTopStand.updateAll(plugin);
        }
    }

    public void addVote(boolean update)
    {
        setTimeStamp("last");
        addNumber("votes", 1);
        if(update)
        {
            VoteTopSign.updateAll(plugin);
            VoteTopStand.updateAll(plugin);
        }
    }

    public List<String> getQueue()
    {
        return plugin.getData().getStringList(Data.VOTE_QUEUE + "." + getUuid());
    }

    public boolean clearQueue()
    {
        return plugin.getData().delete(Data.VOTE_QUEUE + "." + getUuid());
    }

    public boolean addQueue(String service)
    {
        String path = Data.VOTE_QUEUE + "." + getUuid();
        List<String> queue = plugin.getData().getStringList(path);
        queue.add(service);
        plugin.getData().set(path, queue);
        return plugin.getData().saveConfig();
    }
}
