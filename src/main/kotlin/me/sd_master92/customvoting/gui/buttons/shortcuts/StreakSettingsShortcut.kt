package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.settings.StreakSettingsPage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class StreakSettingsShortcut(
    private val plugin: CV,
    private val currentPage: GUI,
    private val key: Int
) : BaseItem(
    Material.FIREWORK_ROCKET,
    PMessage.STREAK_ITEM_NAME_X.with("$key")
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        currentPage.cancelCloseEvent = true
        StreakSettingsPage(plugin, currentPage, key).open(player)
    }
}