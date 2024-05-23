package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.capitalize
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.getDisplayName
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.time.DayOfWeek

class DoubleRewardDaySwitch(private val plugin: CV, private val setting: Setting, private val day: DayOfWeek) :
    AbstractStatusSwitch(
        plugin,
        Material.CLOCK, "$setting.${day.toString().lowercase()}",
        PMessage.DOUBLE_REWARDS_ITEM_NAME_X.with(day.getDisplayName(plugin).lowercase().capitalize())
    )
{
    override fun newInstance(): ItemStack
    {
        return DoubleRewardDaySwitch(plugin, setting, day)
    }
}