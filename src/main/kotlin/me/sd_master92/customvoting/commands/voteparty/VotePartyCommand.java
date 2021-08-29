package me.sd_master92.customvoting.commands.voteparty;

import me.sd_master92.customvoting.Main;
import me.sd_master92.plugin.command.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VotePartyCommand extends SimpleCommand
{
    public VotePartyCommand(Main plugin)
    {
        super(plugin, "voteparty", false, new VotePartyCreateCommand(), new VotePartyStartCommand(plugin));
        withUsage(ChatColor.RED + "- /voteparty create | start");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args)
    {

    }

    @Override
    public void onCommand(Player player, String[] args)
    {

    }
}
