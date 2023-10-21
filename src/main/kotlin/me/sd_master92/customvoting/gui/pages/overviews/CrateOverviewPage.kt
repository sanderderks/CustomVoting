package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.CrateCreateAction
import me.sd_master92.customvoting.gui.buttons.actions.PaginationNextAction
import me.sd_master92.customvoting.gui.buttons.actions.PaginationPreviousAction
import me.sd_master92.customvoting.gui.buttons.shortcuts.CrateSettingsShortcut
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class CrateOverviewPage(private val plugin: CV, backPage: GUI?, private val page: Int = 0) :
    GUI(
        plugin,
        backPage,
        PMessage.CRATE_INVENTORY_NAME_OVERVIEW.toString() + " #${page + 1}",
        { calculateInventorySize(plugin) }
    )
{
    override fun newInstance(): GUI
    {
        return CrateOverviewPage(plugin, backPage, page)
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
            val crates = (plugin.data.getConfigurationSection(Data.VOTE_CRATES.path)?.getKeys(false)?.size ?: 0) + 4
            val size = if (crates % 9 == 0) crates else crates + (9 - (crates % 9))
            return size.coerceAtMost(54)
        }
    }

    init
    {
        setItem(nonClickableSizeWithNull - 1, CrateCreateAction(plugin, this))
        setItem(
            nonClickableSizeWithNull - 1,
            object : PaginationNextAction(plugin, this, page)
            {
                override fun onNext(player: Player, newPage: Int)
                {
                    CrateOverviewPage(plugin, backPage, newPage).open(player)
                }
            })
        setItem(nonClickableSizeWithNull - 1, object : PaginationPreviousAction(plugin, this, page)
        {
            override fun onPrevious(player: Player, newPage: Int)
            {
                CrateOverviewPage(plugin, backPage, newPage).open(player)
            }
        })
        val start = page * nonClickableSizeWithNull
        val crates = plugin.data.getConfigurationSection(Data.VOTE_CRATES.path)
            ?.getKeys(false)
            ?.mapNotNull { it.toIntOrNull() }
            ?.sorted()
            ?.drop(start)
            ?.take(nonClickableSizeWithNull)
            ?: emptyList()
        for (key in crates)
        {
            addItem(CrateSettingsShortcut(plugin, this, key))
        }
    }
}