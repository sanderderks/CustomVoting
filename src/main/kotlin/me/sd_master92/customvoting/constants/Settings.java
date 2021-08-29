package me.sd_master92.customvoting.constants;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.enumerations.VotePartyType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static me.sd_master92.customvoting.gui.GUI.createItem;

public class Settings
{
    public static final String MONTHLY_RESET = "monthly_reset";
    public static final String USE_SOUND_EFFECTS = "sound_effects";
    public static final String VOTE_PARTY_COUNTDOWN = "vote_party_countdown";
    public static final String FIREWORK = "firework";
    public static final String VOTE_PARTY = "vote_party";
    public static final String VOTE_PARTY_TYPE = "vote_party_type";
    public static final String VOTES_REQUIRED_FOR_VOTE_PARTY = "votes_required_for_vote_party";
    public static final String VOTE_REWARD_MONEY = "vote_reward_money";
    public static final String VOTE_REWARD_EXPERIENCE = "vote_reward_xp";
    public static final String LUCKY_VOTE = "lucky_vote";
    public static final String LUCKY_VOTE_CHANCE = "lucky_vote_chance";
    public static final String VOTE_LINK_INVENTORY = "vote_link_inventory";
    public static final String FORBIDDEN_COMMANDS = "forbidden_commands";
    public static final String INGAME_UPDATES = "ingame_updates";
    public static final String USE_DATABASE = "use_database";
    public static final String DATABASE = "database";
    public static final String DATABASE_HOST = DATABASE + ".host";
    public static final String DATABASE_PORT = DATABASE + ".port";
    public static final String DATABASE_DATABASE = DATABASE + ".database";
    public static final String DATABASE_USER = DATABASE + ".user";
    public static final String DATABASE_PASSWORD = DATABASE + ".password";

    public static void initialize(Main plugin)
    {
        setDefault(plugin, MONTHLY_RESET, false);
        setDefault(plugin, USE_SOUND_EFFECTS, true);
        setDefault(plugin, VOTE_PARTY_COUNTDOWN, 30);
        setDefault(plugin, FIREWORK, true);
        setDefault(plugin, VOTE_PARTY, true);
        setDefault(plugin, VOTES_REQUIRED_FOR_VOTE_PARTY, 10);
        setDefault(plugin, VOTE_REWARD_MONEY, 100);
        setDefault(plugin, VOTE_REWARD_EXPERIENCE, 3);
        setDefault(plugin, LUCKY_VOTE, true);
        setDefault(plugin, LUCKY_VOTE_CHANCE, 50);
        setDefault(plugin, VOTE_LINK_INVENTORY, false);
        setDefault(plugin, FORBIDDEN_COMMANDS, new String[]{"fakevote", "op", "stop", "restart", "reload"});
        setDefault(plugin, INGAME_UPDATES, true);
        setDefault(plugin, USE_DATABASE, false);
        setDefault(plugin, DATABASE_HOST, "localhost");
        setDefault(plugin, DATABASE_PORT, 3306);
        setDefault(plugin, DATABASE_DATABASE, "customvoting");
        setDefault(plugin, DATABASE_USER, "root");
        setDefault(plugin, DATABASE_PASSWORD, "root");
        plugin.getConfig().saveConfig();
    }

    private static void setDefault(Main plugin, String path, Object value)
    {
        if (plugin.getConfig().get(path) == null)
        {
            plugin.getConfig().set(path, value);
        }
    }

