package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.constants.interfaces.CarouselEnum
import me.sd_master92.customvoting.constants.interfaces.EnumCompanion

enum class VoteSortType(val label: PMessage) : CarouselEnum
{
    ALL(PMessage.ENUM_SORT_TYPE_ALL),
    MONTHLY(PMessage.ENUM_SORT_TYPE_MONTHLY),
    WEEKLY(PMessage.ENUM_SORT_TYPE_WEEKLY),
    DAILY(PMessage.ENUM_SORT_TYPE_DAILY);

    override fun next(): VoteSortType
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
        override fun valueOf(key: Int): VoteSortType
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