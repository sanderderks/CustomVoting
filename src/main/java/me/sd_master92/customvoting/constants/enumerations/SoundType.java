package me.sd_master92.customvoting.constants.enumerations;

import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.constants.Settings;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;

public enum SoundType
{
    SUCCESS(Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    FAILURE(Sound.BLOCK_ANVIL_LAND),
    NOT_ALLOWED(Sound.BLOCK_ANVIL_PLACE),
    CHANGE(Sound.UI_BUTTON_CLICK),
    NOTIFY(Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    CLICK(Sound.BLOCK_NOTE_BLOCK_PLING),
    OPEN(Sound.BLOCK_ENDER_CHEST_OPEN),
    CLOSE(Sound.BLOCK_ENDER_CHEST_CLOSE),
    VOTE_PARTY_START(Sound.ENTITY_ENDER_DRAGON_GROWL),
    RECEIVE_REWARDS(Sound.ENTITY_PLAYER_LEVELUP);

    private final Sound sound;

    SoundType(final Sound sound)
    {
        this.sound = sound;
    }

    public Sound getSound()
    {
        return sound;
    }

    public void play(Main plugin, Location loc)
    {
        if(plugin.getSettings().getBoolean(Settings.USE_SOUND_EFFECTS))
        {
            World world = loc.getWorld();
            if (world != null)
            {
                world.playSound(loc, sound, 10, 1);
            }
        }
    }
}