package me.sd_master92.customvoting.gui.rewards

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.GUI
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class ItemRewards(private val plugin: CV, private val op: Boolean = false, suffix: String = "") : GUI(
    plugin,
    "Item Rewards $suffix", 27, true
)
{
    private var path = Data.ITEM_REWARDS
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
            player.openInventory(RewardSettings(plugin, op).inventory)
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        save(plugin, player, event.inventory)
    }

    private fun save(plugin: CV, player: Player, inv: Inventory)
    {
        inv.setItem(25, null)
        inv.setItem(26, null)
        if (plugin.data.setItems(path, inv.contents))
        {
            SoundType.SUCCESS.play(plugin, player)
            player.sendMessage(ChatColor.GREEN.toString() + "Successfully updated the $name!")
        } else
        {
            SoundType.FAILURE.play(plugin, player)
            player.sendMessage(ChatColor.RED.toString() + "Failed to update the $name!")
        }
    }

    init
    {
        if (op)
        {
            path += Data.OP_REWARDS
        }
        for (reward in plugin.data.getItems(path))
        {
            inventory.addItem(reward)
        }
        inventory.setItem(25, BACK_ITEM)
        inventory.setItem(26, SAVE_ITEM)
    }
}