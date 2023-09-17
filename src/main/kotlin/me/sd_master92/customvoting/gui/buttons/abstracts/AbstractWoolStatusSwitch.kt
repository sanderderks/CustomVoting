package me.sd_master92.customvoting.gui.buttons.abstracts

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import org.bukkit.Material

abstract class AbstractWoolStatusSwitch(
    plugin: CV,
    setting: Setting,
    name: PMessage,
    reverse: Boolean = false
) : AbstractStatusSwitch(plugin, Material.RED_WOOL, setting, name, reverse)
{
    init
    {
        val positive = if (!reverse) Material.GREEN_WOOL else Material.RED_WOOL
        val negative = if (!reverse) Material.RED_WOOL else Material.GREEN_WOOL
        type = if (plugin.config.getBoolean(setting.path)) positive else negative
    }
}