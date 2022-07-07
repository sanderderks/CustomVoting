package me.sd_master92.customvoting.commands.voteparty

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Messages
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VotePartyCommand(plugin: CV) :
    SimpleCommand(plugin, "voteparty", false, VotePartyCreateCommand(), VotePartyStartCommand(plugin))
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
    }

    init
    {
        withUsage(ChatColor.RED.toString() + "- /voteparty create | start")
        withNoPermMessage(Messages.NO_PERMISSION.getMessage(plugin))
        withPlayer(Messages.MUST_BE_PLAYER.getMessage(plugin))
    }
}