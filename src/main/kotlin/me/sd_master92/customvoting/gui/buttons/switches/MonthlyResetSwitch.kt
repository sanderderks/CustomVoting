package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class MonthlyResetSwitch(private val plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.CLOCK, Setting.MONTHLY_RESET,
    PMessage.RESET_VOTES_ITEM_NAME_MONTHLY
)
{
    override fun newInstance(): ItemStack
    {
        return MonthlyResetSwitch(plugin)
    }
}