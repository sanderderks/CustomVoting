package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.settings.CrateSettingsPage
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

class CrateItemRewardsEditor(private val plugin: CV, private val number: Int, private val percentage: Int) : GUI(
    plugin,
    PMessage.CRATE_INVENTORY_NAME_PERC_REWARDS_XY.with(
        "$percentage",
        plugin.data.getString(Data.VOTE_CRATES.path + ".$number.name")
    ),
    27,
    false,
    save = true
)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        event.isCancelled = true
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        CrateSettingsPage(plugin, number).open(player)
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        save(plugin, player, event.inventory, number, percentage)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
        event.isCancelled = true
        save(plugin, player, event.inventory, number, percentage)
        cancelCloseEvent = true
        CrateSettingsPage(plugin, number).open(player)
    }

    companion object
    {
        fun save(plugin: CV, player: Player, inv: Inventory, number: Int, percentage: Int)
        {
            inv.setItem(25, null)
            inv.setItem(26, null)
            val path = "${Data.VOTE_CRATES}.$number.${Data.ITEM_REWARDS}.$percentage"
            val name = PMessage.CRATE_INVENTORY_NAME_PERC_REWARDS_XY.with(
                "$percentage",
                plugin.data.getString(Data.VOTE_CRATES.path + ".$number.name")
            )
            if (plugin.data.getItems(path).contentEquals(inv.contents.filterNotNull().toTypedArray()))
            {
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_NOTHING_CHANGED.toString())
            } else if (plugin.data.setItems(path, inv.contents))
            {
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_SUCCESS_X.with(name))
            } else
            {
                SoundType.FAILURE.play(plugin, player)
                player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_FAIL_X.with(name))
            }
        }
    }

    init
    {
        for (reward in plugin.data.getItems("${Data.VOTE_CRATES}.$number.${Data.ITEM_REWARDS}.$percentage"))
        {
            addItem(reward)
        }
    }
}