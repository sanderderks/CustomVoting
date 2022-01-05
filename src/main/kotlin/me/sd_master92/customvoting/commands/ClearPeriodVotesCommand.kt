package me.sd_master92.customvoting.commands

import me.sd_master92.customfile.PlayerFile
import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.database.PlayerRow
import me.sd_master92.plugin.command.SimpleCommand
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ClearPeriodVotesCommand(private val plugin: Main) : SimpleCommand(plugin, "clearperiodvotes", true)
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
        if (args.isEmpty())
        {
            if (sender is Player)
            {
                if (plugin.hasDatabaseConnection())
                {
                    val playerRow = PlayerRow(plugin, sender)
                    playerRow.clearPeriod()
                } else
                {
                    val voteFile = VoteFile(sender.uniqueId.toString(), plugin)
                    voteFile.clearPeriod()
                }
                sender.sendMessage(ChatColor.GREEN.toString() + "Your period votes have been reset.")
            } else
            {
                sender.sendMessage(ChatColor.RED.toString() + "- /clearperiodvotes <name>")
            }
        } else
        {
            val name = args[0]
            val playerFile = PlayerFile.getByName(name)
            if (playerFile != null)
            {
                if (plugin.hasDatabaseConnection())
                {
                    val playerRow = PlayerRow(plugin, playerFile.uuid)
                    playerRow.clearPeriod()
                } else
                {
                    val voteFile = VoteFile(playerFile.uuid, plugin)
                    voteFile.clearPeriod()
                    sender.sendMessage(ChatColor.AQUA.toString() + voteFile.name + "'s " + ChatColor.GREEN + "period votes have been reset.")
                }
            } else
            {
                sender.sendMessage(Messages.INVALID_PLAYER.getMessage(plugin))
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