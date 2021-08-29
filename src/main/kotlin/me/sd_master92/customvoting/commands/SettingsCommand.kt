package me.sd_master92.customvoting.commands

import me.sd_master92.customvoting.Main
import me.sd_master92.plugin.command.SimpleCommand
import me.sd_master92.customvoting.gui.VoteSettings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SettingsCommand(private val plugin: Main) : SimpleCommand(plugin, "votesettings")
{
    override fun onCommand(commandSender: CommandSender, strings: Array<String>)
    {
    }

    override fun onCommand(player: Player, strings: Array<String>)
    {
        val settings = VoteSettings(plugin).inventory
        SoundType.OPEN.play(plugin, player)
        player.openInventory(settings)
    }

    init
    {
        withPlayer()
    }
}