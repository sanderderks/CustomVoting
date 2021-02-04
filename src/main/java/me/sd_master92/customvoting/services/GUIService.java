package me.sd_master92.customvoting.services;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.Settings;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import me.sd_master92.customvoting.constants.enumerations.VotePartyType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GUIService
{
    public final static String MAIN_SETTINGS_INVENTORY = "Vote Settings";
    public final static String GENERAL_SETTINGS_INVENTORY = "General Settings";
    public final static String REWARD_SETTINGS_INVENTORY = "Vote Rewards";
    public final static String ITEM_REWARDS_INVENTORY = "Item Rewards";
    public final static String VOTE_PARTY_REWARDS_INVENTORY = "Vote Party Chest #";

    public static final ItemStack BACK_ITEM = createItem(Material.BARRIER, ChatColor.RED + "Back");
    public static final ItemStack SAVE_ITEM = createItem(Material.WRITABLE_BOOK, ChatColor.GREEN + "Save");
    public static final ItemStack UNDER_CONSTRUCTION = createItem(Material.IRON_SHOVEL, ChatColor.RED + "Under " +
            "Construction");
    public static final ItemStack GENERAL_SETTINGS = createItem(Material.COMMAND_BLOCK, ChatColor.AQUA + "General",
            null, true);
    public static final ItemStack REWARD_SETTINGS = createItem(Material.DIAMOND, ChatColor.LIGHT_PURPLE + "Rewards",
            null, true);
    public static final ItemStack ITEM_REWARD_SETTINGS = createItem(Material.CHEST, ChatColor.LIGHT_PURPLE +
            "Item Rewards");

    private final Main plugin;

    public GUIService(Main plugin)
    {
        this.plugin = plugin;
    }

    public static ItemStack createItem(Material mat, String name, String lore, boolean enchanted)
    {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null)
        {
            meta.setDisplayName(name);
            if (lore != null)
            {
                meta.setLore(null);
                meta.setLore(Arrays.asList(lore.split(";")));
            }
            if (enchanted)
            {
                meta.addEnchant(Enchantment.MENDING, 1, true);
            }
            meta.addItemFlags(ItemFlag.values());
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createItem(Material mat, String name, String lore)
    {
        return createItem(mat, name, lore, false);
    }

    public static ItemStack createItem(Material mat, String name)
    {
        return createItem(mat, name, null);
    }

    public Inventory getSettings()
    {
        Inventory inv = Bukkit.createInventory(null, 9, MAIN_SETTINGS_INVENTORY);
        inv.setItem(1, GENERAL_SETTINGS);
        inv.setItem(3, REWARD_SETTINGS);
        inv.setItem(5, UNDER_CONSTRUCTION);
        inv.setItem(7, UNDER_CONSTRUCTION);
        return inv;
    }

    public Inventory getGeneralSettings()
    {
        Inventory inv = Bukkit.createInventory(null, 9, GENERAL_SETTINGS_INVENTORY);
        inv.setItem(0, getDoMonthlyResetSetting());
        inv.setItem(1, getUseSoundEffectsSetting());
        inv.setItem(2, getUseFirework());
        inv.setItem(3, getDoVoteParty());
        inv.setItem(4, getVotePartyType());
        inv.setItem(5, getVotesUntilVoteParty());
        inv.setItem(6, getVotePartyCountdownSetting());
        inv.setItem(8, BACK_ITEM);
        return inv;
    }

    public Inventory getRewardSettings()
    {
        Inventory inv = Bukkit.createInventory(null, 9, REWARD_SETTINGS_INVENTORY);
        inv.setItem(0, ITEM_REWARD_SETTINGS);
        inv.setItem(1, getMoneyRewardSetting());
        inv.setItem(2, getExperienceRewardSetting());
        inv.setItem(8, BACK_ITEM);
        return inv;
    }

    public Inventory getItemRewards()
    {
        Inventory inv = Bukkit.createInventory(null, 27, ITEM_REWARDS_INVENTORY);
        for (ItemStack reward : plugin.getData().getItems(Data.VOTE_REWARDS))
        {
            inv.addItem(reward);
        }
        inv.setItem(25, BACK_ITEM);
        inv.setItem(26, SAVE_ITEM);
        return inv;
    }

    public void saveRewards(Player player, Inventory inv)
    {
        inv.setItem(25, null);
        inv.setItem(26, null);
        if (plugin.getData().setItems(Data.VOTE_REWARDS, inv.getContents()))
        {
            SoundType.SUCCESS.play(plugin, player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Successfully updated vote rewards!");
        } else
        {
            SoundType.FAILURE.play(plugin, player.getLocation());
            player.sendMessage(ChatColor.RED + "Failed to update vote rewards!");
        }
    }

    public Inventory getVotePartyRewards(String key)
    {
        Inventory inv = Bukkit.createInventory(null, 54, VOTE_PARTY_REWARDS_INVENTORY + key);
        inv.setContents(plugin.getData().getItems(Data.VOTE_PARTY + "." + key));
        return inv;
    }

    public ItemStack getDoMonthlyResetSetting()
    {
        return createItem(Material.CLOCK, ChatColor.LIGHT_PURPLE + "Monthly Reset",
                ChatColor.GRAY + "Status: " + (plugin.getSettings().getBoolean(Settings.MONTHLY_RESET) ?
                        ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
    }

    public ItemStack getUseSoundEffectsSetting()
    {
        return createItem(Material.MUSIC_DISC_CAT, ChatColor.LIGHT_PURPLE + "Sound Effects",
                ChatColor.GRAY + "Status: " + (plugin.getSettings().getBoolean(Settings.USE_SOUND_EFFECTS) ?
                        ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
    }

    public ItemStack getUseFirework()
    {
        return createItem(Material.FIREWORK_ROCKET, ChatColor.LIGHT_PURPLE + "Firework",
                ChatColor.GRAY + "Status: " + (plugin.getSettings().getBoolean(Settings.FIREWORK) ?
                        ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
    }

    public ItemStack getDoVoteParty()
    {
        return createItem(Material.EXPERIENCE_BOTTLE, ChatColor.LIGHT_PURPLE + "Vote Party",
                ChatColor.GRAY + "Status: " + (plugin.getSettings().getBoolean(Settings.VOTE_PARTY) ?
                        ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
    }

    public ItemStack getVotePartyType()
    {
        return createItem(Material.SPLASH_POTION, ChatColor.LIGHT_PURPLE + "Vote Party Type",
                ChatColor.GRAY + "Status: " + ChatColor.AQUA + VotePartyType.valueOf(plugin.getSettings().getNumber(Settings.VOTE_PARTY_TYPE)).getName());
    }

    public ItemStack getVotesUntilVoteParty()
    {
        int votesRequired = plugin.getSettings().getNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY);
        int votesUntil = votesRequired - plugin.getData().getNumber("current_votes");
        return createItem(Material.ENCHANTED_BOOK, ChatColor.LIGHT_PURPLE + "Votes until Vote Party",
                ChatColor.GRAY + "Required: " + ChatColor.AQUA + votesRequired + ";" + ChatColor.GRAY + "Votes left:" +
                        " " + ChatColor.GREEN + votesUntil);
    }

    public ItemStack getVotePartyCountdownSetting()
    {
        return createItem(Material.ENDER_CHEST, ChatColor.LIGHT_PURPLE + "Vote Party Countdown",
                ChatColor.GRAY + "Currently: " + ChatColor.AQUA + plugin.getSettings().getNumber(Settings.VOTE_PARTY_COUNTDOWN));
    }

    public ItemStack getMoneyRewardSetting()
    {
        return createItem(Material.GOLD_INGOT, ChatColor.LIGHT_PURPLE + "Money Reward",
                Main.economy != null ?
                        ChatColor.GRAY + "Currently: " + ChatColor.GREEN + Main.economy.format(plugin.getSettings().getDouble(Settings.VOTE_REWARD_MONEY)) : ChatColor.RED + "DISABLED");
    }

    public ItemStack getExperienceRewardSetting()
    {
        return createItem(Material.EXPERIENCE_BOTTLE, ChatColor.LIGHT_PURPLE + "XP Reward",
                ChatColor.GRAY + "Currently: " + ChatColor.GREEN + plugin.getSettings().getNumber(Settings.VOTE_REWARD_EXPERIENCE) + " levels");
    }
}
