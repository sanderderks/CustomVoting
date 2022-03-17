package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.core.file.PlayerFile
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.database.PlayerRow
import me.sd_master92.customvoting.sendText
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VotesCommand(private val plugin: CV) : SimpleCommand(plugin, "votes")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
        if (args.isEmpty())
        {
            if (sender is Player)
            {
                val placeholders = HashMap<String, String>()
                val voter: Voter =
                    if (plugin.hasDatabaseConnection()) PlayerRow(plugin, sender) else VoteFile(sender, plugin)
                placeholders["%VOTES%"] = "${voter.votes}"
                placeholders["%PERIOD%"] = "${voter.period}"
                if (plugin.config.getBoolean(Settings.MONTHLY_PERIOD))
                {
                    placeholders["%s%"] = if (VoteFile(sender, plugin).period == 1) "" else "s"
                } else
                {
                    placeholders["%s%"] = if (VoteFile(sender, plugin).votes == 1) "" else "s"
                }
                sender.sendText(plugin, Messages.VOTES_COMMAND_SELF, placeholders)
            }
        } else
        {
            val name = args[0]
            val playerFile = PlayerFile.getByName(name)
            if (playerFile != null)
            {
                val voteFile = if (plugin.hasDatabaseConnection()) PlayerRow(
                    plugin,
                    playerFile.uuid
                ) else VoteFile(playerFile.uuid, plugin)
                val placeholders = HashMap<String, String>()
                placeholders["%PLAYER%"] = voteFile.name
                placeholders["%VOTES%"] = "${voteFile.votes}"
                placeholders["%PERIOD%"] = "${voteFile.period}"
                if (plugin.config.getBoolean(Settings.MONTHLY_PERIOD))
                {
                    placeholders["%s%"] = if (voteFile.period == 1) "" else "s"
                } else
                {
                    placeholders["%s%"] = if (voteFile.votes == 1) "" else "s"
                }
                sender.sendText(plugin, Messages.VOTES_COMMAND_OTHERS, placeholders)
            } else
            {
                sender.sendText(plugin, Messages.VOTES_COMMAND_NOT_FOUND)
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