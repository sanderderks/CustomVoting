package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.ChatColor
import org.bukkit.Material

class MonthlyResetItem(plugin: CV) : BaseItem(
    Material.CLOCK, ChatColor.LIGHT_PURPLE.toString() + "Monthly Reset",
    ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(Settings.MONTHLY_RESET)) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
)