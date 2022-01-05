package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.ChatColor
import org.bukkit.Material

class DoVotePartyItem(plugin: CV) : BaseItem(
    Material.EXPERIENCE_BOTTLE, ChatColor.LIGHT_PURPLE.toString() + "Vote Party",
    ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(Settings.VOTE_PARTY)) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
)