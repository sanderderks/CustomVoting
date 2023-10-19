package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class VotePartyEnabledSwitch(private val plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.EXPERIENCE_BOTTLE, Setting.VOTE_PARTY_ENABLED,
    PMessage.VOTE_PARTY_ITEM_NAME
)
{
    override fun newInstance(): ItemStack
    {
        return VotePartyEnabledSwitch(plugin)
    }
}