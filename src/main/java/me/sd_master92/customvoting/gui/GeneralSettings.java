package me.sd_master92.customvoting.gui;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Settings;

public class GeneralSettings extends GUI
{
    public static final String NAME = "General Settings";

    public GeneralSettings(Main plugin)
    {
        super(NAME, 9);

        getInventory().setItem(0, Settings.getDoMonthlyResetSetting(plugin));
        getInventory().setItem(1, Settings.getUseSoundEffectsSetting(plugin));
        getInventory().setItem(2, Settings.getUseFireworkSetting(plugin));
        getInventory().setItem(3, Settings.getDoLuckyVoteSetting(plugin));
        getInventory().setItem(4, Settings.getDoVotePartySetting(plugin));
        getInventory().setItem(5, Settings.getVotePartyTypeSetting(plugin));
        getInventory().setItem(6, Settings.getVotesUntilVotePartySetting(plugin));
        getInventory().setItem(7, Settings.getVotePartyCountdownSetting(plugin));
        getInventory().setItem(8, BACK_ITEM);
    }
}
