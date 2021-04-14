package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.subjects.VoteTopStand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateTopCommand implements CommandExecutor
{
    private final Main plugin;

    public CreateTopCommand(Main plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (command.getPermission() != null && sender.hasPermission(command.getPermission()))
            {
                if (args.length > 0)
                {
                    try
                    {
                        int top = Integer.parseInt(args[0]);
                        if(top > 0)
                        {
                            new VoteTopStand(plugin, top, player);
                        } else
                        {
                            player.sendMessage(ChatColor.RED + "Invalid argument: 'top' must be a positive number.");
                        }
                    } catch (Exception e)
                    {
                        player.sendMessage(ChatColor.RED + "Invalid argument: 'top' must be a number.");
                    }
                } else
                {
                    player.sendMessage(ChatColor.RED + "- /createtop <top>");
                }
            } else
            {
                player.sendMessage(plugin.getMessages().getMessage(Messages.NO_PERMISSION));
            }
        } else
        {
            sender.sendMessage(plugin.getMessages().getMessage(Messages.NO_PERMISSION));
        }
        return true;
    }
}

