package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.core.errorLog
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.subjects.stands.VoteTopStand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CreateTopCommand(private val plugin: CV) : SimpleCommand(plugin, "createtop", false)
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
        try
        {
            val top = args[0].toInt()
            if (top > 0)
            {
                VoteTopStand[plugin, top].create(player)
            } else
            {
                player.sendMessage(PMessage.GENERAL_ERROR_INVALID_ARGUMENT_NOT_POSITIVE_X.with("top"))
            }
        } catch (e: NumberFormatException)
        {
            player.sendMessage(PMessage.GENERAL_ERROR_INVALID_ARGUMENT_NOT_NUMBER_X.with("top"))
        } catch (e: Exception)
        {
            player.sendMessage(PMessage.GENERAL_ERROR.toString())
            plugin.errorLog(PMessage.VOTE_TOP_ERROR_CREATE.toString(), e)
        }
    }

    init
    {
        withPlayer(Message.MUST_BE_PLAYER.getMessage(plugin))
        withUsage(PMessage.VOTE_TOP_MESSAGE_CREATE_COMMAND_USAGE.toString())
        withNoPermMessage(Message.NO_PERMISSION.getMessage(plugin))
    }
}