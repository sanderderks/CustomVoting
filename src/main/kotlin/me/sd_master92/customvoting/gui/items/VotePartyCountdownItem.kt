package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.ChatColor
import org.bukkit.Material

class VotePartyCountdownItem(plugin: CV) : BaseItem(
    Material.ENDER_CHEST, ChatColor.LIGHT_PURPLE.toString() + "Vote Party Countdown",
    ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.config.getNumber(Settings.VOTE_PARTY_COUNTDOWN)
)