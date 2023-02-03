package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.core.file.PlayerFile
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.sendText
import me.sd_master92.customvoting.subjects.CustomVote
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class InspectVoteCommand(private val plugin: CV) : SimpleCommand(plugin, "inspectvote")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
        if (args.isEmpty())
        {
            if (sender is Player)
            {
                testVote(sender.getName(), Strings.FAKE_VOTE_WEBSITE.toString(), sender)
            } else
            {
                sender.sendMessage(Strings.INSPECT_VOTE_COMMAND_USAGE.toString())
            }
        } else
        {
            val name = args[0]
            val service = if (args.size >= 2) args[1] else Strings.FAKE_VOTE_WEBSITE.toString()
            if (PlayerFile.getByName(name) != null)
            {
                testVote(name, service, sender)
            } else
            {
                sender.sendText(plugin, Messages.INVALID_PLAYER)
            }
        }
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
    }

    private fun testVote(name: String, service: String, sender: CommandSender? = null)
    {
        CustomVote.create(plugin, name, service, logger = sender)
    }

    init
    {
        withNoPermMessage(Messages.NO_PERMISSION.getMessage(plugin))
    }
}