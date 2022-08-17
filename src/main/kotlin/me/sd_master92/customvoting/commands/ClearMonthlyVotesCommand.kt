package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.sendText
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ClearMonthlyVotesCommand(private val plugin: CV) : SimpleCommand(plugin, "clearmonthlyvotes", true)
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
        if (args.isEmpty())
        {
            if (sender is Player)
            {
                Voter.get(plugin, sender).clearMonthlyVotes()
                sender.sendMessage(ChatColor.GREEN.toString() + "Your monthly votes have been reset.")
            } else
            {
                sender.sendMessage(ChatColor.RED.toString() + "- /clearmonthlyvotes <name>")
            }
        } else
        {
            val name = args[0]
            val voter = Voter.getByName(plugin, name)
            if (voter != null)
            {
                voter.clearMonthlyVotes()
                sender.sendMessage(ChatColor.AQUA.toString() + voter.name + "'s " + ChatColor.GREEN + "monthly votes have been reset.")
            } else
            {
                sender.sendText(plugin, Messages.INVALID_PLAYER)
            }
        }
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
    }

    init
    {
        withNoPermMessage(Messages.NO_PERMISSION.getMessage(plugin))
    }
}