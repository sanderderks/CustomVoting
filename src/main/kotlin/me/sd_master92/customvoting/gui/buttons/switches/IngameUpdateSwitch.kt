package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class IngameUpdateSwitch(private val plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.FILLED_MAP, Setting.INGAME_UPDATES,
    PMessage.PLUGIN_UPDATE_ITEM_NAME_INGAME
)
{
    override fun newInstance(): ItemStack
    {
        return IngameUpdateSwitch(plugin)
    }
}