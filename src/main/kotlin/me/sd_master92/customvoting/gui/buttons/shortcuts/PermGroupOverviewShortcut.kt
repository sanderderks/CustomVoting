package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.overviews.PermGroupOverviewPage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class PermGroupOverviewShortcut(
    private val plugin: CV,
    private val currentPage: GUI,
    name: String
) : BaseItem(
    Material.BELL,
    PMessage.PERM_GROUP_OVERVIEW_ITEM_NAME.toString(),
    PMessage.PERM_GROUP_OVERVIEW_ITEM_LORE_X.with(name)
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        currentPage.cancelCloseEvent = true
        PermGroupOverviewPage(plugin, currentPage).open(player)
    }
}