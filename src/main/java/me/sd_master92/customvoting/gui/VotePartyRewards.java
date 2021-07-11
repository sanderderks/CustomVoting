package me.sd_master92.customvoting.gui;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;

public class VotePartyRewards extends GUI
{
    public static final String NAME = "Vote Party Chest #";

    public VotePartyRewards(Main plugin, String key)
    {
        super(NAME + key, 54);
        getInventory().setContents(plugin.getData().getItems(Data.VOTE_PARTY + "." + key));
    }
}
