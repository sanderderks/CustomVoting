package me.sd_master92.customvoting.commands.voteparty;

import me.sd_master92.customvoting.subjects.VoteParty;
import me.sd_master92.plugin.command.SimpleSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VotePartyCreateCommand extends SimpleSubCommand
{
    public VotePartyCreateCommand()
    {
        super("create");
        withPlayer();
        withPermission("voteparty.create");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args)
    {

    }

    @Override
    public void onCommand(Player player, String[] args)
    {
        player.getInventory().addItem(VoteParty.VOTE_PARTY_ITEM);
        player.sendMessage(ChatColor.GREEN + "You have been given the Vote Party Chest.");
    }
}
