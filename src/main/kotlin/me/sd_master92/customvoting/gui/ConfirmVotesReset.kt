package me.sd_master92.customvoting.gui

import me.sd_master92.core.inventory.ConfirmGUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.tasks.ResetChecker
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent


class ConfirmVotesReset(private val plugin: CV, private val monthly: Boolean) :
    ConfirmGUI(plugin, if (monthly) Strings.RESET_VOTES_MONTHLY.toString() else Strings.RESET_VOTES_ALL.toString())
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
        player.sendMessage(Strings.RESET_VOTES_CANCEL.toString())
        player.openInventory(VoteSettings(plugin).inventory)
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        cancelCloseEvent = true
    }
}