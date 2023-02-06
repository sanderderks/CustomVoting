package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material

class WorldEnabledMessageSwitch(plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.GRASS_BLOCK, Setting.DISABLED_MESSAGE_DISABLED_WORLD,
    PMessage.DISABLED_WORLD_ITEM_NAME_MESSAGE
)
{
    override fun newInstance(plugin: CV): AbstractStatusSwitch
    {
        return WorldEnabledMessageSwitch(plugin)
    }
}