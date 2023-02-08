package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.overviews.VotePartyChestOverview
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VotePartyChestOverviewShortcut(
    private val plugin: CV,
    private val backPage: GUI
) : BaseItem(
    Material.ENDER_CHEST, PMessage.VOTE_PARTY_ITEM_NAME_OVERVIEW.toString(),
    null, true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        backPage.cancelCloseEvent = true
        VotePartyChestOverview(plugin, backPage).open(player)
    }
}