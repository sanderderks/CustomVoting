package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.subjects.CustomVote;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FakeVoteCommand implements CommandExecutor
{
    private final Main plugin;

    public FakeVoteCommand(Main plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (command.getPermission() != null && sender.hasPermission(command.getPermission()))
        {
            if(args.length == 0)
            {
                if(sender instanceof Player)
                {
                    fakeVote(sender.getName());
                } else
                {
                    sender.sendMessage(ChatColor.RED + "- /fakevote <name>");
                }
            } else
            {
                String name = args[0];
                if(VoteFile.getByName(name, plugin) != null)
                {
                    fakeVote(args[0]);
                } else
                {
                    sender.sendMessage(Messages.INVALID_PLAYER.getMessage(plugin));
                }
            }
        } else
        {
            sender.sendMessage(Messages.NO_PERMISSION.getMessage(plugin));
        }
        return true;
    }

    private void fakeVote(String name)
    {
        CustomVote.create(plugin, name, "fakevote.com");
    }
}
