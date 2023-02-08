package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.DiscordGetLinkAction
import me.sd_master92.customvoting.gui.buttons.actions.MergeDuplicatesAction
import me.sd_master92.customvoting.gui.buttons.actions.PluginUpdateAction
import me.sd_master92.customvoting.gui.buttons.shortcuts.DonatorOverviewShortcut
import me.sd_master92.customvoting.gui.buttons.shortcuts.PlayerInfoOverviewShortcut
import me.sd_master92.customvoting.gui.buttons.shortcuts.StatisticOverviewShortcut
import me.sd_master92.customvoting.gui.buttons.switches.DatabaseSwitch
import me.sd_master92.customvoting.gui.buttons.switches.IngameUpdateSwitch
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class SupportSettingsPage(private val plugin: CV, backPage: GUI?) :
    GUI(plugin, backPage, PMessage.SUPPORT_INVENTORY_NAME.toString(), 9)
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
        addItem(PluginUpdateAction(plugin, this))
        addItem(IngameUpdateSwitch(plugin))
        addItem(DiscordGetLinkAction(plugin, this))
        addItem(DatabaseSwitch(plugin))
        addItem(DonatorOverviewShortcut(plugin, this))
        addItem(PlayerInfoOverviewShortcut(plugin, this))
        addItem(MergeDuplicatesAction(plugin))
        addItem(StatisticOverviewShortcut(plugin, this))
    }
}