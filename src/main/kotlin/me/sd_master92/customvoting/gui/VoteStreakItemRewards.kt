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

class VoteStreakItemRewards(private val plugin: Main, private val number: Int) : GUI(plugin, NAME + number, 27, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        if (event.slot >= 25)
        {
            event.isCancelled = true
            if (event.slot == 26)
            {
                save(plugin, player, event.inventory, number)
            } else
            {
                SoundType.CLICK.play(plugin, player)
            }
            cancelCloseEvent()
            player.openInventory(VoteStreakRewards(plugin, number).inventory)
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        save(plugin, player, event.inventory, number)
    }

    companion object
    {
        const val NAME = "Vote Streak Item Rewards #"
        fun save(plugin: Main, player: Player, inv: Inventory, number: Int)
        {
            inv.setItem(25, null)
            inv.setItem(26, null)
            if (plugin.data.setItems("${Data.VOTE_STREAKS}.$number.${Data.ITEM_REWARDS}", inv.contents))
            {
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(ChatColor.GREEN.toString() + "Successfully updated the Item Rewards of Streak #$number!")
            } else
            {
                SoundType.FAILURE.play(plugin, player)
                player.sendMessage(ChatColor.RED.toString() + "Failed to update the Item Rewards of Streak #$number!")
            }
        }
    }

    init
    {
        for (reward in plugin.data.getItems("${Data.VOTE_STREAKS}.$number.${Data.ITEM_REWARDS}"))
        {
            inventory.addItem(reward)
        }
        inventory.setItem(25, BACK_ITEM)
        inventory.setItem(26, SAVE_ITEM)
    }
}