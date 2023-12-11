package me.sd_master92.customvoting.commands.voteparty

import me.sd_master92.core.command.SimpleSubCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.subjects.voteparty.VoteParty
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VotePartyStopCommand(private val plugin: CV) : SimpleSubCommand("stop")
{
    override suspend fun onCommand(sender: CommandSender, args: Array<out String>)
    {
        VoteParty.stop(plugin)
    }

    override suspend fun onCommand(player: Player, args: Array<out String>)
    {
    }
}