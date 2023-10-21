package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.inventory.GUIWithPagination
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.MilestoneCreateAction
import me.sd_master92.customvoting.gui.buttons.shortcuts.MilestoneSettingsShortcut
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class MilestoneOverviewPage(private val plugin: CV, backPage: GUI?, private val page: Int = 0) :
    GUIWithPagination<Int>(
        plugin,
        backPage,
        plugin.data.getConfigurationSection(Data.MILESTONES.path)
                ?.getKeys(false)
                ?.mapNotNull { it.toIntOrNull() }
                ?.sorted()
            ?: emptyList(),
        { context, item -> MilestoneSettingsShortcut(plugin, context, item) },
        page,
        PMessage.MILESTONE_INVENTORY_NAME_OVERVIEW.toString(),
        PMessage.GENERAL_ITEM_NAME_NEXT.toString(),
        PMessage.GENERAL_ITEM_NAME_PREVIOUS.toString(),
        1
    )
{
    override fun newInstance(): GUI
    {
        return newInstance(page)
    }

    fun newInstance(page: Int): GUI
    {
        return MilestoneOverviewPage(plugin, backPage, page)
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

    override fun open(player: Player, page: Int)
    {
        SoundType.CLICK.play(plugin, player)
        newInstance(page).open(player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    init
    {
        addItem(MilestoneCreateAction(plugin, this), true)
    }
}