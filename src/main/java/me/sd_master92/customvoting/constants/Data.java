package me.sd_master92.customvoting.constants;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.gui.GUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Data
{
    public static final String VOTE_PARTY = "vote_party";
    public static final String ITEM_REWARDS = "rewards";
    public static final String LUCKY_REWARDS = "lucky_rewards";
    public static final String CURRENT_VOTES = "current_votes";
    public static final String VOTE_QUEUE = "queue";
    public static final String VOTE_TOP_SIGNS = "vote_top";
    public static final String VOTE_TOP_STANDS = "armor_stands";
    public static final String VOTE_COMMANDS = "vote_commands";

    public static ItemStack getCommandRewardSetting(Main plugin)
    {
        return GUI.createItem(Material.COMMAND_BLOCK, ChatColor.LIGHT_PURPLE + "Command Reward",
                ChatColor.GRAY + "Currently: " + ChatColor.AQUA + plugin.getData().getStringList(VOTE_COMMANDS).size() + ChatColor.GRAY + " commands");
    }

    public static ItemStack getItemRewardSetting(Main plugin)
    {
        return GUI.createItem(Material.CHEST,
                ChatColor.LIGHT_PURPLE +
                        "Item Rewards",
                ChatColor.GRAY + "Currently: " + ChatColor.AQUA + plugin.getData().getItems(ITEM_REWARDS).length + ChatColor.GRAY + " item stacks");
    }

    public static ItemStack getLuckyRewardSetting(Main plugin)
    {
        return GUI.createItem(Material.ENDER_CHEST,
                ChatColor.LIGHT_PURPLE +
                        "Lucky Rewards",
                ChatColor.GRAY + "Currently: " + ChatColor.AQUA + plugin.getData().getItems(LUCKY_REWARDS).length + ChatColor.GRAY + " item stacks");
    }
}
