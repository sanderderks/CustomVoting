package me.sd_master92.customvoting.commands

import me.sd_master92.customfile.PlayerFile
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.subjects.CitizenStand
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.VoteTopStand
import me.sd_master92.plugin.command.SimpleCommand
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReloadCommand(private val plugin: CV) : SimpleCommand(plugin, "votereload")
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
        fun reload(plugin: CV): Boolean
        {
            if (plugin.config.reloadConfig() && plugin.data.reloadConfig() && plugin.messages.reloadConfig())
            {
                if (PlayerFile.getAll().values.stream().allMatch { obj: PlayerFile -> obj.reloadConfig() })
                {
                    VoteTopSign.updateAll(plugin)
                    VoteTopStand.updateAll(plugin)
                    CitizenStand.updateAll(plugin)
                    return true
                }
            }
            return false
        }
    }

    init
    {
        withNoPermMessage(Messages.NO_PERMISSION.getMessage(plugin))
    }
}