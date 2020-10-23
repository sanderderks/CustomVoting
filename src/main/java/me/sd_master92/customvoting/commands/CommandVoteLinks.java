package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.API;
import me.sd_master92.customvoting.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandVoteLinks implements CommandExecutor
{
    private Main plugin;

    public CommandVoteLinks(Main plugin)
    {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(command.getName().equalsIgnoreCase("vote"))
        {
            for(String line : API.getVoteLinks(plugin))
            {
                sender.sendMessage(line);
            }
        }
        return false;
    }
}
