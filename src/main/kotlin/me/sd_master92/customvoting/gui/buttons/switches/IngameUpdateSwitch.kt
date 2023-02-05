package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class IngameUpdateSwitch(private val plugin: CV) : StatusItem(
    Material.FILLED_MAP, PMessage.SUPPORT_ITEM_NAME_INGAME_UPDATE.toString(),
    plugin.config, Setting.INGAME_UPDATES.path
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(
            Setting.INGAME_UPDATES.path,
            !plugin.config.getBoolean(Setting.INGAME_UPDATES.path)
        )
        plugin.config.saveConfig()
        event.currentItem = IngameUpdateSwitch(plugin)
    }
}