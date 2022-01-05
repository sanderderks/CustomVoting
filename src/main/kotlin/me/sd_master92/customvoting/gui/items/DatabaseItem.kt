package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import org.bukkit.ChatColor
import org.bukkit.Material

class DatabaseItem(plugin: CV) : BaseItem(
    Material.ENCHANTING_TABLE, ChatColor.LIGHT_PURPLE.toString() + "Database",
    ChatColor.GRAY.toString() + "Status: " + if (plugin.hasDatabaseConnection()) ChatColor.GREEN.toString() + "Connected" else ChatColor.RED.toString() + "Disabled"
)