package me.sd_master92.customvoting.gui;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Settings;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Support extends GUI
{
    public static final String NAME = "Support";

    public Support(Main plugin)
    {
        super(NAME, 9);

        getInventory().setItem(0, createItem(Material.CLOCK, ChatColor.LIGHT_PURPLE + "Up to date?",
                plugin.isUpToDate() ? ChatColor.GREEN + "Yes" :
                        ChatColor.GRAY + "Currently: " + ChatColor.RED + plugin.VERSION + ";" + ChatColor.GRAY +
                                "Latest: " + ChatColor.GREEN + plugin.getLatestVersion() + ";;" + ChatColor.GRAY + "Click to " +
                                "download"));
        getInventory().setItem(1, Settings.getDoIngameUpdatesSetting(plugin));
        getInventory().setItem(8, BACK_ITEM);
    }
}
