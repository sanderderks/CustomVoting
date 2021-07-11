package me.sd_master92.customvoting.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class GUI
{
    public static final ItemStack BACK_ITEM = createItem(Material.BARRIER, ChatColor.RED + "Back");
    public static final ItemStack SAVE_ITEM = createItem(Material.WRITABLE_BOOK, ChatColor.GREEN + "Save");
    public static final ItemStack UNDER_CONSTRUCTION = createItem(Material.IRON_SHOVEL, ChatColor.RED + "Under " +
            "Construction");
    private final Inventory inventory;
    private final String name;

    public GUI(String name, int size)
    {
        inventory = Bukkit.createInventory(null, size, name);
        this.name = name;
    }

    public static ItemStack createItem(Material mat, String name, String lore, boolean enchanted)
    {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null)
        {
            if (name != null)
            {
                meta.setDisplayName(name);
            }
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

    public static ItemStack createItem(Material mat, boolean enchanted)
    {
        return createItem(mat, null, null, enchanted);
    }

    public Inventory getInventory()
    {
        return inventory;
    }

    public String getName()
    {
        return name;
    }
}
