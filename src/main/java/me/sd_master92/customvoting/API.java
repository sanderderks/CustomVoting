package me.sd_master92.customvoting;

import com.vexsoftware.votifier.model.Vote;
import org.bukkit.ChatColor;

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
        plugin.getServer().getConsoleSender().sendMessage(message);
    }

    public static String getMessage(String path, Map<String, String> placeholders, Main plugin)
    {
        String message = plugin.getMessages().getConfig().getString(path.toLowerCase());
        if (message != null)
        {
            message = ChatColor.translateAlternateColorCodes('&', message);
            if(placeholders != null)
            {
                for(String placeholder : placeholders.keySet())
                {
                    message = message.replace(placeholder, placeholders.get(placeholder));
                }
            }
            return message;
        }
        return "";
    }

    public static List<String> getMessages(String path, Map<String, String> placeholders, Main plugin)
    {
        List<String> messages = plugin.getConfig().getStringList(path.toLowerCase());
        for(int i = 0; i < messages.size(); i++)
        {
            String message = ChatColor.translateAlternateColorCodes('&', messages.get(i));
            if(placeholders != null)
            {
                for(String placeholder : placeholders.keySet())
                {
                    message = message.replace(placeholder, placeholders.get(placeholder));
                }
            }
            messages.set(i, message);
        }
        return messages;
    }
}
