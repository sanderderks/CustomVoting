package me.sd_master92.customvoting.gui;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Settings;
import me.sd_master92.customvoting.constants.enumerations.SoundType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Support extends GUI
{
    public static final String NAME = "Support";

    public Support(Main plugin)
    {
        super(plugin, NAME, 9, false, true);

        getInventory().setItem(0, createItem(Material.CLOCK, ChatColor.LIGHT_PURPLE + "Up to date?",
                plugin.isUpToDate() ? ChatColor.GREEN + "Yes" :
                        ChatColor.GRAY + "Currently: " + ChatColor.RED + plugin.VERSION + ";" + ChatColor.GRAY +
                                "Latest: " + ChatColor.GREEN + plugin.getLatestVersion() + ";;" + ChatColor.GRAY + "Click to " +
                                "download"));
        getInventory().setItem(1, Settings.getDoIngameUpdatesSetting(plugin));
        getInventory().setItem(2, createItem(Material.ENCHANTED_BOOK, ChatColor.LIGHT_PURPLE + "Discord",
                ChatColor.GRAY + "Join the discord server"));
        getInventory().setItem(3, createItem(Material.ENCHANTING_TABLE, ChatColor.LIGHT_PURPLE + "Database",
                ChatColor.GRAY + "Status: " + (plugin.hasDatabaseConnection() ? ChatColor.GREEN + "Connected" :
                        ChatColor.RED + "Disabled")));
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
                if (!plugin.isUpToDate())
                {
                    SoundType.CLICK.play(plugin, player);
                    cancelCloseEvent();
                    player.closeInventory();
                    plugin.sendDownloadUrl(player);
                }
                break;
            case FILLED_MAP:
                SoundType.CHANGE.play(plugin, player);
                plugin.getConfig().set(Settings.INGAME_UPDATES,
                        !plugin.getConfig().getBoolean(Settings.INGAME_UPDATES));
                plugin.getConfig().saveConfig();
                event.setCurrentItem(Settings.getDoIngameUpdatesSetting(plugin));
                break;
            case ENCHANTED_BOOK:
                SoundType.CLICK.play(plugin, player);
                cancelCloseEvent();
                player.closeInventory();
                player.sendMessage(ChatColor.AQUA + "Join the Discord server:");
                player.sendMessage(ChatColor.GREEN + "https://discord.gg/v3qmJu7jWD");
                break;
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event, Player player)
    {
        SoundType.CLOSE.play(plugin, player);
    }
}
