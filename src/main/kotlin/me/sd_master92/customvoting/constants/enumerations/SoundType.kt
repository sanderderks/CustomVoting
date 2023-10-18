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
    SCARY_4(Sound.ENTITY_WITCH_CELEBRATE),
    FUNNY_1(Sound.ENTITY_GHAST_HURT),
    FUNNY_2(Sound.ENTITY_SLIME_JUMP),
    SURPRISE(Sound.ENTITY_SHULKER_TELEPORT);

    interface SoundMaker
    {
        fun playSound(sound: Sound, volume: Float, pitch: Float)
    }

    class PlayerSoundMaker(private val player: Player) : SoundMaker
    {
        override fun playSound(sound: Sound, volume: Float, pitch: Float)
        {
            player.playSound(player.location, sound, volume, pitch)
        }
    }

    class WorldSoundMaker(private val loc: Location) : SoundMaker
    {
        override fun playSound(sound: Sound, volume: Float, pitch: Float)
        {
            loc.world?.playSound(loc, sound, volume, pitch)
        }
    }

    private fun play(plugin: CV, soundMaker: SoundMaker)
    {
        if (plugin.config.getBoolean(Setting.USE_SOUND_EFFECTS.path))
        {
            try
            {
                soundMaker.playSound(if (`try` != null) Sound.valueOf(`try`) else sound, 10f, 1f)
            } catch (_: Exception)
            {
                soundMaker.playSound(sound, 10f, 1f)
            }
        }
    }

    fun play(plugin: CV, player: Player)
    {
        play(plugin, PlayerSoundMaker(player))
    }

    fun play(plugin: CV, loc: Location)
    {
        play(plugin, WorldSoundMaker(loc))
    }

    fun playForAll(plugin: CV)
    {
        for (player in plugin.server.onlinePlayers)
        {
            play(plugin, player)
        }
    }
}