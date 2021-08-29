package me.sd_master92.customvoting.commands;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.subjects.CustomVote;
import me.sd_master92.plugin.command.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FakeVoteCommand extends SimpleCommand
{
    private final Main plugin;

    public FakeVoteCommand(Main plugin)
    {
        super(plugin, "fakevote");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args)
    {
        if (args.length == 0)
        {
            if (sender instanceof Player)
            {
                fakeVote(sender.getName());
            } else
            {
                sender.sendMessage(ChatColor.RED + "- /fakevote <name>");
            }
        } else
        {
            String name = args[0];
            if (VoteFile.getByName(name, plugin) != null)
            {
                fakeVote(args[0]);
            } else
            {
                sender.sendMessage(Messages.INVALID_PLAYER.getMessage(plugin));
            }
        }
    }

    @Override
    public void onCommand(Player player, String[] args)
    {

    }

    private void fakeVote(String name)
    {
        CustomVote.create(plugin, name, "fakevote.com");
    }
}
