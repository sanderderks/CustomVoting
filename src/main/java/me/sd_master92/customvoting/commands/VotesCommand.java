package me.sd_master92.customvoting.commands;

import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.plugin.command.SimpleCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class VotesCommand extends SimpleCommand
{
    private final Main plugin;

    public VotesCommand(Main plugin)
    {
        super(plugin, "votes");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args)
    {
        if (args.length == 0)
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;
                HashMap<String, String> placeholders = new HashMap<>();
                placeholders.put("%VOTES%", "" + new VoteFile(player, plugin).getVotes());
                placeholders.put("%s%", new VoteFile(player, plugin).getVotes() == 1 ? "" : "s");
                player.sendMessage(Messages.VOTES_COMMAND_SELF.getMessage(plugin, placeholders));
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
                sender.sendMessage(Messages.VOTES_COMMAND_OTHERS.getMessage(plugin, placeholders));
            } else
            {
                sender.sendMessage(Messages.VOTES_COMMAND_NOT_FOUND.getMessage(plugin));
            }
        }
    }

    @Override
    public void onCommand(Player player, String[] strings)
    {

    }
}
