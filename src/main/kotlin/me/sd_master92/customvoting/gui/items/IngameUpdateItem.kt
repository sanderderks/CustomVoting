package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.ChatColor
import org.bukkit.Material

class IngameUpdateItem(plugin: CV) : BaseItem(
    Material.FILLED_MAP, ChatColor.LIGHT_PURPLE.toString() + "Ingame Updates",
    ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(Settings.INGAME_UPDATES)) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
)