package me.sd_master92.customvoting.commands.voteparty

import me.sd_master92.core.command.SimpleSubCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.subjects.voteparty.VoteParty
import me.sd_master92.customvoting.subjects.voteparty.VotePartyChest
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VotePartyStartCommand(private val plugin: CV) : SimpleSubCommand("start")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
        if (VotePartyChest.getAll(plugin).isNotEmpty())
        {
            if (!VoteParty(plugin).start())
            {
                sender.sendMessage(PMessage.VOTE_PARTY_MESSAGE_QUEUED.toString())
            }
        } else
        {
            sender.sendMessage(PMessage.VOTE_PARTY_ERROR_NO_CHESTS.toString())
        }
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
    }
}