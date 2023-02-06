package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material

class SoundEffectsSwitch(plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.MUSIC_DISC_CAT, Setting.USE_SOUND_EFFECTS,
    PMessage.SOUND_EFFECTS_ITEM_NAME
)
{
    override fun newInstance(plugin: CV): AbstractStatusSwitch
    {
        return SoundEffectsSwitch(plugin)
    }
}