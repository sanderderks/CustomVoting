package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.carousel.LuckyChanceCarousel
import me.sd_master92.customvoting.gui.buttons.shortcuts.LuckyRewardItemsShortcut
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class LuckySettingsPage(private val plugin: CV, backPage: GUI?) :
    GUI(plugin, backPage, PMessage.LUCKY_INVENTORY_NAME_SETTINGS.toString(), 9)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    init
    {
        addItem(LuckyRewardItemsShortcut(plugin, this))
        addItem(LuckyChanceCarousel(plugin))
    }
}