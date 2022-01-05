package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.constants.enumerations.Materials
import org.bukkit.ChatColor

class VoteLinksItem : BaseItem(
    Materials.SOUL_TORCH.get(), ChatColor.LIGHT_PURPLE.toString() + "Vote Links",
    ChatColor.GRAY.toString() + "Place items in this inventory;;" + ChatColor.GRAY + "Right-click to edit an item"
)