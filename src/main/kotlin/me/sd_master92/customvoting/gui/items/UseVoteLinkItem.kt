package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.ChatColor
import org.bukkit.Material

class UseVoteLinkItem(plugin: CV) : BaseItem(
    Material.CHEST, ChatColor.LIGHT_PURPLE.toString() + "Vote Links Inventory",
    ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(Settings.VOTE_LINK_INVENTORY)) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
)