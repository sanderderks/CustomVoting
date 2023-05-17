package me.sd_master92.customvoting.gui.buttons.abstracts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

abstract class AbstractNumberCarousel(
    private val plugin: CV,
    mat: Material,
    private val path: String,
    name: PMessage,
    private val range: IntRange,
    private val step: Int? = null,
    private val stepRules: Map<Int, Int>? = null
) : BaseItem(mat, name.toString())
{
    abstract fun newInstance(): ItemStack

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        val current = plugin.config.getNumber(path)
        if (current < range.last)
        {
            val step = stepRules?.get(stepRules.keys.filter { current < it }.min()) ?: step ?: 1
            plugin.config.addNumber(path, step)
        } else
        {
            plugin.config.setNumber(path, range.first)
        }
        event.currentItem = newInstance()
    }
}