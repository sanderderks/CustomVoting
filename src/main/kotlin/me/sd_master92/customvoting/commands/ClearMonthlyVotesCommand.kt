package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.core.file.PlayerFile
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.database.PlayerRow
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
                if (plugin.hasDatabaseConnection())
                {
                    val playerRow = PlayerRow(plugin, sender)
                    playerRow.clearMonthlyVotes()
                } else
                {
                    val voteFile = VoteFile(sender.uniqueId.toString(), plugin)
                    voteFile.clearMonthlyVotes()
                }
                sender.sendMessage(ChatColor.GREEN.toString() + "Your monthly votes have been reset.")
            } else
            {
                sender.sendMessage(ChatColor.RED.toString() + "- /clearmonthlyvotes <name>")
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
                    playerRow.clearMonthlyVotes()
                } else
                {
                    val voteFile = VoteFile(playerFile.uuid, plugin)
                    voteFile.clearMonthlyVotes()
                    sender.sendMessage(ChatColor.AQUA.toString() + voteFile.name + "'s " + ChatColor.GREEN + "monthly votes have been reset.")
                }
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