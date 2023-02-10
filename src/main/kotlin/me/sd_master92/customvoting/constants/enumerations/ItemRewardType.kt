package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.constants.interfaces.CarouselEnum
import me.sd_master92.customvoting.constants.interfaces.EnumCompanion
import java.util.*

enum class ItemRewardType(val label: String) : CarouselEnum
{
    ALL_ITEMS("All Items"),
    RANDOM_ITEM("Random Item");

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
        override fun valueOf(value: Int): ItemRewardType
        {
            val itemRewardType = Arrays.stream(values())
                .filter { type: ItemRewardType -> type.ordinal == value }
                .findFirst()
            return itemRewardType.orElse(ALL_ITEMS)
        }
    }
}