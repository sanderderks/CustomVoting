package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.ItemRewardType
import org.bukkit.ChatColor
import org.bukkit.Material

class ItemsRewardTypeItem(plugin: CV) : BaseItem(
    Material.REPEATER, ChatColor.LIGHT_PURPLE.toString() + "Item Reward Type",
    ChatColor.GRAY.toString() + "Status: " + ChatColor.AQUA + ItemRewardType.valueOf(
        plugin.config.getNumber(
            Settings.ITEM_REWARD_TYPE
        )
    ).label
)