package me.sd_master92.customvoting.commands.voteparty

import me.sd_master92.core.command.SimpleSubCommand
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.subjects.voteparty.VoteParty
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
        player.sendMessage(PMessage.VOTE_PARTY_MESSAGE_CHEST_RECEIVED.toString())
    }

    init
    {
        withPlayer()
        withPermission("voteparty.create")
    }
}