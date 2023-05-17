package me.sd_master92.customvoting.gui.buttons.abstracts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.interfaces.EnumCompanion
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

abstract class AbstractEnumCarousel(
    private val plugin: CV,
    mat: Material,
    private val type: EnumCompanion,
    private val setting: String,
    name: PMessage
) : BaseItem(mat, name.toString())
{
    abstract fun newInstance(): ItemStack

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        val next = type.valueOf(plugin.config.getNumber(setting)).next()
        plugin.config.setNumber(setting, next.ordinal)
        event.currentItem = newInstance()
    }
}