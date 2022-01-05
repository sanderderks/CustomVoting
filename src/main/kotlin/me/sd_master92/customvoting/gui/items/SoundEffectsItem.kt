package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.ChatColor
import org.bukkit.Material

class SoundEffectsItem(plugin: CV) : BaseItem(
    Material.MUSIC_DISC_CAT, ChatColor.LIGHT_PURPLE.toString() + "Sound Effects",
    ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(Settings.USE_SOUND_EFFECTS)) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
)