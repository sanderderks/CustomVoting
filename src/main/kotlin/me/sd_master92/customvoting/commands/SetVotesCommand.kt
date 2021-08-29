package me.sd_master92.customvoting.commands

import me.sd_master92.plugin.command.SimpleCommand
import me.sd_master92.customvoting.database.PlayerRow
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customfile.PlayerFile
import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.constants.Messages
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.Exception

class SetVotesCommand(private val plugin: Main) : SimpleCommand(plugin, "setvotes", false)
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
                        if (plugin.hasDatabaseConnection())
                        {
                            val playerRow = PlayerRow(plugin, sender)
                            playerRow.setVotes(n, true)
                        } else
                        {
                            val voteFile = VoteFile(sender.uniqueId.toString(), plugin)
                            voteFile.setVotes(n, true)
                        }
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
                    val playerFile = PlayerFile.getByName(name, plugin)
                    if (playerFile != null)
                    {
                        if (plugin.hasDatabaseConnection())
                        {
                            val playerRow = PlayerRow(plugin, playerFile.uuid)
                            playerRow.setVotes(n, true)
                        } else
                        {
                            val voteFile = VoteFile(playerFile.uuid, plugin)
                            voteFile.setVotes(n, true)
                            sender.sendMessage(ChatColor.AQUA.toString() + voteFile.userName + "'s " + ChatColor.GREEN + "votes have been set to " + ChatColor.AQUA + n + ChatColor.GREEN + ".")
                        }
                    } else
                    {
                        sender.sendMessage(Messages.INVALID_PLAYER.getMessage(plugin))
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

    override fun onCommand(player: Player, strings: Array<String>)
    {
    }

    init
    {
        withUsage(ChatColor.RED.toString() + "- /setvotes <amount> [name]")
    }
}