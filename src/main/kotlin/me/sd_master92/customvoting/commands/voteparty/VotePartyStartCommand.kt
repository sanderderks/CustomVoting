package me.sd_master92.customvoting.commands.voteparty

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.plugin.command.SimpleSubCommand
import me.sd_master92.customvoting.subjects.VoteParty
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VotePartyStartCommand(private val plugin: Main) : SimpleSubCommand("start")
{
    override fun onCommand(sender: CommandSender, strings: Array<String>)
    {
        if (plugin.data.getLocations(Data.VOTE_PARTY).isNotEmpty())
        {
            VoteParty(plugin).start()
        } else
        {
            sender.sendMessage(ChatColor.RED.toString() + "There are no registered Vote Party Chests.")
        }
    }

    override fun onCommand(player: Player, strings: Array<String>)
    {
    }
}