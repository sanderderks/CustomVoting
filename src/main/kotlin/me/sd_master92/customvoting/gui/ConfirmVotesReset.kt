package me.sd_master92.customvoting.gui

import me.sd_master92.core.inventory.ConfirmGUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.tasks.ResetChecker
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import kotlin.jvm.internal.Intrinsics


class ConfirmVotesReset(private val plugin: CV, private val monthly: Boolean) :
    ConfirmGUI(plugin, if (monthly) "Reset monthly votes?" else "Reset ALL votes?")
{
    override fun onConfirm(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        for (voter in Voter.getTopVoters(plugin))
        {
            if (monthly)
            {
                voter.clearMonthlyVotes()
            } else
            {
                voter.setVotes(0, true)
            }
        }
        ResetChecker.FIRST_OF_MONTH = false
        plugin.broadcastText(Messages.MONTHLY_RESET)
        player.openInventory(VoteSettings(plugin).inventory)
    }

    override fun onCancel(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        ResetChecker.FIRST_OF_MONTH = false
        player.sendMessage(Intrinsics.stringPlus(ChatColor.RED.toString(), "Votes are not reset."))
        player.openInventory(VoteSettings(plugin).inventory)
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        cancelCloseEvent = true
    }
}