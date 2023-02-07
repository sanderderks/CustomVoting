package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ArmorStandBreakMessageSwitch(private val plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.ARMOR_STAND, Setting.DISABLED_MESSAGE_ARMOR_STAND,
    PMessage.VOTE_TOP_ITEM_NAME_STAND_BREAK_MESSAGE
)
{
    override fun newInstance(): ItemStack
    {
        return ArmorStandBreakMessageSwitch(plugin)
    }
}