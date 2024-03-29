package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractWoolStatusSwitch
import org.bukkit.inventory.ItemStack

class StreakBroadcastSwitch(private val plugin: CV) : AbstractWoolStatusSwitch(
    plugin,
    Setting.DISABLED_BROADCAST_STREAK,
    PMessage.STREAK_ITEM_NAME_BROADCAST
)
{
    override fun newInstance(): ItemStack
    {
        return StreakBroadcastSwitch(plugin)
    }
}