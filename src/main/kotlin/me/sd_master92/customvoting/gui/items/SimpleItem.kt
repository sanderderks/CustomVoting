package me.sd_master92.customvoting.gui.items

import me.sd_master92.core.inventory.BaseItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

open class SimpleItem(mat: Material, name: String? = null, lore: String? = null, enchanted: Boolean = false) :
    BaseItem(mat, name, lore, enchanted)
{
    constructor(mat: Material, enchanted: Boolean) : this(mat, null, null, enchanted)

    constructor(simpleItem: ItemStack) : this(
        simpleItem.type,
        simpleItem.itemMeta?.displayName,
        simpleItem.itemMeta?.lore?.joinToString(";"),
        simpleItem.itemMeta?.hasEnchants() ?: false
    )

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }
}