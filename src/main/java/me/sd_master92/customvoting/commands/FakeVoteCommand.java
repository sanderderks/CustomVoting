package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.services.VoteService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FakeVoteCommand implements CommandExecutor
{
    private final VoteService voteService;

    public FakeVoteCommand(Main plugin)
    {
        voteService = new VoteService(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (command.getPermission() != null && sender.hasPermission(command.getPermission()))
        {
            if(args.length == 0)
            {
                voteService.fakeVote(sender.getName());
            } else
            {
                voteService.fakeVote(args[0]);
            }
        } else
        {
            sender.sendMessage(Messages.NO_PERMISSION);
        }
        return true;
    }
}
