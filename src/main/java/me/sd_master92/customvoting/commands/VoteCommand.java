package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.types.Messages;
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
        if(command.getPermission() != null && sender.hasPermission(command.getPermission()))
        {
            for (String message : plugin.getMessages().getMessages(Messages.VOTE_COMMAND))
            {
                sender.sendMessage(message);
            }
        } else
        {
            sender.sendMessage(Messages.NO_PERMISSION);
        }
        return true;
    }
}
