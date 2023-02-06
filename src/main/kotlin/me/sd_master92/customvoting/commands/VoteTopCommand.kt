package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.sendText
import me.sd_master92.customvoting.sendTexts
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.stream.Collectors

class VoteTopCommand(private val plugin: CV) : SimpleCommand(plugin, "votetop")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
        val topVoters = Voter.getTopVoters(plugin)
        if (topVoters.isNotEmpty())
        {
            val messages: MutableList<String> = ArrayList()
            for (message in Message.VOTE_TOP_COMMAND_FORMAT.getMessages(plugin))
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
                        placeholders["%MONTHLY_VOTES%"] = "${topVoter.monthlyVotes}"
                        if (plugin.config.getBoolean(Setting.MONTHLY_VOTES.path))
                        {
                            placeholders["%s%"] = if (topVoter.monthlyVotes == 1) "" else "s"
                        } else
                        {
                            placeholders["%s%"] = if (topVoter.votes == 1) "" else "s"
                        }
                        messages.add(Message.VOTE_TOP_COMMAND_PLAYERS.getMessage(plugin, placeholders))
                    }
                }
            }
            sender.sendTexts(messages)
        } else
        {
            sender.sendText(plugin, Message.VOTE_TOP_COMMAND_NOT_FOUND)
        }
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
    }

    init
    {
        withNoPermMessage(Message.NO_PERMISSION.getMessage(plugin))
    }
}