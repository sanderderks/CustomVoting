package me.sd_master92.customvoting.gui

import me.sd_master92.customvoting.Main
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

abstract class GUI @JvmOverloads constructor(plugin: Main, name: String, size: Int, allowDrag: Boolean, alwaysCancel: Boolean = false) : Listener
{
    val inventory: Inventory
    val name: String
    private val allowDrag: Boolean
    private val alwaysCancel: Boolean
    private var cancelCloseEvent: Boolean

    abstract fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent)
    {
        if (isThisInventory(event))
        {
            if (alwaysCancel)
            {
                event.isCancelled = true
            }
            if (event.currentItem != null)
            {
                onClick(event, event.whoClicked as Player, event.currentItem!!)
            }
        }
    }

    abstract fun onClose(event: InventoryCloseEvent, player: Player)

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent)
    {
        if (isThisInventory(event) && !cancelCloseEvent)
        {
            onClose(event, event.player as Player)
        }
    }

    @EventHandler
    fun onDrag(event: InventoryDragEvent)
    {
        if (isThisInventory(event) && !allowDrag)
        {
            event.isCancelled = true
        }
    }

    fun cancelCloseEvent()
    {
        cancelCloseEvent = true
    }

    private fun isThisInventory(event: InventoryEvent): Boolean
    {
        return if (event is InventoryClickEvent)
        {
            event.clickedInventory === inventory
        } else event.inventory === inventory
    }

    companion object
    {
        val BACK_ITEM = createItem(Material.BARRIER, ChatColor.RED.toString() + "Back")
        val SAVE_ITEM = createItem(Material.WRITABLE_BOOK, ChatColor.GREEN.toString() + "Save")

        @JvmOverloads
        fun createItem(mat: Material?, name: String?, lore: String? = null, enchanted: Boolean = false): ItemStack
        {
            val item = ItemStack(mat!!)
            val meta = item.itemMeta
            if (meta != null)
            {
                if (name != null)
                {
                    meta.setDisplayName(name)
                }
                if (lore != null)
                {
                    meta.lore = null
                    meta.lore = listOf(*lore.split(";".toRegex()).toTypedArray())
                }
                if (enchanted)
                {
                    meta.addEnchant(Enchantment.MENDING, 1, true)
                }
                meta.addItemFlags(*ItemFlag.values())
                item.itemMeta = meta
            }
            return item
        }

        fun createItem(mat: Material?, enchanted: Boolean): ItemStack
        {
            return createItem(mat, null, null, enchanted)
        }
    }

    init
    {
        inventory = Bukkit.createInventory(null, size, name)
        this.name = name
        this.allowDrag = allowDrag
        cancelCloseEvent = false
        this.alwaysCancel = alwaysCancel
        plugin.server.pluginManager.registerEvents(this, plugin)
    }
}