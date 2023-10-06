package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.PaginationNextAction
import me.sd_master92.customvoting.gui.buttons.actions.PaginationPreviousAction
import me.sd_master92.customvoting.gui.buttons.actions.VotePartyCreateAction
import me.sd_master92.customvoting.gui.buttons.shortcuts.VotePartyRewardItemsShortcut
import me.sd_master92.customvoting.subjects.voteparty.VotePartyChest
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class VotePartyChestOverview(private val plugin: CV, backPage: GUI?, private val page: Int = 0) :
    GUI(
        plugin,
        backPage,
        PMessage.VOTE_PARTY_INVENTORY_NAME_CHEST_OVERVIEW.toString() + " #${page + 1}",
        calculateInventorySize(plugin, backPage)
    )
{
    override fun newInstance(): GUI
    {
        return VotePartyChestOverview(plugin, backPage)
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    companion object
    {
        private fun calculateInventorySize(plugin: CV, backPage: GUI?): Int
        {
            val chests = VotePartyChest.getAll(plugin).size + if(backPage != null) 4 else 3
            val size = if (chests % 9 == 0) chests else chests + (9 - (chests % 9))
            return size.coerceAtMost(54)
        }
    }

    init
    {
        setItem(nonClickableSizeWithNull - 1, VotePartyCreateAction(plugin, this))
        setItem(
            nonClickableSizeWithNull - 1,
            object : PaginationNextAction(plugin, this, page)
            {
                override fun onNext(player: Player, newPage: Int)
                {
                    VotePartyChestOverview(plugin, backPage, newPage).open(player)
                }
            })
        setItem(nonClickableSizeWithNull - 1, object : PaginationPreviousAction(plugin, this, page)
        {
            override fun onPrevious(player: Player, newPage: Int)
            {
                VotePartyChestOverview(plugin, backPage, newPage).open(player)
            }
        })
        val start = page * nonClickableSizeWithNull
        val chests = VotePartyChest.getAll(plugin).map { it.key }
            .asSequence()
            .mapNotNull { it.toIntOrNull() }
            .sorted()
            .drop(start)
            .take(nonClickableSizeWithNull)
        for (key in chests)
        {
            addItem(VotePartyRewardItemsShortcut(plugin, this, "$key"))
        }
    }
}