package me.sd_master92.customvoting.gui

import me.sd_master92.core.file.PlayerFile.Companion.getAll
import me.sd_master92.core.inventory.ConfirmGUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.database.PlayerRow
import me.sd_master92.customvoting.tasks.ResetChecker
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import kotlin.jvm.internal.Intrinsics


class ConfirmVotesReset(private val plugin: CV, private val period: Boolean) :
    ConfirmGUI(plugin, if (period) "Reset monthly votes?" else "Reset ALL votes?")
{
    override fun onConfirm(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        for (value in getAll().values)
        {
            if (plugin.hasDatabaseConnection())
            {
                if (period)
                {
                    PlayerRow(plugin, value.uuid).clearPeriod()
                } else
                {
                    PlayerRow(plugin, value.uuid).setVotes(0, true)
                }
            }
            if (period)
            {
                VoteFile(value.uuid, plugin).clearPeriod()
            } else
            {
                VoteFile(value.uuid, plugin).setVotes(0, true)
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
        cancelCloseEvent()
    }
}