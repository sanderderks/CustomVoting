package me.sd_master92.customvoting.gui.pages.dialogs

import me.sd_master92.core.inventory.ConfirmGUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.gui.pages.settings.VoteSettingsPage
import me.sd_master92.customvoting.tasks.ResetChecker
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class ResetVotesDialog(private val plugin: CV, private val monthly: Boolean) :
    ConfirmGUI(
        plugin,
        if (monthly) PMessage.RESET_VOTES_INVENTORY_NAME_MONTHLY.toString() else PMessage.RESET_VOTES_INVENTORY_NAME_ALL.toString(),
        PMessage.GENERAL_VALUE_CONFIRM.toString(),
        PMessage.GENERAL_VALUE_CANCEL.toString()
    )
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
        plugin.broadcastText(Message.MONTHLY_RESET)
        VoteSettingsPage(plugin, null).open(player)
    }

    override fun onCancel(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        ResetChecker.FIRST_OF_MONTH = false
        player.sendMessage(PMessage.RESET_VOTES_MESSAGE_CANCEL.toString())
        VoteSettingsPage(plugin, null).open(player)
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        cancelCloseEvent = true
    }
}