package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.core.appendWhenTrue
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.ItemRewardType
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractEnumCarousel
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class VoteRewardItemsTypeCarousel(private val plugin: CV, private val power: Boolean) : AbstractEnumCarousel(
    plugin,
    Material.REPEATER,
    ItemRewardType,
    Setting.ITEM_REWARD_TYPE.path.appendWhenTrue(power, Data.POWER_REWARDS),
    PMessage.ITEM_REWARDS_ITEM_NAME_TYPE
)
{
    override fun newInstance(): ItemStack
    {
        return VoteRewardItemsTypeCarousel(plugin, power)
    }

    init
    {
        val value = plugin.config.getNumber(Setting.ITEM_REWARD_TYPE.path.appendWhenTrue(power, Setting.POWER_REWARDS))
        val type = ItemRewardType.valueOf(value).label
        setLore(PMessage.GENERAL_ITEM_LORE_STATUS_X.with(type))
    }
}