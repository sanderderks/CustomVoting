package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class SoundEffectsSwitch(private val plugin: CV) : StatusItem(
    Material.MUSIC_DISC_CAT, PMessage.SOUND_EFFECTS_ITEM_NAME.toString(),
    plugin.config, Setting.USE_SOUND_EFFECTS.path
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config[Setting.USE_SOUND_EFFECTS.path] =
            !plugin.config.getBoolean(Setting.USE_SOUND_EFFECTS.path)
        plugin.config.saveConfig()
        event.currentItem = SoundEffectsSwitch(plugin)
    }
}