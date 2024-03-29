package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractWoolStatusSwitch
import org.bukkit.inventory.ItemStack

class VotePartyCountBroadcastSwitch(private val plugin: CV) : AbstractWoolStatusSwitch(
    plugin,
    Setting.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN,
    PMessage.VOTE_PARTY_ITEM_NAME_BROADCAST_COUNT,
    true
)
{
    override fun newInstance(): ItemStack
    {
        return VotePartyCountBroadcastSwitch(plugin)
    }
}