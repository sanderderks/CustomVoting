package me.sd_master92.customvoting.gui

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class Donators(private val plugin: Main) : GUI(plugin, "Donators", 9, false, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        if (item.type == Material.BARRIER)
        {
            SoundType.CLICK.play(plugin, player)
            cancelCloseEvent()
            player.openInventory(Support(plugin).inventory)
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    private fun createSkull(name: String): ItemStack
    {
        val skull = ItemStack(Material.PLAYER_HEAD)
        val meta = skull.itemMeta as SkullMeta?
        if (meta != null)
        {
            meta.setDisplayName(ChatColor.AQUA.toString() + name)
            meta.owningPlayer = Bukkit.getOfflinePlayer(name)
        }
        skull.itemMeta = meta
        return skull
    }

    init
    {
        for (donator in listOf("Dutchbeard"))
        {
            inventory.addItem(createSkull(donator))
        }
        inventory.setItem(8, BACK_ITEM)
    }
}