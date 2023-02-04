package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.Settings
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
                val voter = Voter.get(plugin, sender)
                placeholders["%VOTES%"] = "${voter.votes}"
                placeholders["%MONTHLY_VOTES%"] = "${voter.monthlyVotes}"
                if (plugin.config.getBoolean(Settings.MONTHLY_VOTES.path))
                {
                    placeholders["%s%"] = if (voter.monthlyVotes == 1) "" else "s"
                } else
                {
                    placeholders["%s%"] = if (voter.votes == 1) "" else "s"
                }
                sender.sendText(plugin, Message.VOTES_COMMAND_SELF, placeholders)
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
                sender.sendText(plugin, Message.VOTES_COMMAND_OTHERS, placeholders)
            } else
            {
                sender.sendText(plugin, Message.VOTES_COMMAND_NOT_FOUND)
            }
        }
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
    }

    init
    {
        withNoPermMessage(Message.NO_PERMISSION.getMessage(plugin))
    }
}