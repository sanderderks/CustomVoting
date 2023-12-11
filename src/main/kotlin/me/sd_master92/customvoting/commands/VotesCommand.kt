package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.getVotesPlaceholders
import me.sd_master92.customvoting.gui.pages.menus.VoteInfoMenu
import me.sd_master92.customvoting.sendText
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VotesCommand(private val plugin: CV) : SimpleCommand(plugin, "votes")
{
    override suspend fun onCommand(sender: CommandSender, args: Array<out String>)
    {
        if (args.isEmpty())
        {
            getVotes(sender)
        } else
        {
            val name = args[0]
            val topVoter = Voter.getByName(plugin, name)
            if (topVoter != null)
            {
                getVotes(sender, topVoter)
            } else
            {
                sender.sendText(plugin, Message.VOTES_COMMAND_NOT_FOUND)
            }
        }
    }

    private suspend fun getVotes(sender: CommandSender, voter: Voter? = null)
    {
        if (voter != null)
        {
            if (plugin.config.getBoolean(Setting.VOTE_INFO_INVENTORY.path) && sender is Player)
            {
                SoundType.OPEN.play(plugin, sender)
                VoteInfoMenu(plugin, voter, voter.getName()).open(sender)
            } else
            {
                sender.sendText(plugin, Message.VOTES_COMMAND_OTHERS, voter.getVotesPlaceholders(plugin))
            }
        } else if (sender is Player)
        {
            val self = Voter.get(plugin, sender)
            if (plugin.config.getBoolean(Setting.VOTE_INFO_INVENTORY.path))
            {
                SoundType.OPEN.play(plugin, sender)
                VoteInfoMenu(plugin, self).open(sender)
            } else
            {
                sender.sendText(plugin, Message.VOTES_COMMAND_SELF, self.getVotesPlaceholders(plugin))
            }
        }
    }

    override suspend fun onCommand(player: Player, args: Array<out String>)
    {
    }

    init
    {
        withNoPermMessage(Message.NO_PERMISSION.getMessage(plugin))
    }
}