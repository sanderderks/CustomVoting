package me.sd_master92.customvoting.helpers;

import me.sd_master92.customvoting.constants.types.SoundType;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;

public class SoundHelper
{
    public static void playSound(Sound sound, Location loc)
    {
        if (sound == null)
        {
            sound = Sound.BLOCK_NOTE_BLOCK_PLING;
        }
        World world = loc.getWorld();
        if (world != null)
        {
            world.playSound(loc, sound, 10, 1);
        }
    }

    public static void playSound(SoundType soundType, Location loc)
    {
        playSound(soundType.getSound(), loc);
    }
}
