package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.core.appendWhenTrue
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractNumberCarousel
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class VoteRewardExperienceCarousel(private val plugin: CV, private val power: Boolean) : AbstractNumberCarousel(
    plugin,
    Material.EXPERIENCE_BOTTLE,
    Setting.VOTE_REWARD_EXPERIENCE.path.appendWhenTrue(power, Setting.POWER_REWARDS),
    PMessage.XP_REWARD_ITEM_NAME, IntRange(0, 10)
)
{
    override fun newInstance(): ItemStack
    {
        return VoteRewardExperienceCarousel(plugin, power)
    }

    init
    {
        val number =
            plugin.config.getNumber(Setting.VOTE_REWARD_EXPERIENCE.path.appendWhenTrue(power, Setting.POWER_REWARDS))
        setLore(PMessage.GENERAL_ITEM_LORE_CURRENT_XY.with("$number", PMessage.XP_UNIT_LEVELS_MULTIPLE.toString()))
    }
}