package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.subjects.VoteTopStand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteTopCommand implements CommandExecutor
{
    private final Main plugin;

    public DeleteTopCommand(Main plugin)
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
                        VoteTopStand voteTopStand = VoteTopStand.get(top);
                        if(voteTopStand != null)
                        {
                            voteTopStand.delete(player);
                        } else
                        {
                            player.sendMessage(ChatColor.RED + "That Vote Stand does not exist.");
                        }
                    } catch (Exception e)
                    {
                        sender.sendMessage(ChatColor.RED + "Invalid argument: 'top' must be a number.");
                    }
                } else
                {
                    sender.sendMessage(ChatColor.RED + "- /deletetop <top>");
                }
            } else
            {
                player.sendMessage(Messages.NO_PERMISSION.getMessage(plugin));
            }
        } else
        {
            sender.sendMessage(Messages.MUST_BE_PLAYER.getMessage(plugin));
        }
        return true;
    }
}
