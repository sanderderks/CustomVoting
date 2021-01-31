package me.sd_master92.customvoting.constants.types;

import org.bukkit.Sound;

public enum SoundType
{
    SUCCESS(Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    FAILURE(Sound.BLOCK_ANVIL_LAND),
    NOT_ALLOWED(Sound.BLOCK_ANVIL_PLACE),
    CHANGE(Sound.UI_BUTTON_CLICK),
    CLICK(Sound.BLOCK_NOTE_BLOCK_PLING),
    OPEN(Sound.BLOCK_ENDER_CHEST_OPEN),
    CLOSE(Sound.BLOCK_ENDER_CHEST_CLOSE);

    private final Sound sound;

    SoundType(final Sound sound)
    {
        this.sound = sound;
    }

    public Sound getSound()
    {
        return sound;
    }
}
