package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.sendText
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetVotesCommand(private val plugin: CV) : SimpleCommand(plugin, "setvotes", false)
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
        if (args.size == 1)
        {
            if (sender is Player)
            {
                val amount = args[0]
                try
                {
                    val n = amount.toInt()
                    if (n >= 0)
                    {
                        Voter.getByUuid(plugin, sender).setVotes(n, true)
                        sender.sendMessage(ChatColor.GREEN.toString() + "Your votes have been set to " + ChatColor.AQUA + n + ChatColor.GREEN + ".")
                    } else
                    {
                        sender.sendMessage(ChatColor.RED.toString() + "Invalid argument: 'amount' must be positive.")
                    }
                } catch (e: Exception)
                {
                    sender.sendMessage(ChatColor.RED.toString() + "Invalid argument: 'amount' must be a number.")
                }
            } else
            {
                sender.sendMessage(ChatColor.RED.toString() + "- /setvotes <amount> <name>")
            }
        } else
        {
            val amount = args[0]
            val name = args[1]
            try
            {
                val n = amount.toInt()
                if (n >= 0)
                {
                    val voter = Voter.getByName(plugin, name)
                    if (voter != null)
                    {
                        voter.setVotes(n, true)
                        sender.sendMessage(ChatColor.AQUA.toString() + voter.name + "'s " + ChatColor.GREEN + "votes have been set to " + ChatColor.AQUA + n + ChatColor.GREEN + ".")
                    } else
                    {
                        sender.sendText(plugin, Messages.INVALID_PLAYER)
                    }
                } else
                {
                    sender.sendMessage(ChatColor.RED.toString() + "Invalid argument: 'amount' must be positive.")
                }
            } catch (e: Exception)
            {
                sender.sendMessage(ChatColor.RED.toString() + "Invalid argument: 'amount' must be a number.")
            }
        }
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
    }

    init
    {
        withUsage(ChatColor.RED.toString() + "- /setvotes <amount> [name]")
        withNoPermMessage(Messages.NO_PERMISSION.getMessage(plugin))
    }
}