package me.sd_master92.customvoting.services;

import com.vexsoftware.votifier.model.Vote;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;

import java.util.HashMap;

public class VoteService
{
    private final Main plugin;

    public VoteService(Main plugin)
    {
        this.plugin = plugin;
    }

    public void forwardVote(String uuid, Vote vote)
    {
        new VoteFile(uuid, plugin).addVote(true);
        broadcastVote(vote);
    }

    public void broadcastVote(Vote vote)
    {
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("%PLAYER%", vote.getUsername());
        placeholders.put("%SERVICE%", vote.getServiceName());
        String message = plugin.getMessages().getMessage("broadcast", placeholders);
        plugin.getServer().broadcastMessage(message);
    }
}
