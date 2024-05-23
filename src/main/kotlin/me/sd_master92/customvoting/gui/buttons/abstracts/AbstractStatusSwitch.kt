package me.sd_master92.customvoting.gui.buttons.abstracts

import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

abstract class AbstractStatusSwitch(
    private val plugin: CV,
    mat: Material,
    private val path: String,
    name: String,
    reverse: Boolean = false
) : StatusItem(mat, name, plugin.config, path, reverse)
{
    constructor(
        plugin: CV,
        mat: Material,
        setting: Setting,
        name: PMessage,
        reverse: Boolean = false
    ) : this(plugin, mat, setting.path, name.toString(), reverse)

    abstract fun newInstance(): ItemStack

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(path, !plugin.config.getBoolean(path))
        plugin.config.saveConfig()
        event.currentItem = newInstance()
    }
}