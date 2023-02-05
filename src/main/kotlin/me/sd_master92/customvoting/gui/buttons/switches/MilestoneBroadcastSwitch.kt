package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class MilestoneBroadcastSwitch(private val plugin: CV) : StatusItem(
    Material.ENDER_PEARL, PMessage.MILESTONE_ITEM_NAME_BROADCAST.toString(),
    plugin.config, Setting.DISABLED_BROADCAST_MILESTONE.path,
    true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(
            Setting.DISABLED_BROADCAST_MILESTONE.path,
            !plugin.config.getBoolean(Setting.DISABLED_BROADCAST_MILESTONE.path)
        )
        plugin.config.saveConfig()
        event.currentItem = MilestoneBroadcastSwitch(plugin)
    }
}