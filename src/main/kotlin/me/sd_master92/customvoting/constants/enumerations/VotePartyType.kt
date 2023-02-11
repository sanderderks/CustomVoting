package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.constants.interfaces.CarouselEnum
import me.sd_master92.customvoting.constants.interfaces.EnumCompanion
import java.util.*

enum class VotePartyType(val label: String) : CarouselEnum
{
    RANDOMLY("Randomly"),
    RANDOM_CHEST_AT_A_TIME("Random Chest at a Time"),
    ALL_CHESTS_AT_ONCE("All Chests at Once"),
    ONE_CHEST_AT_A_TIME("One Chest at a Time"),
    ADD_TO_INVENTORY("Add To Inventory"),
    EXPLODE_CHESTS("Explode Chests"),
    SCARY("Scary"),
    PIG_HUNT("Pig Hunt");

    override fun next(): VotePartyType
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
        fun random(): VotePartyType
        {
            return values()[Random().nextInt(1, values().size)]
        }

        override fun valueOf(key: Int): VotePartyType
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