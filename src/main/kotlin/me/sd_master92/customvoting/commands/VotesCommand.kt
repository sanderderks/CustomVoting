package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.getVotesPlaceholders
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
                val topVoter = Voter.get(plugin, sender)
                sender.sendText(plugin, Message.VOTES_COMMAND_SELF, topVoter.getVotesPlaceholders(plugin))
            }
        } else
        {
            val name = args[0]
            val topVoter = Voter.getByName(plugin, name)
            if (topVoter != null)
            {
                sender.sendText(plugin, Message.VOTES_COMMAND_OTHERS, topVoter.getVotesPlaceholders(plugin))
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