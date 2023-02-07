package me.sd_master92.customvoting.gui.buttons.abstracts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.interfaces.CarouselButton
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

abstract class AbstractNumberCarousel(
    private val plugin: CV,
    mat: Material,
    private val setting: Setting,
    name: PMessage,
    private val range: IntRange,
    private val step: Int? = null,
    private val stepRules: Map<Int, Int>? = null
) : BaseItem(mat, name.toString()), CarouselButton
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        val current = plugin.config.getNumber(setting.path)
        if (current < range.last)
        {
            val step = stepRules?.get(stepRules.keys.filter { current < it }.min()) ?: step ?: 1
            plugin.config.addNumber(setting.path, step)
        } else
        {
            plugin.config.setNumber(setting.path, range.first)
        }
        event.currentItem = newInstance(plugin)
    }
}