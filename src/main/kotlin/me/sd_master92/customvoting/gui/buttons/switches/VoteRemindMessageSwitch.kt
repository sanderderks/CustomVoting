package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material

class VoteRemindMessageSwitch(plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.OAK_SIGN, Setting.DISABLED_MESSAGE_VOTE_REMINDER,
    PMessage.VOTE_REMINDER_ITEM_NAME
)
{
    override fun newInstance(plugin: CV): AbstractStatusSwitch
    {
        return VoteRemindMessageSwitch(plugin)
    }
}