package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.ChatColor
import org.bukkit.Material

class LuckyVoteChanceItem(plugin: CV) : BaseItem(
    Material.ENDER_EYE, ChatColor.LIGHT_PURPLE.toString() + "Lucky Vote Chance",
    ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.config.getNumber(Settings.LUCKY_VOTE_CHANCE) + ChatColor.GRAY + "%"
)