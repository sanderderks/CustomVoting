package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class VoteStreakConsecutiveSwitch(private val plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.REPEATER, Setting.VOTE_STREAK_CONSECUTIVE,
    PMessage.VOTE_STREAK_ITEM_NAME_CONSECUTIVE
)
{
    override fun newInstance(): ItemStack
    {
        return VoteStreakConsecutiveSwitch(plugin)
    }
}