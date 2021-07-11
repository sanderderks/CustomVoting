package me.sd_master92.customvoting.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class VoteSettings extends GUI
{
    public static final String NAME = "Vote Settings";
    public static final ItemStack GENERAL_SETTINGS = createItem(Material.COMMAND_BLOCK, ChatColor.AQUA + "General",
            null, true);
    public static final ItemStack REWARD_SETTINGS = createItem(Material.DIAMOND, ChatColor.LIGHT_PURPLE + "Rewards",
            null, true);
    public static final ItemStack SUPPORT = createItem(Material.SPYGLASS, ChatColor.GREEN + "Support",
            null, true);

    public VoteSettings()
    {
        super(NAME, 9);

        getInventory().setItem(1, GENERAL_SETTINGS);
        getInventory().setItem(3, REWARD_SETTINGS);
        getInventory().setItem(5, UNDER_CONSTRUCTION);
        getInventory().setItem(7, SUPPORT);
    }
}
