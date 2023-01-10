package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.database.PlayerTable
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
                    if (plugin.hasDatabaseConnection()) PlayerTable(plugin, sender) else VoteFile.get(plugin, sender)
                placeholders["%VOTES%"] = "${voter.votes}"
                placeholders["%MONTHLY_VOTES%"] = "${voter.monthlyVotes}"
                if (plugin.config.getBoolean(Settings.MONTHLY_VOTES.path))
                {
                    placeholders["%s%"] = if (VoteFile.get(plugin, sender).monthlyVotes == 1) "" else "s"
                } else
                {
                    placeholders["%s%"] = if (VoteFile.get(plugin, sender).votes == 1) "" else "s"
                }
                sender.sendText(plugin, Messages.VOTES_COMMAND_SELF, placeholders)
            }
        } else
        {
            val name = args[0]
            val voter = Voter.getByName(plugin, name)
            if (voter != null)
            {
                val placeholders = HashMap<String, String>()
                placeholders["%PLAYER%"] = voter.name
                placeholders["%VOTES%"] = "${voter.votes}"
                placeholders["%MONTHLY_VOTES%"] = "${voter.monthlyVotes}"
                if (plugin.config.getBoolean(Settings.MONTHLY_VOTES.path))
                {
                    placeholders["%s%"] = if (voter.monthlyVotes == 1) "" else "s"
                } else
                {
                    placeholders["%s%"] = if (voter.votes == 1) "" else "s"
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