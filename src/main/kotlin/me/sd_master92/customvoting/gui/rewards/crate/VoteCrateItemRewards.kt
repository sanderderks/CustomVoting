package me.sd_master92.customvoting.gui.rewards.crate

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

class VoteCrateItemRewards(private val plugin: CV, private val number: Int, private val percentage: Int) : GUI(
    plugin,
    "$percentage% Rewards '" + plugin.data.getString(Data.VOTE_CRATES + ".$number.name") + "'", 27, false
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
            player.openInventory(VoteCrateSettings(plugin, number).inventory)
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
            if (plugin.data.getItems(path).contentEquals(inv.contents.filterNotNull().toTypedArray()))
            {
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(ChatColor.GRAY.toString() + "Nothing changed!")
            } else if (plugin.data.setItems(path, inv.contents))
            {
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(
                    ChatColor.GREEN.toString() + "Successfully updated the $percentage% Crate Rewards of ${
                        plugin.data.getString(
                            Data.VOTE_CRATES + ".$number.name"
                        )
                    }!"
                )
            } else
            {
                SoundType.FAILURE.play(plugin, player)
                player.sendMessage(
                    ChatColor.RED.toString() + "Failed to update the $percentage% Crate Rewards of ${
                        plugin.data.getString(
                            Data.VOTE_CRATES + ".$number.name"
                        )
                    }!"
                )
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