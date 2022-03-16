package me.sd_master92.customvoting.helpers

import me.sd_master92.customvoting.gui.items.BaseItem
import org.bukkit.Material
import org.bukkit.inventory.EntityEquipment

class EntityHelper
{
    companion object
    {
        fun setEntityEquipment(equipment: EntityEquipment, top: Int)
        {
            when (top)
            {
                1    ->
                {
                    equipment.chestplate = BaseItem(Material.DIAMOND_CHESTPLATE, true)
                    equipment.leggings = BaseItem(Material.DIAMOND_LEGGINGS, true)
                    equipment.boots = BaseItem(Material.DIAMOND_BOOTS, true)
                    equipment.setItemInMainHand(BaseItem(Material.DIAMOND_SWORD, true))
                }
                2    ->
                {
                    equipment.chestplate = BaseItem(Material.GOLDEN_CHESTPLATE, true)
                    equipment.leggings = BaseItem(Material.GOLDEN_LEGGINGS, true)
                    equipment.boots = BaseItem(Material.GOLDEN_BOOTS, true)
                    equipment.setItemInMainHand(BaseItem(Material.GOLDEN_SWORD, true))
                }
                3    ->
                {
                    equipment.chestplate = BaseItem(Material.IRON_CHESTPLATE, true)
                    equipment.leggings = BaseItem(Material.IRON_LEGGINGS, true)
                    equipment.boots = BaseItem(Material.IRON_BOOTS, true)
                    equipment.setItemInMainHand(BaseItem(Material.IRON_SWORD, true))
                }
                else ->
                {
                    equipment.chestplate = BaseItem(Material.CHAINMAIL_CHESTPLATE, true)
                    equipment.leggings = BaseItem(Material.CHAINMAIL_LEGGINGS, true)
                    equipment.boots = BaseItem(Material.CHAINMAIL_BOOTS, true)
                    equipment.setItemInMainHand(BaseItem(Material.STONE_SWORD, true))
                }
            }
        }
    }
}