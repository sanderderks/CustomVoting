package me.sd_master92.customvoting.gui.items

import org.bukkit.ChatColor
import org.bukkit.Material

class StreakKeyItem(key: String) : BaseItem(
    Material.ENDER_PEARL,
    ChatColor.LIGHT_PURPLE.toString() + "Streak #" + key
)