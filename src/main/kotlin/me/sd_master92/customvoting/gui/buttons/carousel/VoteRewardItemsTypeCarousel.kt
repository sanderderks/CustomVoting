package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.core.appendWhenTrue
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.ItemRewardType
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractEnumCarousel
import org.bukkit.Material

class VoteRewardItemsTypeCarousel(plugin: CV, private val op: Boolean) : AbstractEnumCarousel(
    plugin,
    Material.REPEATER,
    ItemRewardType,
    Setting.ITEM_REWARD_TYPE.path.appendWhenTrue(op, Data.OP_REWARDS),
    PMessage.ITEM_REWARDS_ITEM_NAME_TYPE
)
{
    override fun newInstance(plugin: CV): VoteRewardItemsTypeCarousel
    {
        return VoteRewardItemsTypeCarousel(plugin, op)
    }

    init
    {
        val value = plugin.config.getNumber(Setting.ITEM_REWARD_TYPE.path.appendWhenTrue(op, Setting.OP_REWARDS))
        val type = ItemRewardType.valueOf(value).label
        setLore(PMessage.GENERAL_ITEM_LORE_STATUS_X.with(type))
    }
}