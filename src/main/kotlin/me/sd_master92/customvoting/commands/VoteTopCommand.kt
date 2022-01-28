package me.sd_master92.customvoting.commands

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.database.PlayerTable
import me.sd_master92.customvoting.sendText
import me.sd_master92.customvoting.sendTexts
import me.sd_master92.plugin.command.SimpleCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.stream.Collectors

class VoteTopCommand(private val plugin: CV) : SimpleCommand(plugin, "votetop")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
        val topVoters =
                if (plugin.hasDatabaseConnection()) PlayerTable.getTopVoters(plugin) else VoteFile.getTopVoters(plugin)
        if (topVoters.isNotEmpty())
        {
            val messages: MutableList<String> = ArrayList()
            for (message in Messages.VOTE_TOP_COMMAND_FORMAT.getMessages(plugin))
            {
                if (!message.contains("%PLAYERS%"))
                {
                    messages.add(message)
                } else
                {
                    val placeholders: MutableMap<String, String> = HashMap()
                    for (topVoter in topVoters.stream()
                            .limit(5)
                            .collect(Collectors.toList()))
                    {
                        placeholders["%PLAYER%"] = topVoter.name
                        placeholders["%VOTES%"] = "${topVoter.votes}"
                        placeholders["%PERIOD%"] = "${topVoter.period}"
                        if (plugin.config.getBoolean(Settings.MONTHLY_PERIOD))
                        {
                            placeholders["%s%"] = if (topVoter.period == 1) "" else "s"
                        } else
                        {
                            placeholders["%s%"] = if (topVoter.votes == 1) "" else "s"
                        }
                        messages.add(Messages.VOTE_TOP_COMMAND_PLAYERS.getMessage(plugin, placeholders))
                    }
                }
            }
            sender.sendTexts(messages)
        } else
        {
            sender.sendText(plugin, Messages.VOTE_TOP_COMMAND_NOT_FOUND)
        }
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
    }

    init
    {
        withNoPermMessage(Messages.NO_PERMISSION.getMessage(plugin))
    }
}