package me.sd_master92.customvoting.commands;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Date;

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
        if(command.getPermission() != null && sender.hasPermission(command.getPermission()))
        {
            Vote vote = new Vote();
            vote.setUsername(sender.getName());
            vote.setServiceName("fakevote.com");
            vote.setAddress("0.0.0.0");
            Date date = new Date();
            vote.setTimeStamp(String.valueOf(date.getTime()));
            plugin.getServer().getPluginManager().callEvent(new VotifierEvent(vote));
        } else
        {
            sender.sendMessage(Messages.NO_PERMISSION);
        }
        return true;
    }
}
