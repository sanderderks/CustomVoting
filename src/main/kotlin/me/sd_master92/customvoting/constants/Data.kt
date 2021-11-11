package me.sd_master92.customvoting.constants

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.gui.GUI
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Data
{
    const val VOTE_PARTY = "vote_party"
    const val ITEM_REWARDS = "rewards"
    const val LUCKY_REWARDS = "lucky_rewards"
    const val CURRENT_VOTES = "current_votes"
    const val VOTE_QUEUE = "queue"
    const val VOTE_TOP_SIGNS = "vote_top"
    const val VOTE_TOP_STANDS = "armor_stands"
    const val VOTE_COMMANDS = "vote_commands"
    const val VOTE_LINK_ITEMS = "vote_link_items"
    const val VOTE_LINKS = "vote_links"
    const val VOTE_STREAKS = "vote_streaks"

    fun getCommandRewardSetting(plugin: Main): ItemStack
    {
        return GUI.createItem(Material.COMMAND_BLOCK, ChatColor.LIGHT_PURPLE.toString() + "Command Rewards",
                ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.data.getStringList(VOTE_COMMANDS).size + ChatColor.GRAY + " commands")
    }

    fun getStreakPermissionRewardSetting(plugin: Main, number: Int): ItemStack
    {
        return GUI.createItem(Material.DIAMOND_SWORD, ChatColor.LIGHT_PURPLE.toString() + "Permission Rewards",
                ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.data.getStringList("$VOTE_STREAKS.$number.permissions").size + ChatColor.GRAY + " permissions")
    }

    fun getStreakCommandRewardSetting(plugin: Main, number: Int): ItemStack
    {
        return GUI.createItem(Material.SHIELD, ChatColor.LIGHT_PURPLE.toString() + "Command Rewards",
            ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.data.getStringList("$VOTE_STREAKS.$number.commands").size + ChatColor.GRAY + " commands")
    }

    fun getItemRewardSetting(plugin: Main): ItemStack
    {
        return GUI.createItem(Material.CHEST, ChatColor.LIGHT_PURPLE.toString() +
                "Item Rewards",
                ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.data.getItems(ITEM_REWARDS).size + ChatColor.GRAY + " item stacks")
    }

    fun getLuckyRewardSetting(plugin: Main): ItemStack
    {
        return GUI.createItem(Material.ENDER_CHEST, ChatColor.LIGHT_PURPLE.toString() +
                "Lucky Rewards",
                ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.data.getItems(LUCKY_REWARDS).size + ChatColor.GRAY + " item stacks")
    }

    fun getVoteStreakInventorySize(plugin: Main): Int
    {
        val streaks = (plugin.data.getConfigurationSection(VOTE_STREAKS)?.getKeys(false)?.size ?: 0) + 2
        return if (streaks % 9 == 0)
        {
            streaks
        } else
        {
            streaks + (9 - (streaks % 9))
        }
    }
}