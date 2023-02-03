package me.sd_master92.customvoting.commands.voteparty

import me.sd_master92.core.command.SimpleSubCommand
import me.sd_master92.customvoting.constants.enumerations.Strings
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
        player.sendMessage(Strings.VOTE_PARTY_CHEST_RECEIVE.toString())
    }

    init
    {
        withPlayer()
        withPermission("voteparty.create")
    }
}