package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.ConfirmVotesReset
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ResetVotesCommand(private val plugin: CV) : SimpleCommand(plugin, "votereset")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
        SoundType.OPEN.play(plugin, player)
        ConfirmVotesReset(plugin, plugin.config.getBoolean(Setting.MONTHLY_VOTES.path)).open(player)
    }

    init
    {
        withPlayer(Message.MUST_BE_PLAYER.getMessage(plugin))
        withNoPermMessage(Message.NO_PERMISSION.getMessage(plugin))
    }
}