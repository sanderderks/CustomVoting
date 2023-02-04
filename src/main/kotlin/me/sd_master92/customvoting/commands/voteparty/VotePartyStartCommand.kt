package me.sd_master92.customvoting.commands.voteparty

import me.sd_master92.core.command.SimpleSubCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.subjects.voteparty.VoteParty
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VotePartyStartCommand(private val plugin: CV) : SimpleSubCommand("start")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
        if (plugin.data.getLocations(Data.VOTE_PARTY.path).isNotEmpty())
        {
            VoteParty(plugin).start()
        } else
        {
            sender.sendMessage(Strings.VOTE_PARTY_ERROR_NO_CHESTS.toString())
        }
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
    }
}