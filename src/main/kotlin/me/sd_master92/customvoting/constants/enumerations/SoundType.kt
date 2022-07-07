package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player

enum class SoundType(
    private val sound: Sound
)
{
    SUCCESS(Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    FAILURE(Sound.BLOCK_ANVIL_LAND),
    CHANGE(Sound.UI_BUTTON_CLICK),
    NOTIFY(Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    CLICK(Sound.BLOCK_NOTE_BLOCK_PLING),
    OPEN(Sound.BLOCK_ENDER_CHEST_OPEN),
    CLOSE(Sound.BLOCK_ENDER_CHEST_CLOSE),
    VOTE_PARTY_START(Sound.ENTITY_ENDER_DRAGON_GROWL),
    PICKUP(Sound.ENTITY_ITEM_PICKUP);

    fun play(plugin: CV, loc: Location)
    {
        if (plugin.config.getBoolean(Settings.USE_SOUND_EFFECTS.path))
        {
            val world = loc.world
            world?.playSound(loc, sound, 10f, 1f)
        }
    }

    fun play(plugin: CV, player: Player)
    {
        play(plugin, player.location)
    }

    fun playForAll(plugin: CV)
    {
        for (player in plugin.server.onlinePlayers)
        {
            play(plugin, player.location)
        }
    }
}