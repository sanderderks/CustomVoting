package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import org.bukkit.ChatColor
import org.bukkit.Material

open class StatusItem(plugin: CV, mat: Material, name: String, path: String) : BaseItem(
    mat,
    ChatColor.LIGHT_PURPLE.toString() + name,
    ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(
            path
        )
    ) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
)
{
}