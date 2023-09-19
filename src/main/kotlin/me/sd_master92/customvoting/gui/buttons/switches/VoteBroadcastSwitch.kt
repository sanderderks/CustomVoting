package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractWoolStatusSwitch
import org.bukkit.inventory.ItemStack

class VoteBroadcastSwitch(private val plugin: CV) : AbstractWoolStatusSwitch(
    plugin,
    Setting.DISABLED_BROADCAST_VOTE,
    PMessage.VOTE_ITEM_NAME_BROADCAST,
    true
)
{
    override fun newInstance(): ItemStack
    {
        return VoteBroadcastSwitch(plugin)
    }
}