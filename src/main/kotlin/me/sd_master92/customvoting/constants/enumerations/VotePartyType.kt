package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV
import java.util.*

enum class VotePartyType(val value: Int, val label: String)
{
    RANDOMLY(0, "Randomly!"),
    RANDOM_CHEST_AT_A_TIME(1, "Random Chest at a Time"),
    ALL_CHESTS_AT_ONCE(2, "All Chests at Once"),
    ONE_CHEST_AT_A_TIME(3, "One Chest at a Time"),
    ADD_TO_INVENTORY(4, "Add To Inventory"),
    EXPLODE_CHESTS(5, "Explode Chests"),
    SCARY(6, "Scary"),
    PIG_HUNT(7, "Pig Hunt");

    companion object
    {
        fun random(): VotePartyType
        {
            return valueOf(Random().nextInt(1, values().size))
        }

        fun next(plugin: CV): VotePartyType
        {
            val currentValue = valueOf(plugin.config.getNumber(Settings.VOTE_PARTY_TYPE.path)).value
            return if (currentValue < values().size - 1)
            {
                valueOf(currentValue + 1)
            } else
            {
                valueOf(0)
            }
        }

        fun valueOf(value: Int): VotePartyType
        {
            val votePartyType = Arrays.stream(values())
                .filter { type: VotePartyType -> type.value == value }
                .findFirst()
            return votePartyType.orElse(RANDOM_CHEST_AT_A_TIME)
        }
    }
}