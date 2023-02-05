package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class ArmorStandBreakMessageSwitch(private val plugin: CV) : StatusItem(
    Material.ARMOR_STAND, PMessage.VOTE_TOP_ITEM_NAME_STAND_BREAK_MESSAGE.toString(),
    plugin.config, Setting.DISABLED_MESSAGE_ARMOR_STAND.path,
    true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(
            Setting.DISABLED_MESSAGE_ARMOR_STAND.path,
            !plugin.config.getBoolean(Setting.DISABLED_MESSAGE_ARMOR_STAND.path)
        )
        plugin.config.saveConfig()
        event.currentItem = ArmorStandBreakMessageSwitch(plugin)
    }
}