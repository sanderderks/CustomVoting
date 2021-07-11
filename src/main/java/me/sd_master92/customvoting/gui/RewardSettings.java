package me.sd_master92.customvoting.gui;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.Settings;

public class RewardSettings extends GUI
{
    public static final String NAME = "Vote Rewards";

    public RewardSettings(Main plugin)
    {
        super(NAME, 9);

        getInventory().setItem(0, Data.getItemRewardSetting(plugin));
        getInventory().setItem(1, Settings.getMoneyRewardSetting(plugin));
        getInventory().setItem(2, Settings.getExperienceRewardSetting(plugin));
        getInventory().setItem(3, Data.getCommandRewardSetting(plugin));
        getInventory().setItem(4, Data.getLuckyRewardSetting(plugin));
        getInventory().setItem(5, Settings.getLuckyVoteChanceSetting(plugin));
        getInventory().setItem(8, BACK_ITEM);
    }
}
