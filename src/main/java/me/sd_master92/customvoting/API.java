package me.sd_master92.customvoting;

import org.bukkit.ChatColor;

import java.util.List;

public class API
{
    public static List<String> getVoteLinks(Main plugin)
    {
        List<String> links = plugin.getSettings().getConfig().getStringList("links");
        for (int i = 0; i < links.size(); i++)
        {
            links.set(i, ChatColor.translateAlternateColorCodes('&', links.get(i)));
        }
        return links;
    }
}
