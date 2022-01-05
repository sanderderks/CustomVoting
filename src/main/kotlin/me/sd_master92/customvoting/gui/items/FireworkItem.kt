package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.ChatColor
import org.bukkit.Material

class FireworkItem(plugin: CV) : BaseItem(
    Material.FIREWORK_ROCKET, ChatColor.LIGHT_PURPLE.toString() + "Firework",
    ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(Settings.FIREWORK)) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
)