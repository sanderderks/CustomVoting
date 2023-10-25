package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.inventory.GUIWithPagination
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.VotePartyCreateAction
import me.sd_master92.customvoting.gui.buttons.shortcuts.VotePartyRewardItemsShortcut
import me.sd_master92.customvoting.subjects.voteparty.VotePartyChest
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class VotePartyChestOverview(private val plugin: CV, backPage: GUI?, private val page: Int = 0) :
    GUIWithPagination<VotePartyChest>(
        plugin,
        backPage,
        VotePartyChest.getAll(plugin),
        { it.key.toIntOrNull() },
        { context, item, _ -> VotePartyRewardItemsShortcut(plugin, context, item.key) },
        page,
        PMessage.VOTE_PARTY_INVENTORY_NAME_CHEST_OVERVIEW.toString(),
        PMessage.GENERAL_ITEM_NAME_NEXT.toString(),
        PMessage.GENERAL_ITEM_NAME_PREVIOUS.toString(),
        { listOf(VotePartyCreateAction(plugin, it)) }
    )
{
    override fun newInstance(page: Int): GUI
    {
        return VotePartyChestOverview(plugin, backPage?.newInstance(), page)
    }

    override fun newInstance(): GUI
    {
        return newInstance(page)
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

    override fun onPaginate(player: Player, page: Int)
    {
        SoundType.CLICK.play(plugin, player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }
}