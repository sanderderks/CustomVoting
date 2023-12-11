package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.carousel.LuckyChanceCarousel
import me.sd_master92.customvoting.gui.buttons.shortcuts.LuckyRewardItemsShortcut
import me.sd_master92.customvoting.gui.buttons.switches.LuckyVoteEnabledSwitch
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class LuckySettingsPage(private val plugin: CV, backPage: GUI?) :
    GUI(plugin, backPage, PMessage.LUCKY_INVENTORY_NAME_SETTINGS.toString())
{
    override fun newInstance(): GUI
    {
        return LuckySettingsPage(plugin, backPage)
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
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
        addItem(LuckyVoteEnabledSwitch(plugin))
        addItem(LuckyRewardItemsShortcut(plugin, this))
        addItem(LuckyChanceCarousel(plugin))
    }
}