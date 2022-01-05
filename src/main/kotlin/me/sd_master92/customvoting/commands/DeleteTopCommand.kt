package me.sd_master92.customvoting.commands

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.plugin.command.SimpleCommand
import me.sd_master92.customvoting.subjects.VoteTopStand
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.Exception

class DeleteTopCommand(plugin: CV) : SimpleCommand(plugin, "deletetop", false)
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
        try
        {
            val top = args[0].toInt()
            val voteTopStand = VoteTopStand[top]
            if (voteTopStand != null)
            {
                voteTopStand.delete(player)
            } else
            {
                player.sendMessage(ChatColor.RED.toString() + "That Vote Stand does not exist.")
            }
        } catch (e: Exception)
        {
            player.sendMessage(ChatColor.RED.toString() + "Invalid argument: 'top' must be a number.")
        }
    }

    init
    {
        withPlayer(Messages.MUST_BE_PLAYER.getMessage(plugin))
        withUsage(ChatColor.RED.toString() + "- /deletetop <top>")
        withNoPermMessage(Messages.NO_PERMISSION.getMessage(plugin))
    }
}