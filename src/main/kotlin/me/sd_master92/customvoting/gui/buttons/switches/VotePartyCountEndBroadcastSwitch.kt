package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class VotePartyCountEndBroadcastSwitch(private val plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.FIREWORK_ROCKET,
    Setting.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING,
    PMessage.VOTE_PARTY_ITEM_NAME_BROADCAST_COUNTDOWN_END,
    true
)
{
    override fun newInstance(): ItemStack
    {
        return VotePartyCountEndBroadcastSwitch(plugin)
    }
}