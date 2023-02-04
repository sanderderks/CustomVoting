package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.gui.messages.VoteLinks
import me.sd_master92.customvoting.sendTexts
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VoteCommand(private val plugin: CV) : SimpleCommand(plugin, "vote")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
        if (plugin.config.getBoolean(Settings.VOTE_LINK_INVENTORY.path))
        {
            player.openInventory(VoteLinks(plugin, true).inventory)
        } else
        {
            player.sendTexts(plugin, Message.VOTE_COMMAND)
        }
    }

    init
    {
        withPlayer(Message.MUST_BE_PLAYER.getMessage(plugin))
        withNoPermMessage(Message.NO_PERMISSION.getMessage(plugin))
    }
}