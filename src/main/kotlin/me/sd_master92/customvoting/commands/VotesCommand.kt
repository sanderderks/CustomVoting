package me.sd_master92.customvoting.commands

import me.sd_master92.plugin.command.SimpleCommand
import java.util.HashMap
import me.sd_master92.customvoting.database.PlayerRow
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customfile.PlayerFile
import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.constants.Messages
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VotesCommand(private val plugin: Main) : SimpleCommand(plugin, "votes")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
        if (args.isEmpty())
        {
            if (sender is Player)
            {
                val placeholders = HashMap<String, String>()
                placeholders["%VOTES%"] = "" + if (plugin.hasDatabaseConnection()) PlayerRow(plugin, sender).votes else VoteFile(sender, plugin).votes
                placeholders["%s%"] = if (VoteFile(sender, plugin).votes == 1) "" else "s"
                sender.sendMessage(Messages.VOTES_COMMAND_SELF.getMessage(plugin, placeholders))
            }
        } else
        {
            val name = args[0]
            val playerFile = PlayerFile.getByName(name, plugin)
            if (playerFile != null)
            {
                val voteFile = if (plugin.hasDatabaseConnection()) PlayerRow(plugin, playerFile.uuid) else VoteFile(playerFile.uuid, plugin)
                val placeholders = HashMap<String, String>()
                placeholders["%PLAYER%"] = "" + voteFile.userName
                placeholders["%VOTES%"] = "" + voteFile.votes
                placeholders["%s%"] = if (voteFile.votes == 1) "" else "s"
                sender.sendMessage(Messages.VOTES_COMMAND_OTHERS.getMessage(plugin, placeholders))
            } else
            {
                sender.sendMessage(Messages.VOTES_COMMAND_NOT_FOUND.getMessage(plugin))
            }
        }
    }

    override fun onCommand(player: Player, strings: Array<String>)
    {
    }
}