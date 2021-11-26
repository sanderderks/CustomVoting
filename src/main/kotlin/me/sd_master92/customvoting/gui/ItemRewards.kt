package me.sd_master92.customvoting.gui

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class ItemRewards(private val plugin: Main) : GUI(plugin, NAME, 27, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        if (event.slot >= 25)
        {
            event.isCancelled = true
            if (event.slot == 26)
            {
                save(plugin, player, event.inventory)
            } else
            {
                SoundType.CLICK.play(plugin, player)
            }
            cancelCloseEvent()
            player.openInventory(RewardSettings(plugin).inventory)
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        save(plugin, player, event.inventory)
    }

    companion object
    {
        const val NAME = "Item Rewards"
        fun save(plugin: Main, player: Player, inv: Inventory)
        {
            inv.setItem(25, null)
            inv.setItem(26, null)
            if (plugin.data.setItems(Data.ITEM_REWARDS, inv.contents))
            {
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(ChatColor.GREEN.toString() + "Successfully updated the $NAME!")
            } else
            {
                SoundType.FAILURE.play(plugin, player)
                player.sendMessage(ChatColor.RED.toString() + "Failed to update the $NAME!")
            }
        }
    }

    init
    {
        for (reward in plugin.data.getItems(Data.ITEM_REWARDS))
        {
            inventory.addItem(reward)
        }
        inventory.setItem(25, BACK_ITEM)
        inventory.setItem(26, SAVE_ITEM)
    }
}