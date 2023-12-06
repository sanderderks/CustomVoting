package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.sendText
import me.sd_master92.customvoting.subjects.CustomVote
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FakeVoteCommand(private val plugin: CV) : SimpleCommand(plugin, "fakevote")
{
    override suspend fun onCommand(sender: CommandSender, args: Array<out String>)
    {
        if (args.isEmpty())
        {
            if (sender is Player)
            {
                fakeVote(sender.getName(), PMessage.FAKE_VOTE_VALUE_WEBSITE.toString())
            } else
            {
                sender.sendMessage(PMessage.FAKE_VOTE_MESSAGE_COMMAND_USAGE.toString())
            }
        } else
        {
            val name = args[0]
            val service = if (args.size >= 2) args[1] else PMessage.FAKE_VOTE_VALUE_WEBSITE.toString()
            if (Voter.getByName(plugin, name) != null)
            {
                fakeVote(name, service)
            } else
            {
                sender.sendText(plugin, Message.INVALID_PLAYER)
            }
        }
    }

    override suspend fun onCommand(player: Player, args: Array<out String>)
    {
    }

    private fun fakeVote(name: String, service: String)
    {
        CustomVote.create(plugin, name, service)
    }

    init
    {
        withNoPermMessage(Message.NO_PERMISSION.getMessage(plugin))
    }
}