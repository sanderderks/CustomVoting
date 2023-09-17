package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractWoolStatusSwitch
import org.bukkit.inventory.ItemStack

class WorldEnabledMessageSwitch(private val plugin: CV) : AbstractWoolStatusSwitch(
    plugin,
    Setting.DISABLED_MESSAGE_DISABLED_WORLD,
    PMessage.DISABLED_WORLD_ITEM_NAME_MESSAGE
)
{
    override fun newInstance(): ItemStack
    {
        return WorldEnabledMessageSwitch(plugin)
    }
}