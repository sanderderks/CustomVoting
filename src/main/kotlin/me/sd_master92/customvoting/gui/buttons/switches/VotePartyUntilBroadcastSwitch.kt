package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material

class VotePartyUntilBroadcastSwitch(plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.BOOKSHELF, Setting.DISABLED_BROADCAST_VOTE_PARTY_UNTIL,
    PMessage.VOTE_PARTY_ITEM_NAME_BROADCAST_UNTIL
)
{
    override fun newInstance(plugin: CV): AbstractStatusSwitch
    {
        return VotePartyUntilBroadcastSwitch(plugin)
    }
}