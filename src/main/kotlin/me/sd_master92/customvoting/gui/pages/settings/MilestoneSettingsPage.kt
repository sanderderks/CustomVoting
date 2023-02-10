package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.MilestoneDeleteAction
import me.sd_master92.customvoting.gui.buttons.inputfields.MilestoneRewardCommandsInput
import me.sd_master92.customvoting.gui.buttons.inputfields.MilestoneRewardPermissionsInput
import me.sd_master92.customvoting.gui.buttons.shortcuts.MilestoneRewardItemsShortcut
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class MilestoneSettingsPage(private val plugin: CV, backPage: GUI?, private val number: Int) :
    GUI(plugin, backPage, PMessage.MILESTONE_INVENTORY_NAME_X.with("$number"), 9)
{
    override fun newInstance(): GUI
    {
        return MilestoneSettingsPage(plugin, backPage, number)
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
        addItem(MilestoneRewardItemsShortcut(plugin, this, number))
        addItem(MilestoneRewardCommandsInput(plugin, this, number))
        addItem(MilestoneRewardPermissionsInput(plugin, this, number))
        setItem(7, MilestoneDeleteAction(plugin, this, number))
    }
}