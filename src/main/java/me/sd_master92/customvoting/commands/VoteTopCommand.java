package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.API;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

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
        List<VoteFile> topVoters = API.getTopVoters(plugin);
        if(topVoters.size() > 0)
        {
            List<String> messages = new ArrayList<>();
            for (String message : API.getMessages("vote_top_command.format", null, plugin))
            {
                if (!message.contains("%PLAYERS%"))
                {
                    messages.add(message);
                } else
                {
                    Map<String, String> placeholders = new HashMap<>();
                    for (int i = 0; i < topVoters.size(); i++)
                    {
                        VoteFile topVoter = topVoters.get(i);
                        String name = topVoter.getName();
                        if(name == null)
                        {
                            Player player = Bukkit.getPlayer(UUID.fromString(topVoter.getUuid()));
                            if(player != null)
                            {
                                name = player.getName();
                            }
                        }
                        placeholders.put("%PLAYER%", name != null ? name : "unknown");
                        placeholders.put("%VOTES%", "" + topVoter.getVotes());
                        message = API.getMessage("vote_top_command.players", placeholders, plugin);
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
            sender.sendMessage(API.getMessage("vote_top_command.no_players", null, plugin));
        }
        return true;
    }
}
