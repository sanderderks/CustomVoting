package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.overviews.CrateOverviewPage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class CrateOverviewShortcut(private val plugin: CV, private val currentPage: GUI) : BaseItem(
    Material.TRIPWIRE_HOOK,
    PMessage.CRATE_ITEM_NAME_OVERVIEW.toString(),
    PMessage.CRATE_ITEM_LORE_OVERVIEW.toString(),
    true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        currentPage.cancelCloseEvent = true
        CrateOverviewPage(plugin, currentPage).open(player)
    }
}