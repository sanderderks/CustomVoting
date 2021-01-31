package me.sd_master92.customvoting.commands;

import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.types.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetVotesCommand implements CommandExecutor
{
    private final Main plugin;

    public SetVotesCommand(Main plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(command.getPermission() != null && sender.hasPermission(command.getPermission()))
        {
            if (args.length == 0)
            {
                if (sender instanceof Player)
                {
                    sender.sendMessage(ChatColor.RED + "- /setvotes <amount> [name]");
                } else
                {
                    sender.sendMessage(ChatColor.RED + "- /setvotes <amount> <name>");
                }
            } else if (args.length == 1)
            {
                if (sender instanceof Player)
                {
                    Player player = (Player) sender;
                    String amount = args[0];
                    try
                    {
                        int n = Integer.parseInt(amount);
                        if (n >= 0)
                        {
                            VoteFile voteFile = new VoteFile(player.getUniqueId().toString(), plugin);
                            voteFile.setVotes(n, true);
                            sender.sendMessage(ChatColor.GREEN + "Your votes have been set to " + ChatColor.AQUA + n + ChatColor.GREEN + ".");
                        } else
                        {
                            sender.sendMessage(ChatColor.RED + "Invalid argument: 'amount' must be positive.");
                        }
                    } catch (Exception e)
                    {
                        sender.sendMessage(ChatColor.RED + "Invalid argument: 'amount' must be a number.");
                    }
                } else
                {
                    sender.sendMessage(ChatColor.RED + "- /setvotes <amount> <name>");
                }
            } else
            {
                String amount = args[0];
                String name = args[1];
                try
                {
                    int n = Integer.parseInt(amount);
                    if (n >= 0)
                    {
                        PlayerFile playerFile = PlayerFile.getByName(name, plugin);
                        if (playerFile != null)
                        {
                            VoteFile voteFile = new VoteFile(playerFile.getUuid(), plugin);
                            voteFile.setVotes(n, true);
                            sender.sendMessage(ChatColor.AQUA + voteFile.getName() + "'s " + ChatColor.GREEN + "votes have been set to " + ChatColor.AQUA + n + ChatColor.GREEN + ".");
                        } else
                        {
                            sender.sendMessage(Messages.INVALID_PLAYER);
                        }
                    } else
                    {
                        sender.sendMessage(ChatColor.RED + "Invalid argument: 'amount' must be positive.");
                    }
                } catch (Exception e)
                {
                    sender.sendMessage(ChatColor.RED + "Invalid argument: 'amount' must be a number.");
                }
            }
        } else {
            sender.sendMessage(Messages.NO_PERMISSION);
        }
        return true;
    }
}
