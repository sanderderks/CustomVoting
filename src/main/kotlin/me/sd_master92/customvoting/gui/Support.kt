package me.sd_master92.customvoting.gui

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.plugin.CustomPlugin
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class Support(private val plugin: Main) : GUI(plugin, NAME, 9, false, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(VoteSettings(plugin).inventory)
            }
            Material.CLOCK -> if (!plugin.isUpToDate)
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.closeInventory()
                plugin.sendDownloadUrl(player)
            }
            Material.FILLED_MAP ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(Settings.INGAME_UPDATES,
                        !plugin.config.getBoolean(Settings.INGAME_UPDATES))
                plugin.config.saveConfig()
                event.currentItem = Settings.getDoIngameUpdatesSetting(plugin)
            }
            Material.ENCHANTED_BOOK ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.closeInventory()
                player.sendMessage(ChatColor.AQUA.toString() + "Join the Discord server:")
                player.sendMessage(ChatColor.GREEN.toString() + "https://discord.gg/v3qmJu7jWD")
            }
            else ->
            {
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    companion object
    {
        const val NAME = "Support"
    }

    init
    {
        inventory.setItem(0, createItem(Material.CLOCK, ChatColor.LIGHT_PURPLE.toString() + "Up to date?",
                if (plugin.isUpToDate) ChatColor.GREEN.toString() + "Yes" else ChatColor.GRAY.toString() + "Currently: " + ChatColor.RED + CustomPlugin.VERSION + ";" + ChatColor.GRAY +
                        "Latest: " + ChatColor.GREEN + plugin.latestVersion + ";;" + ChatColor.GRAY + "Click to " +
                        "download"))
        inventory.setItem(1, Settings.getDoIngameUpdatesSetting(plugin))
        inventory.setItem(2, createItem(Material.ENCHANTED_BOOK, ChatColor.LIGHT_PURPLE.toString() + "Discord", ChatColor.GRAY.toString() + "Join the discord server"))
        inventory.setItem(3, createItem(Material.ENCHANTING_TABLE, ChatColor.LIGHT_PURPLE.toString() + "Database",
                ChatColor.GRAY.toString() + "Status: " + if (plugin.hasDatabaseConnection()) ChatColor.GREEN.toString() + "Connected" else ChatColor.RED.toString() + "Disabled"))
        inventory.setItem(8, BACK_ITEM)
    }
}