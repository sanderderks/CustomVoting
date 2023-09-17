package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class VoteCommandOverrideSwitch(private val plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.COMMAND_BLOCK, Setting.VOTE_COMMAND_OVERRIDE,
    PMessage.VOTE_COMMAND_ITEM_NAME_OVERRIDE
)
{
    override fun newInstance(): ItemStack
    {
        return VoteCommandOverrideSwitch(plugin)
    }

    init
    {
        addLore(";" + PMessage.VOTE_COMMAND_ITEM_LORE_OVERRIDE.toString())
    }
}