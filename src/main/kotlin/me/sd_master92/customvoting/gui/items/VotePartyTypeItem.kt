package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.VotePartyType
import org.bukkit.ChatColor
import org.bukkit.Material

class VotePartyTypeItem(plugin: CV) : BaseItem(
    Material.SPLASH_POTION, ChatColor.LIGHT_PURPLE.toString() + "Vote Party Type",
    ChatColor.GRAY.toString() + "Status: " + ChatColor.AQUA + VotePartyType.valueOf(
        plugin.config.getNumber(
            Settings.VOTE_PARTY_TYPE
        )
    ).label
)