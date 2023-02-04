package me.sd_master92.customvoting.gui.items

import me.sd_master92.core.inventory.BaseItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class SimpleItem(mat: Material, name: String? = null, lore: String? = null, enchanted: Boolean = false) :
    BaseItem(mat, name, lore, enchanted)
{
    constructor(mat: Material, enchanted: Boolean) : this(mat, null, null, enchanted)

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }
}