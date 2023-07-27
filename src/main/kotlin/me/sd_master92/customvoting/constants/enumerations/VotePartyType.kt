package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.constants.interfaces.CarouselEnum
import me.sd_master92.customvoting.constants.interfaces.EnumCompanion
import java.util.*

enum class VotePartyType(private val label_: PMessage) : CarouselEnum
{
    RANDOMLY(PMessage.ENUM_VOTE_PARTY_TYPE_RANDOM),
    RANDOM_CHEST_AT_A_TIME(PMessage.ENUM_VOTE_PARTY_TYPE_RANDOM_CHEST_AT_A_TIME),
    ALL_CHESTS_AT_ONCE(PMessage.ENUM_VOTE_PARTY_TYPE_ALL_CHESTS_AT_ONCE),
    ONE_CHEST_AT_A_TIME(PMessage.ENUM_VOTE_PARTY_TYPE_ONE_CHEST_AT_A_TIME),
    ADD_TO_INVENTORY(PMessage.ENUM_VOTE_PARTY_TYPE_ADD_TO_INVENTORY),
    EXPLODE_CHESTS(PMessage.ENUM_VOTE_PARTY_TYPE_EXPLODE_CHESTS),
    SCARY(PMessage.ENUM_VOTE_PARTY_TYPE_SCARY),
    PIG_HUNT(PMessage.ENUM_VOTE_PARTY_TYPE_PIG_HUNT),
    LOCKED_CRATES(PMessage.ENUM_VOTE_PARTY_TYPE_LOCKED_CRATES);

    fun label(locale: Locale? = null): String
    {
        return label_.getValue(locale)
    }

    override fun next(): VotePartyType
    {
        return if (ordinal < entries.size - 1)
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
            return entries[Random().nextInt(1, entries.size)]
        }

        override fun valueOf(key: Int): VotePartyType
        {
            return try
            {
                entries[key]
            } catch (_: Exception)
            {
                entries[0]
            }
        }
    }
}