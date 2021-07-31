package me.sd_master92.customvoting.commands;

import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.database.PlayerRow;
import me.sd_master92.plugin.command.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetVotesCommand extends SimpleCommand
{
    private final Main plugin;

    public SetVotesCommand(Main plugin)
    {
        super(plugin, "setvotes", false);
        withUsage(ChatColor.RED + "- /setvotes <amount> [name]");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args)
    {
        if (args.length == 1)
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
                        if(plugin.useDatabase())
                        {
                            PlayerRow playerRow = new PlayerRow(plugin, player);
                            playerRow.setVotes(n, true);
                        } else
                        {
                            VoteFile voteFile = new VoteFile(player.getUniqueId().toString(), plugin);
                            voteFile.setVotes(n, true);
                        }
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
                        if(plugin.useDatabase())
                        {
                            PlayerRow playerRow = new PlayerRow(plugin, playerFile.getUuid());
                            playerRow.setVotes(n, true);
                        } else
                        {
                            VoteFile voteFile = new VoteFile(playerFile.getUuid(), plugin);
                            voteFile.setVotes(n, true);
                            sender.sendMessage(ChatColor.AQUA + voteFile.getName() + "'s " + ChatColor.GREEN + "votes have been set to " + ChatColor.AQUA + n + ChatColor.GREEN + ".");
                        }
                    } else
                    {
                        sender.sendMessage(Messages.INVALID_PLAYER.getMessage(plugin));
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
    }

    @Override
    public void onCommand(Player player, String[] strings)
    {

    }
}
