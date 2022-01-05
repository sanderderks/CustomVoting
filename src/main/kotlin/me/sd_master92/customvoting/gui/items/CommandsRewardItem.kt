package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import org.bukkit.ChatColor
import org.bukkit.Material

class CommandsRewardItem(plugin: CV, path: String) : BaseItem(
    Material.COMMAND_BLOCK, ChatColor.LIGHT_PURPLE.toString() + "Command Rewards",
    ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.data.getStringList(path).size + ChatColor.GRAY + " commands"
)