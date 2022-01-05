package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.plugin.CustomPlugin
import org.bukkit.ChatColor
import org.bukkit.Material

class UpdateItem(plugin: CV) : BaseItem(
    Material.CLOCK, ChatColor.LIGHT_PURPLE.toString() + "Up to date?",
    if (plugin.isUpToDate) ChatColor.GREEN.toString() + "Yes" else ChatColor.GRAY.toString() + "Currently: " + ChatColor.RED + CustomPlugin.VERSION + ";" + ChatColor.GRAY +
            "Latest: " + ChatColor.GREEN + plugin.latestVersion + ";;" + ChatColor.GRAY + "Click to " +
            "download"
)