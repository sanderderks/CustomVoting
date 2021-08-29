package me.sd_master92.customvoting.commands

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.plugin.command.SimpleCommand
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.gui.VoteLinks
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VoteCommand(private val plugin: Main) : SimpleCommand(plugin, "vote")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
        if (plugin.config.getBoolean(Settings.VOTE_LINK_INVENTORY))
        {
            player.openInventory(VoteLinks(plugin, true).inventory)
        } else
        {
            for (message in Messages.VOTE_COMMAND.getMessages(plugin))
            {
                player.sendMessage(message)
            }
        }
    }

    init
    {
        withPlayer()
    }
}