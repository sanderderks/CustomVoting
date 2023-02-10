package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.StreakCreateAction
import me.sd_master92.customvoting.gui.buttons.shortcuts.StreakSettingsShortcut
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class StreakOverviewPage(private val plugin: CV, backPage: GUI?) :
    GUI(
        plugin,
        backPage,
        PMessage.STREAK_INVENTORY_NAME_OVERVIEW.toString(),
        calculateInventorySize(plugin)
    )
{
    override fun newInstance(): GUI
    {
        return StreakOverviewPage(plugin, backPage)
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

    companion object
    {
        private fun calculateInventorySize(plugin: CV): Int
        {
            val streaks = (plugin.data.getConfigurationSection(Data.STREAKS.path)?.getKeys(false)?.size ?: 0) + 2
            return if (streaks % 9 == 0)
            {
                streaks
            } else
            {
                streaks + (9 - (streaks % 9))
            }
        }
    }

    init
    {
        setItem(calculateInventorySize(plugin) - 2, StreakCreateAction(plugin, this))
        for (key in plugin.data.getConfigurationSection(Data.STREAKS.path)?.getKeys(false)?.mapNotNull { key ->
            key.toIntOrNull()
        }?.sorted() ?: listOf())
        {
            addItem(StreakSettingsShortcut(plugin, this, key))
        }
    }
}