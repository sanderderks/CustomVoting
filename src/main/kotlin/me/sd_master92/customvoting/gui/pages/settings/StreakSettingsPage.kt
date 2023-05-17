package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.StreakDeleteAction
import me.sd_master92.customvoting.gui.buttons.inputfields.StreakRewardCommandsInput
import me.sd_master92.customvoting.gui.buttons.inputfields.StreakRewardPermissionsInput
import me.sd_master92.customvoting.gui.buttons.shortcuts.StreakRewardItemsShortcut
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class StreakSettingsPage(private val plugin: CV, backPage: GUI?, private val number: Int) :
    GUI(plugin, backPage, PMessage.STREAK_INVENTORY_NAME_X.with("$number"), 9)
{
    override fun newInstance(): GUI
    {
        return StreakSettingsPage(plugin, backPage, number)
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
        addItem(StreakRewardItemsShortcut(plugin, this, number))
        addItem(StreakRewardCommandsInput(plugin, this, number))
        addItem(StreakRewardPermissionsInput(plugin, this, number))
        setItem(7, StreakDeleteAction(plugin, this, number))
    }
}