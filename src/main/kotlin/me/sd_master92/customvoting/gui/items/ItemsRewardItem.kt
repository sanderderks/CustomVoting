package me.sd_master92.customvoting.gui.items

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import org.bukkit.ChatColor
import org.bukkit.Material

class ItemsRewardItem(plugin: CV, path: String, name: String? = null) : BaseItem(
    Material.CHEST, ChatColor.LIGHT_PURPLE.toString() + (name ?: "Item Rewards"),
    ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.data.getItems(path).size + ChatColor.GRAY + " item stacks"
)