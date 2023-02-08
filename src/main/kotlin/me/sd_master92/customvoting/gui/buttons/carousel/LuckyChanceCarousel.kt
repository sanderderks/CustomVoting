package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractNumberCarousel
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class LuckyChanceCarousel(private val plugin: CV) : AbstractNumberCarousel(
    plugin,
    Material.ENDER_EYE,
    Setting.LUCKY_VOTE_CHANCE,
    PMessage.LUCKY_ITEM_NAME_CHANCE,
    IntRange(1, 100),
    stepRules = mapOf(Pair(10, 1), Pair(100, 5))
)
{
    override fun newInstance(): ItemStack
    {
        return LuckyChanceCarousel(plugin)
    }

    init
    {
        val chance = plugin.config.getNumber(Setting.LUCKY_VOTE_CHANCE.path)
        setLore(PMessage.GENERAL_ITEM_LORE_CURRENT_X.with("$chance%"))
    }
}