package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.constants.interfaces.CarouselEnum
import me.sd_master92.customvoting.constants.interfaces.EnumCompanion

enum class ItemRewardType(val label: String) : CarouselEnum
{
    ALL_ITEMS(PMessage.ENUM_ITEM_REWARD_TYPE_ALL.toString()),
    RANDOM_ITEM(PMessage.ENUM_ITEM_REWARD_TYPE_RANDOM.toString());

    override fun next(): ItemRewardType
    {
        return if (ordinal < values().size - 1)
        {
            valueOf(ordinal + 1)
        } else
        {
            valueOf(0)
        }
    }

    companion object : EnumCompanion
    {
        override fun valueOf(key: Int): ItemRewardType
        {
            return try
            {
                values()[key]
            } catch (_: Exception)
            {
                values()[0]
            }
        }
    }
}