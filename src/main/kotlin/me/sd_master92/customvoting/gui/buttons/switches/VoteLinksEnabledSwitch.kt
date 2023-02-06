package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material

class VoteLinksEnabledSwitch(plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.CHEST, Setting.VOTE_LINK_INVENTORY,
    PMessage.VOTE_LINKS_ITEM_NAME_GUI
)
{
    override fun newInstance(plugin: CV): AbstractStatusSwitch
    {
        return VoteLinksEnabledSwitch(plugin)
    }
}