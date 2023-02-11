package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.constants.interfaces.CarouselEnum
import me.sd_master92.customvoting.constants.interfaces.EnumCompanion
import java.util.*

enum class VotePartyType(val label: String) : CarouselEnum
{
    RANDOMLY(PMessage.ENUM_VOTE_PARTY_TYPE_RANDOM.toString()),
    RANDOM_CHEST_AT_A_TIME(PMessage.ENUM_VOTE_PARTY_TYPE_RANDOM_CHEST_AT_A_TIME.toString()),
    ALL_CHESTS_AT_ONCE(PMessage.ENUM_VOTE_PARTY_TYPE_ALL_CHESTS_AT_ONCE.toString()),
    ONE_CHEST_AT_A_TIME(PMessage.ENUM_VOTE_PARTY_TYPE_ONE_CHEST_AT_A_TIME.toString()),
    ADD_TO_INVENTORY(PMessage.ENUM_VOTE_PARTY_TYPE_ADD_TO_INVENTORY.toString()),
    EXPLODE_CHESTS(PMessage.ENUM_VOTE_PARTY_TYPE_EXPLODE_CHESTS.toString()),
    SCARY(PMessage.ENUM_VOTE_PARTY_TYPE_SCARY.toString()),
    PIG_HUNT(PMessage.ENUM_VOTE_PARTY_TYPE_PIG_HUNT.toString());

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