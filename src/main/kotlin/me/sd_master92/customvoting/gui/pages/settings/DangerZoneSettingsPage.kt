package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.MergeDuplicatesAction
import me.sd_master92.customvoting.gui.buttons.inputfields.PrefixSupportInput
import me.sd_master92.customvoting.gui.buttons.switches.DatabaseSwitch
import me.sd_master92.customvoting.gui.buttons.switches.IgnorePlayernameCasingSwitch
import me.sd_master92.customvoting.gui.buttons.switches.UuidSupportSwitch
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class DangerZoneSettingsPage(private val plugin: CV, backPage: GUI?) :
    GUI(plugin, backPage, PMessage.DANGER_ZONE_INVENTORY_NAME.toString())
{
    override fun newInstance(): GUI
    {
        return DangerZoneSettingsPage(plugin, backPage)
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
        addItem(UuidSupportSwitch(plugin))
        addItem(IgnorePlayernameCasingSwitch(plugin))
        addItem(DatabaseSwitch(plugin))
        addItem(MergeDuplicatesAction(plugin))
        addItem(PrefixSupportInput(plugin, this))
    }
}

