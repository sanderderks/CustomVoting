package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.VoteSortType
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.sendText
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VotesCommand(private val plugin: CV) : SimpleCommand(plugin, "votes")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
        if (args.isEmpty())
        {
            if (sender is Player)
            {
                val placeholders = HashMap<String, String>()
                val topVoter = Voter.get(plugin, sender)
                placeholders["%VOTES%"] = "${topVoter.votes}"
                placeholders["%VOTES_MONTHLY%"] = "${topVoter.votesMonthly}"
                placeholders["%VOTES_WEEKLY%"] = "${topVoter.votesWeekly}"
                placeholders["%VOTES_DAILY%"] = "${topVoter.votesDaily}"
                when (VoteSortType.valueOf(plugin.config.getNumber(Setting.VOTES_SORT_TYPE.path)))
                {
                    VoteSortType.ALL     -> placeholders["%s%"] = if (topVoter.votes == 1) "" else "s"
                    VoteSortType.MONTHLY -> placeholders["%s%"] = if (topVoter.votesMonthly == 1) "" else "s"
                    VoteSortType.WEEKLY  -> placeholders["%s%"] = if (topVoter.votesWeekly == 1) "" else "s"
                    VoteSortType.DAILY   -> placeholders["%s%"] = if (topVoter.votesDaily == 1) "" else "s"
                }
                sender.sendText(plugin, Message.VOTES_COMMAND_SELF, placeholders)
            }
        } else
        {
            val name = args[0]
            val topVoter = Voter.getByName(plugin, name)
            if (topVoter != null)
            {
                val placeholders = HashMap<String, String>()
                placeholders["%PLAYER%"] = topVoter.name
                placeholders["%VOTES%"] = "${topVoter.votes}"
                placeholders["%VOTES_MONTHLY%"] = "${topVoter.votesMonthly}"
                placeholders["%VOTES_WEEKLY%"] = "${topVoter.votesWeekly}"
                placeholders["%VOTES_DAILY%"] = "${topVoter.votesDaily}"
                when (VoteSortType.valueOf(plugin.config.getNumber(Setting.VOTES_SORT_TYPE.path)))
                {
                    VoteSortType.ALL     -> placeholders["%s%"] = if (topVoter.votes == 1) "" else "s"
                    VoteSortType.MONTHLY -> placeholders["%s%"] = if (topVoter.votesMonthly == 1) "" else "s"
                    VoteSortType.WEEKLY  -> placeholders["%s%"] = if (topVoter.votesWeekly == 1) "" else "s"
                    VoteSortType.DAILY   -> placeholders["%s%"] = if (topVoter.votesDaily == 1) "" else "s"
                }
                sender.sendText(plugin, Message.VOTES_COMMAND_OTHERS, placeholders)
            } else
            {
                sender.sendText(plugin, Message.VOTES_COMMAND_NOT_FOUND)
            }
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