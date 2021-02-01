package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.constants.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VoteTopCommand implements CommandExecutor
{
    private final Main plugin;

    public VoteTopCommand(Main plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (command.getPermission() != null && sender.hasPermission(command.getPermission()))
        {
            List<VoteFile> topVoters = VoteFile.getTopVoters(plugin);
            if (topVoters.size() > 0)
            {
                List<String> messages = new ArrayList<>();
                for (String message : plugin.getMessages().getMessages(Messages.VOTE_TOP_COMMAND_FORMAT))
                {
                    if (!message.contains("%PLAYERS%"))
                    {
                        messages.add(message);
                    } else
                    {
                        Map<String, String> placeholders = new HashMap<>();
                        for (VoteFile topVoter :
                                topVoters.stream().limit(plugin.getSettings().getNumber(Settings.VOTE_TOP_COMMAND_SHOW_PLAYERS)).collect(Collectors.toList()))
                        {
                            placeholders.put("%PLAYER%", topVoter.getName());
                            placeholders.put("%VOTES%", "" + topVoter.getVotes());
                            message = plugin.getMessages().getMessage(Messages.VOTE_TOP_COMMAND_PLAYERS, placeholders);
                            messages.add(message);
                        }
                    }
                }
                for (String message : messages)
                {
                    sender.sendMessage(message);
                }
            } else
            {
                sender.sendMessage(plugin.getMessages().getMessage(Messages.VOTE_TOP_COMMAND_NOT_FOUND));
            }
        } else
        {
            sender.sendMessage(plugin.getMessages().getMessage(Messages.NO_PERMISSION));
        }
        return true;
    }
}
