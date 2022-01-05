package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.constants.enumerations.Materials
import org.bukkit.ChatColor

class SupportItem : BaseItem(
    Materials.SPYGLASS.get(), ChatColor.GREEN.toString() + "Support",
    null, true
)