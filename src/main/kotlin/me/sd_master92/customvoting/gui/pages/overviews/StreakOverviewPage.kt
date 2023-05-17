package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.PaginationNextAction
import me.sd_master92.customvoting.gui.buttons.actions.PaginationPreviousAction
import me.sd_master92.customvoting.gui.buttons.actions.StreakCreateAction
import me.sd_master92.customvoting.gui.buttons.shortcuts.StreakSettingsShortcut
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class StreakOverviewPage(private val plugin: CV, backPage: GUI?, private val page: Int = 0) :
    GUI(
        plugin,
        backPage,
        PMessage.STREAK_INVENTORY_NAME_OVERVIEW.toString() + " #${page + 1}",
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
            val streaks = (plugin.data.getConfigurationSection(Data.STREAKS.path)?.getKeys(false)?.size ?: 0) + 4
            val size = if (streaks % 9 == 0) streaks else streaks + (9 - (streaks % 9))
            return size.coerceAtMost(54)
        }
    }

    init
    {
        setItem(nonClickableSizeWithNull - 1, StreakCreateAction(plugin, this))
        setItem(
            nonClickableSizeWithNull - 1,
            object : PaginationNextAction(plugin, this, page)
            {
                override fun onNext(player: Player, newPage: Int)
                {
                    StreakOverviewPage(plugin, backPage, newPage).open(player)
                }
            })
        setItem(nonClickableSizeWithNull - 1, object : PaginationPreviousAction(plugin, this, page)
        {
            override fun onPrevious(player: Player, newPage: Int)
            {
                StreakOverviewPage(plugin, backPage, newPage).open(player)
            }
        })
        val start = page * nonClickableSizeWithNull
        val streaks = plugin.data.getConfigurationSection(Data.STREAKS.path)
            ?.getKeys(false)
            ?.mapNotNull { it.toIntOrNull() }
            ?.sorted()
            ?.drop(start)
            ?.take(nonClickableSizeWithNull)
            ?: emptyList()
        for (key in streaks)
        {
            addItem(StreakSettingsShortcut(plugin, this, key))
        }
    }
}