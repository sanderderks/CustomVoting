package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.overviews.DonatorOverviewPage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class DonatorOverviewShortcut(private val plugin: CV, private val gui: GUI) : BaseItem(
    Material.CREEPER_HEAD, PMessage.SUPPORT_ITEM_NAME_DONATORS.toString(),
    PMessage.SUPPORT_ITEM_LORE_DONATORS.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        gui.cancelCloseEvent = true
        DonatorOverviewPage(plugin).open(player)
    }
}