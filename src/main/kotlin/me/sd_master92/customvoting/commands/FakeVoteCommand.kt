package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.core.file.PlayerFile
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.sendText
import me.sd_master92.customvoting.subjects.CustomVote
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FakeVoteCommand(private val plugin: CV) : SimpleCommand(plugin, "fakevote")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
        if (args.isEmpty())
        {
            if (sender is Player)
            {
                fakeVote(sender.getName())
            } else
            {
                sender.sendMessage(ChatColor.RED.toString() + "- /fakevote <name>")
            }
        } else
        {
            val name = args[0]
            if (PlayerFile.getByName(name) != null)
            {
                fakeVote(args[0])
            } else
            {
                sender.sendText(plugin, Messages.INVALID_PLAYER)
            }
        }
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
    }

    private fun fakeVote(name: String)
    {
        CustomVote.create(plugin, name, "fakevote.com")
        withNoPermMessage(Messages.NO_PERMISSION.getMessage(plugin))
    }
}