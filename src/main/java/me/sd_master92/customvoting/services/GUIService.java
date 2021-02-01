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
        inv.setItem(0, getDoMonthlyReset());
        inv.setItem(1, getUseSoundEffects());
        inv.setItem(2, getVoteTopCommandShowPlayers());
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

    public void saveRewardSettings(Player player, Inventory inv)
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

    public ItemStack getDoMonthlyReset()
    {
        return createItem(Material.CLOCK, ChatColor.RED + "Monthly Reset",
                ChatColor.GRAY + "Status: " + (plugin.getSettings().getBoolean(Settings.MONTHLY_RESET) ?
                        ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
    }

    public ItemStack getUseSoundEffects()
    {
        return createItem(Material.MUSIC_DISC_CAT, ChatColor.LIGHT_PURPLE + "Sound Effects",
                ChatColor.GRAY + "Status: " + (plugin.getSettings().getBoolean(Settings.USE_SOUND_EFFECTS) ?
                        ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
    }

    public ItemStack getVoteTopCommandShowPlayers()
    {
        return createItem(Material.PLAYER_HEAD, ChatColor.LIGHT_PURPLE + "Vote Top Command",
                ChatColor.GRAY + "How many players to show?;" + ChatColor.GRAY + "Currently: " +
                        ChatColor.AQUA + plugin.getSettings().getNumber(Settings.VOTE_TOP_COMMAND_SHOW_PLAYERS));
    }
}
