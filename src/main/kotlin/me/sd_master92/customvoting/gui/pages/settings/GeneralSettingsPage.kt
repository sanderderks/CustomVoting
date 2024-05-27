package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.carousel.LanguageCarousel
import me.sd_master92.customvoting.gui.buttons.carousel.VoteDelayCarousel
import me.sd_master92.customvoting.gui.buttons.carousel.VotesSortTypeCarousel
import me.sd_master92.customvoting.gui.buttons.carousel.WorldExclusionTypeCarousel
import me.sd_master92.customvoting.gui.buttons.shortcuts.DangerZoneSettingsShortcut
import me.sd_master92.customvoting.gui.buttons.shortcuts.WorldDisabledOverviewShortcut
import me.sd_master92.customvoting.gui.buttons.switches.FireworkSwitch
import me.sd_master92.customvoting.gui.buttons.switches.SoundEffectsSwitch
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class GeneralSettingsPage(private val plugin: CV, backPage: GUI?) :
    GUI(plugin, backPage, PMessage.GENERAL_SETTINGS_INVENTORY_NAME.toString())
{
    override fun newInstance(): GUI
    {
        return GeneralSettingsPage(plugin, backPage)
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
        addItem(LanguageCarousel(plugin, this))
        addItem(VotesSortTypeCarousel(plugin))
        addItem(SoundEffectsSwitch(plugin))
        addItem(FireworkSwitch(plugin))
        addItem(WorldDisabledOverviewShortcut(plugin, this))
        addItem(WorldExclusionTypeCarousel(plugin, this))
        addItem(DangerZoneSettingsShortcut(plugin, this))
        addItem(VoteDelayCarousel(plugin))
    }
}

