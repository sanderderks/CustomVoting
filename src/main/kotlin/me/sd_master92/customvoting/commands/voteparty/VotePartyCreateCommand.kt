package me.sd_master92.customvoting.commands.voteparty

import me.sd_master92.core.command.SimpleSubCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.gui.pages.overviews.VotePartyChestOverview
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VotePartyCreateCommand(private val plugin: CV) : SimpleSubCommand("create")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
        VotePartyChestOverview(plugin, null).open(player)
    }

    init
    {
        withPlayer()
        withPermission("voteparty.create")
    }
}