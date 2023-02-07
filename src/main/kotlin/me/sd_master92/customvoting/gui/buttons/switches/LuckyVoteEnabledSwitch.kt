package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class LuckyVoteEnabledSwitch(private val plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.TOTEM_OF_UNDYING, Setting.LUCKY_VOTE,
    PMessage.LUCKY_VOTE_ITEM_NAME
)
{
    override fun newInstance(): ItemStack
    {
        return LuckyVoteEnabledSwitch(plugin)
    }
}