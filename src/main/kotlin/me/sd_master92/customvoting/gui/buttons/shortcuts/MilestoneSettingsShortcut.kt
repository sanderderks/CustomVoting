package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.settings.MilestoneSettingsPage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class MilestoneSettingsShortcut(
    private val plugin: CV,
    private val currentPage: GUI,
    private val key: Int
) : BaseItem(
    Material.ENDER_PEARL,
    PMessage.MILESTONE_ITEM_NAME_X.with("$key")
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        currentPage.cancelCloseEvent = true
        MilestoneSettingsPage(plugin, currentPage, key).open(player)
    }
}