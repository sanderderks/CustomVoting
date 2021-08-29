package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.constants.Voter;
import me.sd_master92.customvoting.database.PlayerTable;
import me.sd_master92.plugin.command.SimpleCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VoteTopCommand extends SimpleCommand
{
    private final Main plugin;

    public VoteTopCommand(Main plugin)
    {
        super(plugin, "votetop");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args)
    {
        List<? extends Voter> topVoters = plugin.hasDatabaseConnection() ? PlayerTable.getTopVoters(plugin) :
                VoteFile.getTopVoters(plugin);
        if (topVoters.size() > 0)
        {
            List<String> messages = new ArrayList<>();
            for (String message : Messages.VOTE_TOP_COMMAND_FORMAT.getMessages(plugin))
            {
                if (!message.contains("%PLAYERS%"))
                {
                    messages.add(message);
                } else
                {
                    Map<String, String> placeholders = new HashMap<>();
                    for (Voter topVoter :
                            topVoters.stream().limit(5).collect(Collectors.toList()))
                    {
                        placeholders.put("%PLAYER%", topVoter.getName());
                        placeholders.put("%VOTES%", "" + topVoter.getVotes());
                        placeholders.put("%s%", topVoter.getVotes() == 1 ? "s" : "");
                        message = Messages.VOTE_TOP_COMMAND_PLAYERS.getMessage(plugin, placeholders);
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
            sender.sendMessage(Messages.VOTE_TOP_COMMAND_NOT_FOUND.getMessage(plugin));
        }
    }

    @Override
    public void onCommand(Player player, String[] strings)
    {

    }
}
