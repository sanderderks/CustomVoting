package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.overviews.PermUserOverviewPage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class PermUserOverviewShortcut(
    private val plugin: CV,
    private val currentPage: GUI
) : BaseItem(
    Material.PLAYER_HEAD,
    PMessage.PERM_USER_OVERVIEW_ITEM_NAME.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        currentPage.cancelCloseEvent = true
        PermUserOverviewPage(plugin, currentPage).open(player)
    }
}