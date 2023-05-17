package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.gui.items.SimpleItem
import net.citizensnpcs.api.trait.trait.Equipment
import org.bukkit.Material
import org.bukkit.inventory.EntityEquipment

enum class ArmorType(
    private val chest: Material,
    private val leggings: Material,
    private val boots: Material,
    private val sword: Material,
    private val enchanted: Boolean = true
)
{
    DIAMOND(Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.DIAMOND_SWORD),
    GOLD(Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS, Material.GOLDEN_SWORD),
    IRON(Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.IRON_SWORD),
    LEATHER(
        Material.LEATHER_CHESTPLATE,
        Material.LEATHER_LEGGINGS,
        Material.LEATHER_BOOTS,
        Material.STONE_SWORD,
        false
    );

    companion object
    {
        private fun getType(top: Int): ArmorType
        {
            return when (top)
            {
                1    -> DIAMOND
                2    -> GOLD
                3    -> IRON
                else -> LEATHER
            }
        }

        fun dress(equipment: EntityEquipment, top: Int)
        {
            val type = getType(top)
            equipment.chestplate = SimpleItem(type.chest, type.enchanted)
            equipment.leggings = SimpleItem(type.leggings, type.enchanted)
            equipment.boots = SimpleItem(type.boots, type.enchanted)
            equipment.setItemInMainHand(SimpleItem(type.sword, type.enchanted))
        }

        fun dress(equipment: Equipment?, top: Int)
        {
            if (equipment != null)
            {
                val type = getType(top)
                equipment.set(Equipment.EquipmentSlot.CHESTPLATE, SimpleItem(type.chest, type.enchanted))
                equipment.set(Equipment.EquipmentSlot.LEGGINGS, SimpleItem(type.leggings, type.enchanted))
                equipment.set(Equipment.EquipmentSlot.BOOTS, SimpleItem(type.boots, type.enchanted))
                equipment.set(Equipment.EquipmentSlot.HAND, SimpleItem(type.sword, type.enchanted))
            }
        }
    }
}