package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.API;
import me.sd_master92.customvoting.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VoteCommand implements CommandExecutor
{
    private final Main plugin;

    public VoteCommand(Main plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        for (String message : API.getMessages("vote_command", null, plugin))
        {
            sender.sendMessage(message);
        }
        return true;
    }
}
