package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.services.VoteTopService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

public class VoteTopCommand implements CommandExecutor
{
    private final Main plugin;
    private final VoteTopService voteTopService;

    public VoteTopCommand(Main plugin)
    {
        this.plugin = plugin;
        voteTopService = new VoteTopService(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(command.getPermission() != null && sender.hasPermission(command.getPermission()))
        {
            List<VoteFile> topVoters = voteTopService.getTopVoters();
            if (topVoters.size() > 0)
            {
                List<String> messages = new ArrayList<>();
                for (String message : plugin.getMessages().getMessages("vote_top_command.format", null))
                {
                    if (!message.contains("%PLAYERS%"))
                    {
                        messages.add(message);
                    } else
                    {
                        Map<String, String> placeholders = new HashMap<>();
                        for (VoteFile topVoter : topVoters.stream().limit(plugin.getSettings().getNumber("vote_top_command")).collect(Collectors.toList()))
                        {
                            placeholders.put("%PLAYER%", topVoter.getName());
                            placeholders.put("%VOTES%", "" + topVoter.getVotes());
                            message = plugin.getMessages().getMessage("vote_top_command.players", placeholders);
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
                sender.sendMessage(plugin.getMessages().getMessage("vote_top_command.not_found", null));
            }
        } else
        {
            sender.sendMessage(Messages.NO_PERMISSION);
        }
        return true;
    }
}
