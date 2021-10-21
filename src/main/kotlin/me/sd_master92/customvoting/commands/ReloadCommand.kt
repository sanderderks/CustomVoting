package me.sd_master92.customvoting.commands

import me.sd_master92.plugin.command.SimpleCommand
import me.sd_master92.customfile.PlayerFile
import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.VoteTopStand
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReloadCommand(private val plugin: Main) : SimpleCommand(plugin, "votereload")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
        if (reload(plugin))
        {
            sender.sendMessage(ChatColor.GREEN.toString() + "Configuration files reloaded!")
        } else
        {
            sender.sendMessage(ChatColor.RED.toString() + "Could not reload configuration files!")
        }
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
    }

    companion object
    {
        fun reload(plugin: Main): Boolean
        {
            if (plugin.config.reloadConfig() && plugin.data.reloadConfig() && plugin.messages.reloadConfig())
            {
                if (PlayerFile.getAll(plugin).stream().allMatch { obj: PlayerFile -> obj.reloadConfig() })
                {
                    VoteTopSign.updateAll(plugin)
                    VoteTopStand.updateAll(plugin)
                    return true
                }
            }
            return false
        }
    }
}