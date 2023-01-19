package me.sd_master92.customvoting.commands.voteparty

import me.sd_master92.core.command.SimpleSubCommand
import me.sd_master92.customvoting.subjects.voteparty.VoteParty
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VotePartyCreateCommand : SimpleSubCommand("create")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
        player.inventory.addItem(VoteParty.VOTE_PARTY_ITEM)
        player.sendMessage(ChatColor.GREEN.toString() + "You have been given the Vote Party Chest.")
    }

    init
    {
        withPlayer()
        withPermission("voteparty.create")
    }
}