package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.services.VoteService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FakeVoteCommand implements CommandExecutor
{
    private final Main plugin;
    private final VoteService voteService;

    public FakeVoteCommand(Main plugin)
    {
        this.plugin = plugin;
        voteService = new VoteService(plugin);
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
                    voteService.fakeVote(sender.getName());
                } else
                {
                    sender.sendMessage(ChatColor.RED + "- /fakevote <name>");
                }
            } else
            {
                String name = args[0];
                if(VoteFile.getByName(name, plugin) != null)
                {
                    voteService.fakeVote(args[0]);
                } else
                {
                    sender.sendMessage(plugin.getMessages().getMessage(Messages.INVALID_PLAYER));
                }
            }
        } else
        {
            sender.sendMessage(plugin.getMessages().getMessage(Messages.NO_PERMISSION));
        }
        return true;
    }
}
