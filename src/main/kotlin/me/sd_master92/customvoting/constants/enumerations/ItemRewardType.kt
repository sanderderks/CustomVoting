package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV
import java.util.*

enum class ItemRewardType(val value: Int, val label: String)
{
    ALL_ITEMS(0, "All Items"),
    RANDOM_ITEM(1, "Randomly");

    companion object
    {
        fun next(plugin: CV, op: Boolean): ItemRewardType
        {
            val currentValue =
                valueOf(plugin.config.getNumber(Setting.ITEM_REWARD_TYPE.path + if (op) Data.OP_REWARDS else "")).value
            return if (currentValue < values().size - 1)
            {
                valueOf(currentValue + 1)
            } else
            {
                valueOf(0)
            }
        }

        fun valueOf(value: Int): ItemRewardType
        {
            val itemRewardType = Arrays.stream(values())
                .filter { type: ItemRewardType -> type.value == value }
                .findFirst()
            return itemRewardType.orElse(ALL_ITEMS)
        }
    }
}