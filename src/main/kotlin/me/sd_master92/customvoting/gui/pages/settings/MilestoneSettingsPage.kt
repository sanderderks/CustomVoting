package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.MilestoneDeleteAction
import me.sd_master92.customvoting.gui.buttons.editors.MilestoneRewardCommandsEditor
import me.sd_master92.customvoting.gui.buttons.editors.MilestoneRewardPermissionsEditor
import me.sd_master92.customvoting.gui.buttons.shortcuts.MilestoneRewardItemsShortcut
import me.sd_master92.customvoting.gui.pages.overviews.MilestoneOverviewPage
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class MilestoneSettingsPage(private val plugin: CV, private val number: Int) :
    GUI(plugin, PMessage.MILESTONE_INVENTORY_NAME_X.with("$number"), 9)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        MilestoneOverviewPage(plugin).open(player)
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
        addItem(MilestoneRewardCommandsEditor(plugin, this, number))
        addItem(MilestoneRewardPermissionsEditor(plugin, this, number))
        setItem(7, MilestoneDeleteAction(plugin, this, number))
    }
}