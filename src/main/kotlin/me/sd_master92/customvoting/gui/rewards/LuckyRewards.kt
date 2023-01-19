package me.sd_master92.customvoting.gui.rewards

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class LuckyRewards(private val plugin: CV) : GUI(plugin, NAME, 27, false)
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
            cancelCloseEvent = true
            player.openInventory(RewardSettings(plugin).inventory)
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        save(plugin, player, event.inventory)
    }

    companion object
    {
        const val NAME = "Lucky Rewards"
        fun save(plugin: CV, player: Player, inv: Inventory)
        {
            inv.setItem(25, null)
            inv.setItem(26, null)
            if (plugin.data.getItems(Data.LUCKY_REWARDS).contentEquals(inv.contents.filterNotNull().toTypedArray()))
            {
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(ChatColor.GRAY.toString() + "Nothing changed!")
            } else if (plugin.data.setItems(Data.LUCKY_REWARDS, inv.contents))
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
        for (reward in plugin.data.getItems(Data.LUCKY_REWARDS))
        {
            inventory.addItem(reward)
        }
        inventory.setItem(25, BACK_ITEM)
        inventory.setItem(26, SAVE_ITEM)
    }
}