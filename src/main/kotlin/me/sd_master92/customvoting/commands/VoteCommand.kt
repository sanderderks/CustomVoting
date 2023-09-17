package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.menus.VoteLinksMenu
import me.sd_master92.customvoting.sendTexts
import me.sd_master92.customvoting.subjects.VoteSite
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VoteCommand(private val plugin: CV) : SimpleCommand(plugin, "vote")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
        SoundType.NOTIFY.play(plugin, player)
        if (plugin.config.getBoolean(Setting.VOTE_LINK_INVENTORY.path))
        {
            VoteLinksMenu(plugin).open(player)
        } else if (plugin.config.getBoolean(Setting.VOTE_COMMAND_OVERRIDE.path))
        {
            player.sendTexts(plugin, Message.VOTE_COMMAND_OVERRIDE)
        } else
        {
            val messages = mutableListOf(
                "",
                Message.VOTE_COMMAND_TITLE.getMessage(plugin),
                Message.VOTE_COMMAND_DIVIDER.getMessage(plugin)
            )
            for (voteSite in VoteSite.getAll(plugin))
            {
                messages.add(voteSite.title)
                messages.add(Message.VOTE_COMMAND_PREFIX.getMessage(plugin, mapOf(Pair("%SERVICE%", voteSite.url))))
            }
            messages.add(Message.VOTE_COMMAND_DIVIDER.getMessage(plugin))

            player.sendTexts(messages)
        }
    }

    init
    {
        withPlayer(Message.MUST_BE_PLAYER.getMessage(plugin))
        withNoPermMessage(Message.NO_PERMISSION.getMessage(plugin))
    }
}