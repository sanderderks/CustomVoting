package me.sd_master92.customvoting.services;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Settings;
import me.sd_master92.customvoting.constants.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
    public final static String VOTE_PARTY_REWARDS_INVENTORY = "Vote Party Chest #";

    public static final ItemStack BACK_ITEM = createItem(Material.BARRIER, ChatColor.RED + "Back");
    public static final ItemStack SAVE_ITEM = createItem(Material.WRITABLE_BOOK, ChatColor.GREEN + "Save");
    public static final ItemStack UNDER_CONSTRUCTION = createItem(Material.IRON_SHOVEL, ChatColor.RED + "Under " +
            "Construction");
    public static final ItemStack GENERAL_SETTINGS = createItem(Material.COMMAND_BLOCK, ChatColor.AQUA + "General");
    public static final ItemStack REWARD_SETTINGS = createItem(Material.DIAMOND, ChatColor.LIGHT_PURPLE + "Rewards");

    private final Main plugin;

    public GUIService(Main plugin)
    {
        this.plugin = plugin;
    }

    public static ItemStack createItem(Material mat, String name, String lore)
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
            meta.addItemFlags(ItemFlag.values());
            item.setItemMeta(meta);
        }
        return item;
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
        inv.setItem(4, getVotesUntilVoteParty());
        inv.setItem(5, getVotePartyCountdownSetting());
        inv.setItem(6, getVoteTopCommandShowPlayersSetting());
        inv.setItem(8, BACK_ITEM);
        return inv;
    }

    public Inventory getRewardSettings()
    {
        Inventory inv = Bukkit.createInventory(null, 27, REWARD_SETTINGS_INVENTORY);
        for (ItemStack reward : plugin.getData().getItems("rewards"))
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
        if (plugin.getData().setItems("rewards", inv.getContents()))
        {
            Sounds.SUCCESS.play(plugin, player.getLocation());
            player.sendMessage(ChatColor.GREEN + "Successfully updated vote rewards!");
        } else
        {
            Sounds.FAILURE.play(plugin, player.getLocation());
            player.sendMessage(ChatColor.RED + "Failed to update vote rewards!");
        }
    }

    public Inventory getVotePartyRewards(String key)
    {
        Inventory inv = Bukkit.createInventory(null, 54, VOTE_PARTY_REWARDS_INVENTORY + key);
        inv.setContents(plugin.getData().getItems("voteparty." + key));
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

    public ItemStack getVoteTopCommandShowPlayersSetting()
    {
        return createItem(Material.PLAYER_HEAD, ChatColor.LIGHT_PURPLE + "Vote Top Command",
                ChatColor.GRAY + "How many players to show?;" + ChatColor.GRAY + "Currently: " +
                        ChatColor.AQUA + plugin.getSettings().getNumber(Settings.VOTE_TOP_COMMAND_SHOW_PLAYERS));
    }
}
