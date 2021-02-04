package me.sd_master92.customvoting.commands;

import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Messages;
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
        if(command.getPermission() != null && sender.hasPermission(command.getPermission()))
        {
            if (args.length == 0)
            {
                if (sender instanceof Player)
                {
                    Player player = (Player) sender;
                    HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("%VOTES%", "" + new VoteFile(player, plugin).getVotes());
                    placeholders.put("%s%", new VoteFile(player, plugin).getVotes() == 1 ? "" : "s");
                    player.sendMessage(plugin.getMessages().getMessage(Messages.VOTES_COMMAND_SELF, placeholders));
                }
            } else
            {
                String name = args[0];
                PlayerFile playerFile = PlayerFile.getByName(name, plugin);
                if (playerFile != null)
                {
                    VoteFile voteFile = new VoteFile(playerFile.getUuid(), plugin);
                    HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("%PLAYER%", "" + voteFile.getName());
                    placeholders.put("%VOTES%", "" + voteFile.getVotes());
                    placeholders.put("%s%", voteFile.getVotes() == 1 ? "" : "s");
                    sender.sendMessage(plugin.getMessages().getMessage(Messages.VOTES_COMMAND_OTHERS, placeholders));
                } else
                {
                    sender.sendMessage(plugin.getMessages().getMessage(Messages.VOTES_COMMAND_NOT_FOUND));
                }
            }
        } else
        {
            sender.sendMessage(plugin.getMessages().getMessage(Messages.NO_PERMISSION));
        }
        return true;
    }
}
