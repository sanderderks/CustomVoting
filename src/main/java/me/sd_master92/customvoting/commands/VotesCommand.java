package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.API;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class VotesCommand implements CommandExecutor
{
    private final Main plugin;

    public VotesCommand(Main plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 0)
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;
                HashMap<String, String> placeholders = new HashMap<>();
                placeholders.put("%VOTES%", "" + new VoteFile(player, plugin).getVotes());
                player.sendMessage(API.getMessage("votes_command", placeholders, plugin));
            }
        }
        return true;
    }
}
