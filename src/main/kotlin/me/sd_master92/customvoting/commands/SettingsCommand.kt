package me.sd_master92.customvoting.commands

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.plugin.command.SimpleCommand
import me.sd_master92.customvoting.gui.VoteSettings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SettingsCommand(private val plugin: CV) : SimpleCommand(plugin, "votesettings")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
        val settings = VoteSettings(plugin).inventory
        SoundType.OPEN.play(plugin, player)
        player.openInventory(settings)
    }

    init
    {
        withPlayer(Messages.MUST_BE_PLAYER.getMessage(plugin))
        withNoPermMessage(Messages.NO_PERMISSION.getMessage(plugin))
    }
}