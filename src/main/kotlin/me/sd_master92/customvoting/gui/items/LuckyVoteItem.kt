package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.ChatColor
import org.bukkit.Material

class LuckyVoteItem(plugin: CV) : BaseItem(
    Material.TOTEM_OF_UNDYING, ChatColor.LIGHT_PURPLE.toString() + "Lucky Vote",
    ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(Settings.LUCKY_VOTE)) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
)