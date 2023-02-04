package me.sd_master92.customvoting.gui.rewards.crate

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class CrateItemRewards(private val plugin: CV, private val number: Int, private val percentage: Int) : GUI(
    plugin,
    PMessage.CRATE_INVENTORY_NAME_PERC_REWARDS_XY.with(
        "$percentage",
        plugin.data.getString(Data.VOTE_CRATES.path + ".$number.name")
    ),
    27,
    false
)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        if (event.slot >= 25)
        {
            event.isCancelled = true
            if (event.slot == 26)
            {
                save(plugin, player, event.inventory, number, percentage)
            } else
            {
                SoundType.CLICK.play(plugin, player)
            }
            cancelCloseEvent = true
            player.openInventory(CrateSettings(plugin, number).inventory)
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        save(plugin, player, event.inventory, number, percentage)
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
            inventory.addItem(reward)
        }
        inventory.setItem(25, BACK_ITEM)
        inventory.setItem(26, SAVE_ITEM)
    }
}