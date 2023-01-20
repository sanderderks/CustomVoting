package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player

enum class SoundType(
    private val sound: Sound,
    private val `try`: String? = null
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
    PICKUP(Sound.ENTITY_ITEM_PICKUP),
    EXPLODE(Sound.ENTITY_GENERIC_EXPLODE),
    SCARY_1(Sound.ENTITY_ILLUSIONER_CAST_SPELL),
    SCARY_2(Sound.ENTITY_RAVAGER_ROAR, "ENTITY_WARDEN_EMERGE"),
    SCARY_3(Sound.EVENT_RAID_HORN, "ITEM_GOAT_HORN_SOUND_5"),
    SCARY_4(Sound.ENTITY_WITCH_CELEBRATE);

    fun play(plugin: CV, loc: Location)
    {
        if (plugin.config.getBoolean(Settings.USE_SOUND_EFFECTS.path))
        {
            val world = loc.world
            try
            {
                world?.playSound(loc, if (`try` != null) Sound.valueOf(`try`) else sound, 10f, 1f)
            } catch (_: Exception)
            {
                world?.playSound(loc, sound, 10f, 1f)
            }
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