package me.sd_master92.customvoting.gui.support

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.getSkull
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class Donators(private val plugin: CV) : GUI(plugin, "Donators", 9, false, true)
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

    init
    {
        for (donator in listOf("sd_master92", "Dutchbeard", "Smirren"))
        {
            inventory.addItem(Bukkit.getOfflinePlayer(donator).getSkull())
        }
        inventory.setItem(8, BACK_ITEM)
    }
}