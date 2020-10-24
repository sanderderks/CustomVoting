package me.sd_master92.customvoting;

import com.vexsoftware.votifier.model.Vote;
import me.sd_master92.customfile.PlayerFile;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class API
{
    public static void broadcastVote(Vote vote, Main plugin)
    {
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("%PLAYER%", vote.getUsername());
        placeholders.put("%SERVICE%", vote.getServiceName());
        String message = getMessage("broadcast", placeholders, plugin);
        plugin.getServer().broadcastMessage(message);
    }

    public static List<VoteFile> getTopVoters(Main plugin)
    {
        List<VoteFile> voteFiles = new ArrayList<>();
        for (PlayerFile playerFile : PlayerFile.getAll(plugin))
        {
            voteFiles.add(new VoteFile(playerFile.getUuid(), plugin));
        }
        voteFiles.sort((a, b) ->
        {
            int votesA = b.getVotes();
            int votesB = a.getVotes();
            return Integer.compare(votesA, votesB);
        });
        List<VoteFile> topVoters = new ArrayList<>();
        for (int i = 0; i < plugin.getSettings().getNumber("votetop_command") && i < voteFiles.size(); i++)
        {
            topVoters.add(voteFiles.get(i));
        }
        return topVoters;
    }

    public static String getMessage(String path, Map<String, String> placeholders, Main plugin)
    {
        String message = plugin.getMessages().getConfig().getString(path.toLowerCase());
        if (message != null)
        {
            message = ChatColor.translateAlternateColorCodes('&', message);
            if (placeholders != null)
            {
                for (String placeholder : placeholders.keySet())
                {
                    message = message.replace(placeholder, placeholders.get(placeholder));
                }
            }
            return message;
        }
        return "";
    }

    public static List<String> getMessages(String path, Map<String, String> placeholders, boolean replaceFirst, Main plugin)
    {
        List<String> messages = plugin.getMessages().getConfig().getStringList(path.toLowerCase());
        for (int i = 0; i < messages.size(); i++)
        {
            String message = ChatColor.translateAlternateColorCodes('&', messages.get(i));
            if (placeholders != null)
            {
                for (String placeholder : placeholders.keySet())
                {
                    if (replaceFirst)
                    {
                        message = message.replaceFirst(placeholder, placeholders.get(placeholder));
                    } else
                    {
                        message = message.replace(placeholder, placeholders.get(placeholder));
                    }
                }
            }
            messages.set(i, message);
        }
        return messages;
    }

    public static List<String> getMessages(String path, Map<String, String> placeholders, Main plugin)
    {
        return getMessages(path.toLowerCase(), placeholders, false, plugin);
    }
}
