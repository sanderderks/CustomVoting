package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.MilestoneCreateAction
import me.sd_master92.customvoting.gui.buttons.shortcuts.MilestoneSettingsShortcut
import me.sd_master92.customvoting.gui.pages.settings.RewardSettingsPage
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class MilestoneOverviewPage(private val plugin: CV) :
    GUI(plugin, PMessage.MILESTONE_INVENTORY_NAME_OVERVIEW.toString(), calculateInventorySize(plugin))
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        RewardSettingsPage(plugin).open(player)
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

    companion object
    {
        private fun calculateInventorySize(plugin: CV): Int
        {
            val milestones = (plugin.data.getConfigurationSection(Data.MILESTONES.path)?.getKeys(false)?.size ?: 0) + 2
            return if (milestones % 9 == 0)
            {
                milestones
            } else
            {
                milestones + (9 - (milestones % 9))
            }
        }
    }

    init
    {
        setItem(calculateInventorySize(plugin) - 2, MilestoneCreateAction(plugin, this))
        for (key in plugin.data.getConfigurationSection(Data.MILESTONES.path)?.getKeys(false)?.mapNotNull { key ->
            key.toIntOrNull()
        }?.sorted() ?: listOf())
        {
            addItem(MilestoneSettingsShortcut(plugin, this, key))
        }
    }
}