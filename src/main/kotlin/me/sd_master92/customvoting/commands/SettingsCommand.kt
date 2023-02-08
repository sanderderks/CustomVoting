package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.dialogs.ResetVotesDialog
import me.sd_master92.customvoting.gui.pages.settings.VoteSettingsPage
import me.sd_master92.customvoting.tasks.ResetChecker
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
        if (ResetChecker.FIRST_OF_MONTH && plugin.config.getBoolean(Setting.MONTHLY_RESET.path))
        {

            ResetVotesDialog(
                plugin,
                plugin.config.getBoolean(Setting.MONTHLY_VOTES.path)
            ).open(player)
        } else
        {
            VoteSettingsPage(plugin, null).open(player)
        }
    }

    init
    {
        withPlayer(Message.MUST_BE_PLAYER.getMessage(plugin))
        withNoPermMessage(Message.NO_PERMISSION.getMessage(plugin))
    }
}