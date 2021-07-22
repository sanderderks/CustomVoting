package me.sd_master92.customvoting.constants.enumerations;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Settings;

import java.util.Arrays;
import java.util.Optional;

public enum VotePartyType
{
    RANDOM_CHEST_AT_A_TIME(0, "Randomly"),
    ALL_CHESTS_AT_ONCE(1, "All Chests at Once"),
    ONE_CHEST_AT_A_TIME(2, "One Chest at a Time"),
    ADD_TO_INVENTORY(3, "Add To Inventory");

    private final int value;
    private final String name;

    VotePartyType(final int dropType, final String name)
    {
        this.value = dropType;
        this.name = name;
    }

    public static VotePartyType next(Main plugin)
    {
        int currentValue = VotePartyType.valueOf(plugin.getConfig().getNumber(Settings.VOTE_PARTY_TYPE)).getValue();
        if (currentValue < values().length - 1)
        {
            return valueOf(currentValue + 1);
        } else
        {
            return valueOf(0);
        }
    }

    public static VotePartyType valueOf(int value)
    {
        Optional<VotePartyType> votePartyType = Arrays.stream(values())
                .filter(type -> type.value == value)
                .findFirst();
        return votePartyType.orElse(ALL_CHESTS_AT_ONCE);
    }

    public int getValue()
    {
        return value;
    }

    public String getName()
    {
        return name;
    }
}
