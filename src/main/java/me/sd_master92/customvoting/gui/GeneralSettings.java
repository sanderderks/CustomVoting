package me.sd_master92.customvoting.gui;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Settings;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import me.sd_master92.customvoting.constants.enumerations.VotePartyType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GeneralSettings extends GUI
{
    public static final String NAME = "General Settings";

    public GeneralSettings(Main plugin)
    {
        super(plugin, NAME, 9, false, true);

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

    @Override
    public void onClick(InventoryClickEvent event, Player player, @NotNull ItemStack item)
    {
        switch (item.getType())
        {
            case BARRIER:
                SoundType.CLICK.play(plugin, player);
                cancelCloseEvent();
                player.openInventory(new VoteSettings(plugin).getInventory());
                break;
            case CLOCK:
                SoundType.CHANGE.play(plugin, player);
                plugin.getConfig().set(Settings.MONTHLY_RESET,
                        !plugin.getConfig().getBoolean(Settings.MONTHLY_RESET));
                plugin.getConfig().saveConfig();
                event.setCurrentItem(Settings.getDoMonthlyResetSetting(plugin));
                break;
            case MUSIC_DISC_CAT:
                SoundType.CHANGE.play(plugin, player);
                plugin.getConfig().set(Settings.USE_SOUND_EFFECTS,
                        !plugin.getConfig().getBoolean(Settings.USE_SOUND_EFFECTS));
                plugin.getConfig().saveConfig();
                event.setCurrentItem(Settings.getUseSoundEffectsSetting(plugin));
                break;
            case FIREWORK_ROCKET:
                SoundType.CHANGE.play(plugin, player);
                plugin.getConfig().set(Settings.FIREWORK,
                        !plugin.getConfig().getBoolean(Settings.FIREWORK));
                plugin.getConfig().saveConfig();
                event.setCurrentItem(Settings.getUseFireworkSetting(plugin));
                break;
            case EXPERIENCE_BOTTLE:
                SoundType.CHANGE.play(plugin, player);
                plugin.getConfig().set(Settings.VOTE_PARTY,
                        !plugin.getConfig().getBoolean(Settings.VOTE_PARTY));
                plugin.getConfig().saveConfig();
                event.setCurrentItem(Settings.getDoVotePartySetting(plugin));
                break;
            case SPLASH_POTION:
                SoundType.CHANGE.play(plugin, player);
                plugin.getConfig().setNumber(Settings.VOTE_PARTY_TYPE,
                        VotePartyType.next(plugin).getValue());
                event.setCurrentItem(Settings.getVotePartyTypeSetting(plugin));
                break;
            case ENCHANTED_BOOK:
                SoundType.CHANGE.play(plugin, player);
                if (plugin.getConfig().getNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY) < 100)
                {
                    plugin.getConfig().addNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY, 10);
                } else
                {
                    plugin.getConfig().setNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY, 10);
                }
                event.setCurrentItem(Settings.getVotesUntilVotePartySetting(plugin));
                break;
            case ENDER_CHEST:
                SoundType.CHANGE.play(plugin, player);
                if (plugin.getConfig().getNumber(Settings.VOTE_PARTY_COUNTDOWN) < 60)
                {
                    plugin.getConfig().addNumber(Settings.VOTE_PARTY_COUNTDOWN, 10);
                } else
                {
                    plugin.getConfig().setNumber(Settings.VOTE_PARTY_COUNTDOWN, 0);
                }
                event.setCurrentItem(Settings.getVotePartyCountdownSetting(plugin));
                break;
            case TOTEM_OF_UNDYING:
                SoundType.CHANGE.play(plugin, player);
                plugin.getConfig().set(Settings.LUCKY_VOTE,
                        !plugin.getConfig().getBoolean(Settings.LUCKY_VOTE));
                plugin.getConfig().saveConfig();
                event.setCurrentItem(Settings.getDoLuckyVoteSetting(plugin));
                break;
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event, Player player)
    {
        SoundType.CLOSE.play(plugin, player);
    }
}
