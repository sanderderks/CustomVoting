package me.sd_master92.customvoting.commands.voteparty;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.subjects.VoteParty;
import me.sd_master92.plugin.command.SimpleSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VotePartyStartCommand extends SimpleSubCommand
{
    private final Main plugin;

    public VotePartyStartCommand(Main plugin)
    {
        super("start");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] strings)
    {
        if (!plugin.getData().getLocations(Data.VOTE_PARTY).isEmpty())
        {
            new VoteParty(plugin).start();
        } else
        {
            sender.sendMessage(ChatColor.RED + "There are no registered Vote Party Chests.");
        }
    }

    @Override
    public void onCommand(Player player, String[] strings)
    {

    }
}
