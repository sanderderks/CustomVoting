package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.CrateCreateAction
import me.sd_master92.customvoting.gui.buttons.shortcuts.CrateSettingsShortcut
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class CrateOverviewPage(private val plugin: CV, backPage: GUI?) :
    GUI(
        plugin,
        backPage,
        PMessage.CRATE_INVENTORY_NAME_OVERVIEW.toString(),
        calculateInventorySize(plugin)
    )
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

    companion object
    {
        private fun calculateInventorySize(plugin: CV): Int
        {
            val crates = (plugin.data.getConfigurationSection(Data.VOTE_CRATES.path)?.getKeys(false)?.size ?: 0) + 2
            return if (crates % 9 == 0)
            {
                crates
            } else
            {
                crates + (9 - (crates % 9))
            }
        }
    }

    init
    {
        setItem(calculateInventorySize(plugin) - 2, CrateCreateAction(plugin, this))
        for (key in plugin.data.getConfigurationSection(Data.VOTE_CRATES.path)?.getKeys(false)?.mapNotNull { key ->
            key.toIntOrNull()
        }?.sorted() ?: listOf())
        {
            addItem(CrateSettingsShortcut(plugin, this, key))
        }
    }
}