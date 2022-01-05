package me.sd_master92.customvoting.gui.items

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

open class BaseItem(mat: Material, name: String?, lore: String? = null, enchanted: Boolean = false) : ItemStack(mat)
{
    constructor(mat: Material, enchanted: Boolean) : this(mat, null, null, enchanted)

    init
    {
        if (itemMeta != null)
        {
            if (name != null)
            {
                itemMeta!!.setDisplayName(name)
            }
            if (lore != null)
            {
                itemMeta!!.lore = null
                itemMeta!!.lore = listOf(*lore.split(";".toRegex()).toTypedArray())
            }
            if (enchanted)
            {
                itemMeta!!.addEnchant(Enchantment.LUCK, 1, true)
            }
            itemMeta!!.addItemFlags(*ItemFlag.values())
        }
    }
}