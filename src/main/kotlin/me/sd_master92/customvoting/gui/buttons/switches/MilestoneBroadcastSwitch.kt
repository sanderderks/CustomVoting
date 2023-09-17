package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractWoolStatusSwitch
import org.bukkit.inventory.ItemStack

class MilestoneBroadcastSwitch(private val plugin: CV) : AbstractWoolStatusSwitch(
    plugin,
    Setting.DISABLED_BROADCAST_MILESTONE,
    PMessage.MILESTONE_ITEM_NAME_BROADCAST,
    true
)
{
    override fun newInstance(): ItemStack
    {
        return MilestoneBroadcastSwitch(plugin)
    }
}