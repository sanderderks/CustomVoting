package me.sd_master92.customvoting.gui;

import me.sd_master92.customvoting.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class GUI implements Listener
{
    public static final ItemStack BACK_ITEM = createItem(Material.BARRIER, ChatColor.RED + "Back");
    public static final ItemStack SAVE_ITEM = createItem(Material.WRITABLE_BOOK, ChatColor.GREEN + "Save");
    protected final Main plugin;
    private final Inventory inventory;
    private final String name;
    private final boolean allowDrag;
    private final boolean alwaysCancel;
    private boolean cancelCloseEvent;

    public GUI(Main plugin, String name, int size, boolean allowDrag, boolean alwaysCancel)
    {
        this.plugin = plugin;
        inventory = Bukkit.createInventory(null, size, name);
        this.name = name;
        this.allowDrag = allowDrag;
        this.cancelCloseEvent = false;
        this.alwaysCancel = alwaysCancel;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public GUI(Main plugin, String name, int size, boolean allowDrag)
    {
        this(plugin, name, size, allowDrag, false);
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

    public abstract void onClick(InventoryClickEvent event, Player player, @NotNull ItemStack item);

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (isThisInventory(event))
        {
            if (alwaysCancel)
            {
                event.setCancelled(true);
            }
            if (event.getCurrentItem() != null)
            {
                onClick(event, (Player) event.getWhoClicked(), event.getCurrentItem());
            }
        }
    }

    public abstract void onClose(InventoryCloseEvent event, Player player);

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if (isThisInventory(event) && !cancelCloseEvent)
        {
            onClose(event, (Player) event.getPlayer());
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event)
    {
        if (isThisInventory(event) && !allowDrag)
        {
            event.setCancelled(true);
        }
    }

    public Inventory getInventory()
    {
        return inventory;
    }

    public String getName()
    {
        return name;
    }

    public void cancelCloseEvent()
    {
        cancelCloseEvent = true;
    }

    private boolean isThisInventory(InventoryEvent event)
    {
        if (event instanceof InventoryClickEvent)
        {
            return ((InventoryClickEvent) event).getClickedInventory() == inventory;
        }
        return event.getInventory() == inventory;
    }
}
