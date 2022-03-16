package me.sd_master92.customvoting.commands

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.subjects.CitizenStand
import me.sd_master92.customvoting.subjects.VoteTopStand
import me.sd_master92.plugin.command.SimpleCommand
import org.bukkit.ChatColor
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
                if (CV.CITIZENS)
                {
                    CitizenStand(plugin, top, player)
                } else
                {
                    VoteTopStand(plugin, top, player)
                }
            } else
            {
                player.sendMessage(ChatColor.RED.toString() + "Invalid argument: 'top' must be a positive number.")
            }
        } catch (e: NumberFormatException)
        {
            player.sendMessage(ChatColor.RED.toString() + "Invalid argument: 'top' must be a number.")
        } catch (e: Exception)
        {
            player.sendMessage(ChatColor.RED.toString() + "Something went wrong!")
            plugin.errorLog("Error while creating vote top", e)
        }
    }

    init
    {
        withPlayer(Messages.MUST_BE_PLAYER.getMessage(plugin))
        withUsage(ChatColor.RED.toString() + "- /createtop <top>")
        withNoPermMessage(Messages.NO_PERMISSION.getMessage(plugin))
    }
}