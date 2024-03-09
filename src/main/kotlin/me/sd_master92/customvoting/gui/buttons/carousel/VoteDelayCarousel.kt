package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractNumberCarousel
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class VoteDelayCarousel(private val plugin: CV) : AbstractNumberCarousel(
    plugin,
    Material.CLOCK,
    Setting.VOTE_DELAY.toString(),
    PMessage.VOTE_ITEM_NAME_DELAY,
    IntRange(0, 60),
    2
)
{
    override fun newInstance(): ItemStack
    {
        return VoteDelayCarousel(plugin)
    }

    init
    {
        val number = plugin.config.getNumber(Setting.VOTE_DELAY.path)
        setLore(PMessage.GENERAL_ITEM_LORE_CURRENT_XY.with("$number", PMessage.TIME_UNIT_SECONDS_MULTIPLE.toString()))
    }
}