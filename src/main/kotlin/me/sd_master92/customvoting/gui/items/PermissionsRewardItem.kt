package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import org.bukkit.ChatColor
import org.bukkit.Material

class PermissionsRewardItem(plugin: CV, path: String) : BaseItem(
    Material.DIAMOND_SWORD, ChatColor.LIGHT_PURPLE.toString() + "Permission Rewards",
    ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.data.getStringList(path).size + ChatColor.GRAY + " permissions"
)