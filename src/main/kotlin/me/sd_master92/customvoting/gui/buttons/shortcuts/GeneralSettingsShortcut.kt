package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.settings.GeneralSettingsPage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class GeneralSettingsShortcut(
    private val plugin: CV,
    private val currentPage: GUI
) : BaseItem(
    Material.COMMAND_BLOCK, PMessage.SETTINGS_ITEM_NAME_GENERAL.toString(),
    null, true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        currentPage.cancelCloseEvent = true
        GeneralSettingsPage(plugin, currentPage).open(player)
    }
}