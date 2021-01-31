package me.sd_master92.customvoting.constants;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;

public enum Sounds
{
    SUCCESS(Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    FAILURE(Sound.BLOCK_ANVIL_LAND),
    NOT_ALLOWED(Sound.BLOCK_ANVIL_PLACE),
    CHANGE(Sound.UI_BUTTON_CLICK),
    CLICK(Sound.BLOCK_NOTE_BLOCK_PLING),
    OPEN(Sound.BLOCK_ENDER_CHEST_OPEN),
    CLOSE(Sound.BLOCK_ENDER_CHEST_CLOSE);

    private final Sound sound;

    Sounds(final Sound sound)
    {
        this.sound = sound;
    }

    public Sound getSound()
    {
        return sound;
    }

    public void play(Location loc)
    {
        World world = loc.getWorld();
        if (world != null)
        {
            world.playSound(loc, sound, 10, 1);
        }
    }
}
