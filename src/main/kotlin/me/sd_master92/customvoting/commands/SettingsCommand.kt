package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.ConfirmVotesReset
import me.sd_master92.customvoting.gui.VoteSettings
import me.sd_master92.customvoting.tasks.DailyTask
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SettingsCommand(private val plugin: CV) : SimpleCommand(plugin, "votesettings")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
        SoundType.OPEN.play(plugin, player)
        if (DailyTask.FIRST_OF_MONTH)
        {
            player.openInventory(ConfirmVotesReset(plugin, plugin.config.getBoolean("monthly_period")).inventory)
        } else
        {
            player.openInventory(VoteSettings(plugin).inventory)
        }
    }

    init
    {
        withPlayer(Messages.MUST_BE_PLAYER.getMessage(plugin))
        withNoPermMessage(Messages.NO_PERMISSION.getMessage(plugin))
    }
}