package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.shortcuts.VotePartyRewardItemsShortcut
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class VotePartyChestOverview(private val plugin: CV, backPage: GUI?) :
    GUI(
        plugin,
        backPage,
        PMessage.VOTE_PARTY_INVENTORY_NAME_CHEST_OVERVIEW.toString(),
        calculateInventorySize(plugin)
    )
{
    override fun newInstance(): GUI
    {
        return VotePartyChestOverview(plugin, backPage)
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
            val crates = (plugin.data.getConfigurationSection(Data.VOTE_CRATES.path)?.getKeys(false)?.size ?: 0) + 1
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
        for (key in plugin.data.getLocations(Data.VOTE_PARTY_CHESTS.path).keys.map { key -> key.toInt() }.sorted())
        {
            addItem(VotePartyRewardItemsShortcut(plugin, this, key))
        }
    }
}