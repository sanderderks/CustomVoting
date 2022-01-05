package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.ChatColor
import org.bukkit.Material

class MonthlyPeriodItem(plugin: CV) : BaseItem(
    Material.TNT, ChatColor.LIGHT_PURPLE.toString() + "Monthly Period",
    ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(Settings.MONTHLY_PERIOD)) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
)