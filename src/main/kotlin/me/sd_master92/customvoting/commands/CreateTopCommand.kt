package me.sd_master92.customvoting.commands

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.subjects.VoteTopStand
import me.sd_master92.plugin.command.SimpleCommand
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CreateTopCommand(private val plugin: Main) : SimpleCommand(plugin, "createtop", false)
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
                player.sendMessage(ChatColor.RED.toString() + "Invalid argument: 'top' must be a positive number.")
            }
        } catch (e: Exception)
        {
            player.sendMessage(ChatColor.RED.toString() + "Invalid argument: 'top' must be a number.")
        }
    }

    init
    {
        withPlayer()
        withUsage(ChatColor.RED.toString() + "- /createtop <top>")
    }
}