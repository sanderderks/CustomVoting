package me.sd_master92.customvoting.gui.buttons.abstracts

import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.interfaces.StatusSwitch
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

abstract class AbstractStatusSwitch(
    private val plugin: CV,
    mat: Material,
    private val setting: Setting,
    name: PMessage
) : StatusItem(mat, name.toString(), plugin.config, setting.path), StatusSwitch
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(setting.path, !plugin.config.getBoolean(setting.path))
        plugin.config.saveConfig()
        event.currentItem = newInstance(plugin)
    }
}