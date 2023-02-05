package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.VMaterial
import me.sd_master92.customvoting.gui.pages.settings.SupportPage
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class SupportSettingsShortcut(private val plugin: CV, private val gui: GUI) : BaseItem(
    VMaterial.SPYGLASS.get(), PMessage.SETTINGS_ITEM_NAME_SUPPORT.toString(),
    null, true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        gui.cancelCloseEvent = true
        SupportPage(plugin).open(player)
    }
}