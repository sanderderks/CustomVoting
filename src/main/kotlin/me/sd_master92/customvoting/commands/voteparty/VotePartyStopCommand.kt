package me.sd_master92.customvoting.commands.voteparty

import me.sd_master92.core.command.SimpleSubCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.subjects.voteparty.VoteParty
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VotePartyStopCommand(private val plugin: CV) : SimpleSubCommand("stop")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
        VoteParty.stop(plugin)
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
    }
}