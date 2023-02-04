package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.sendText
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
                        Voter.get(plugin, sender).setVotes(n, true)
                        sender.sendMessage(PMessage.VOTES_SET_MESSAGE_SELF_X.with("$n"))
                    } else
                    {
                        sender.sendMessage(PMessage.GENERAL_ERROR_INVALID_ARGUMENT_NOT_POSITIVE_X.with("amount"))
                    }
                } catch (e: Exception)
                {
                    sender.sendMessage(PMessage.GENERAL_ERROR_INVALID_ARGUMENT_NOT_NUMBER_X.with("amount"))
                }
            } else
            {
                sender.sendMessage(PMessage.VOTES_SET_MESSAGE_COMMAND_USAGE.toString())
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
                        sender.sendMessage(PMessage.VOTES_SET_MESSAGE_OTHER_XY.with(voter.name, "$n"))
                    } else
                    {
                        sender.sendText(plugin, Message.INVALID_PLAYER)
                    }
                } else
                {
                    sender.sendMessage(PMessage.GENERAL_ERROR_INVALID_ARGUMENT_NOT_POSITIVE_X.with("amount"))
                }
            } catch (e: Exception)
            {
                sender.sendMessage(PMessage.GENERAL_ERROR_INVALID_ARGUMENT_NOT_NUMBER_X.with("amount"))
            }
        }
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
    }

    init
    {
        withUsage(PMessage.VOTES_SET_MESSAGE_COMMAND_USAGE.toString())
        withNoPermMessage(Message.NO_PERMISSION.getMessage(plugin))
    }
}