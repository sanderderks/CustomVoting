package me.sd_master92.customvoting.commands.voteparty

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.PMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VotePartyCommand(plugin: CV) :
    SimpleCommand(
        plugin,
        "voteparty",
        false,
        VotePartyCreateCommand(plugin),
        VotePartyStartCommand(plugin),
        VotePartyStopCommand(plugin)
    )
{
    override suspend fun onCommand(sender: CommandSender, args: Array<out String>)
    {
    }

    override suspend fun onCommand(player: Player, args: Array<out String>)
    {
    }

    init
    {
        withUsage(PMessage.VOTE_PARTY_MESSAGE_COMMAND_USAGE.toString())
        withNoPermMessage(Message.NO_PERMISSION.getMessage(plugin))
        withPlayer(Message.MUST_BE_PLAYER.getMessage(plugin))
    }
}