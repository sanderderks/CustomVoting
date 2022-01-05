package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import org.bukkit.ChatColor
import org.bukkit.Material

class LuckyRewardItem(plugin: CV) : BaseItem(
    Material.ENDER_CHEST, ChatColor.LIGHT_PURPLE.toString() +
            "Lucky Rewards",
    ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.data.getItems(Data.LUCKY_REWARDS).size + ChatColor.GRAY + " item stacks"
)