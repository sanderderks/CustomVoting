package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import java.util.*

enum class VotePartyType(val value: Int, val label: String)
{
    RANDOM_CHEST_AT_A_TIME(0, "Randomly"),
    ALL_CHESTS_AT_ONCE(1, "All Chests at Once"),
    ONE_CHEST_AT_A_TIME(2, "One Chest at a Time"),
    ADD_TO_INVENTORY(3, "Add To Inventory");

    companion object
    {
        fun next(plugin: CV): VotePartyType
        {
            val currentValue = valueOf(plugin.config.getNumber(Settings.VOTE_PARTY_TYPE)).value
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
            return votePartyType.orElse(ALL_CHESTS_AT_ONCE)
        }
    }
}