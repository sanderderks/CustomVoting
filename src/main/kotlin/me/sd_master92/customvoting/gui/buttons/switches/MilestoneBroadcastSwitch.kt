package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material

class MilestoneBroadcastSwitch(plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.ENDER_PEARL, Setting.DISABLED_BROADCAST_MILESTONE,
    PMessage.MILESTONE_ITEM_NAME_BROADCAST
)
{
    override fun newInstance(plugin: CV): AbstractStatusSwitch
    {
        return MilestoneBroadcastSwitch(plugin)
    }
}