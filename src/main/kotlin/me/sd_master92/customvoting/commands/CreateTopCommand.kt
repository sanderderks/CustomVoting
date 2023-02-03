package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.core.errorLog
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.subjects.VoteTopStand
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
                VoteTopStand(plugin, top, player)
            } else
            {
                player.sendMessage(Strings.INVALID_ARGUMENT_NOT_POSITIVE_X.with("top"))
            }
        } catch (e: NumberFormatException)
        {
            player.sendMessage(Strings.INVALID_ARGUMENT_NOT_NUMBER_X.with("top"))
        } catch (e: Exception)
        {
            player.sendMessage(Strings.ERROR.toString())
            plugin.errorLog(Strings.VOTE_TOP_CREATE_ERROR.toString(), e)
        }
    }

    init
    {
        withPlayer(Messages.MUST_BE_PLAYER.getMessage(plugin))
        withUsage(Strings.VOTE_TOP_CREATE_COMMAND_USAGE.toString())
        withNoPermMessage(Messages.NO_PERMISSION.getMessage(plugin))
    }
}