    public static ItemStack getDoMonthlyResetSetting(Main plugin)
    {
        return createItem(Material.CLOCK, ChatColor.LIGHT_PURPLE + "Monthly Reset",
                ChatColor.GRAY + "Status: " + (plugin.getConfig().getBoolean(MONTHLY_RESET) ?
                        ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
    }

    public static ItemStack getUseSoundEffectsSetting(Main plugin)
    {
        return createItem(Material.MUSIC_DISC_CAT, ChatColor.LIGHT_PURPLE + "Sound Effects",
                ChatColor.GRAY + "Status: " + (plugin.getConfig().getBoolean(USE_SOUND_EFFECTS) ?
                        ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
    }

    public static ItemStack getUseFireworkSetting(Main plugin)
    {
        return createItem(Material.FIREWORK_ROCKET, ChatColor.LIGHT_PURPLE + "Firework",
                ChatColor.GRAY + "Status: " + (plugin.getConfig().getBoolean(FIREWORK) ?
                        ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
    }

    public static ItemStack getDoVotePartySetting(Main plugin)
    {
        return createItem(Material.EXPERIENCE_BOTTLE, ChatColor.LIGHT_PURPLE + "Vote Party",
                ChatColor.GRAY + "Status: " + (plugin.getConfig().getBoolean(VOTE_PARTY) ?
                        ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
    }

    public static ItemStack getDoLuckyVoteSetting(Main plugin)
    {
        return createItem(Material.TOTEM_OF_UNDYING, ChatColor.LIGHT_PURPLE + "Lucky Vote",
                ChatColor.GRAY + "Status: " + (plugin.getConfig().getBoolean(LUCKY_VOTE) ?
                        ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
    }

    public static ItemStack getVotePartyTypeSetting(Main plugin)
    {
        return createItem(Material.SPLASH_POTION, ChatColor.LIGHT_PURPLE + "Vote Party Type",
                ChatColor.GRAY + "Status: " + ChatColor.AQUA + VotePartyType.valueOf(plugin.getConfig().getNumber(VOTE_PARTY_TYPE)).getName());
    }

    public static ItemStack getVotesUntilVotePartySetting(Main plugin)
    {
        int votesRequired = plugin.getConfig().getNumber(VOTES_REQUIRED_FOR_VOTE_PARTY);
        int votesUntil = votesRequired - plugin.getData().getNumber("current_votes");
        return createItem(Material.ENCHANTED_BOOK, ChatColor.LIGHT_PURPLE + "Votes until Vote Party",
                ChatColor.GRAY + "Required: " + ChatColor.AQUA + votesRequired + ";" + ChatColor.GRAY + "Votes left:" +
                        " " + ChatColor.GREEN + votesUntil);
    }

    public static ItemStack getVotePartyCountdownSetting(Main plugin)
    {
        return createItem(Material.ENDER_CHEST, ChatColor.LIGHT_PURPLE + "Vote Party Countdown",
                ChatColor.GRAY + "Currently: " + ChatColor.AQUA + plugin.getConfig().getNumber(VOTE_PARTY_COUNTDOWN));
    }

    public static ItemStack getLuckyVoteChanceSetting(Main plugin)
    {
        return createItem(Material.ENDER_EYE, ChatColor.LIGHT_PURPLE + "Lucky Vote Chance",
                ChatColor.GRAY + "Currently: " + ChatColor.AQUA + plugin.getConfig().getNumber(LUCKY_VOTE_CHANCE) + ChatColor.GRAY + "%");
    }

    public static ItemStack getUseVoteLinksInventory(Main plugin)
    {
        return createItem(Material.CHEST, ChatColor.LIGHT_PURPLE + "Vote Links Inventory",
                ChatColor.GRAY + "Status: " + (plugin.getConfig().getBoolean(VOTE_LINK_INVENTORY) ?
                        ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));    }

    public static ItemStack getMoneyRewardSetting(Main plugin)
    {
        return createItem(Material.GOLD_INGOT, ChatColor.LIGHT_PURPLE + "Money Reward",
                Main.economy != null ?
                        ChatColor.GRAY + "Currently: " + ChatColor.GREEN + Main.economy.format(plugin.getConfig().getDouble(VOTE_REWARD_MONEY)) : ChatColor.RED + "Disabled");
    }

    public static ItemStack getExperienceRewardSetting(Main plugin)
    {
        return createItem(Material.EXPERIENCE_BOTTLE, ChatColor.LIGHT_PURPLE + "XP Reward",
                ChatColor.GRAY + "Currently: " + ChatColor.AQUA + plugin.getConfig().getNumber(VOTE_REWARD_EXPERIENCE) + ChatColor.GRAY + " levels");
    }

    public static ItemStack getDoIngameUpdatesSetting(Main plugin)
    {
        return createItem(Material.FILLED_MAP, ChatColor.LIGHT_PURPLE + "Ingame Updates",
                ChatColor.GRAY + "Status: " + (plugin.getConfig().getBoolean(INGAME_UPDATES) ?
                        ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
    }
}