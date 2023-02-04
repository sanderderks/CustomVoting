package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.sendText
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
                sender.sendMessage(Strings.MONTHLY_VOTES_MESSAGE_RESET_SUCCESS_SELF.toString())
            } else
            {
                sender.sendMessage(Strings.MONTHLY_VOTES_MESSAGE_RESET_COMMAND_USAGE.toString())
            }
        } else
        {
            val name = args[0]
            val voter = Voter.getByName(plugin, name)
            if (voter != null)
            {
                voter.clearMonthlyVotes()
                sender.sendMessage(Strings.MONTHLY_VOTES_MESSAGE_RESET_SUCCESS_OTHER_X.with(voter.name))
            } else
            {
                sender.sendText(plugin, Message.INVALID_PLAYER)
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