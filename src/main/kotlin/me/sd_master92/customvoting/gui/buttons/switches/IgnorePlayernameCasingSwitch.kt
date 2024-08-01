package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class IgnorePlayernameCasingSwitch(private val plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.PLAYER_HEAD, Setting.IGNORE_PLAYERNAME_CASING,
    PMessage.IGNORE_PLAYERNAME_CASING_ITEM_NAME
)
{
    override fun newInstance(): ItemStack
    {
        return IgnorePlayernameCasingSwitch(plugin)
    }

    init
    {
        addLore(";" + PMessage.IGNORE_PLAYERNAME_CASING_ITEM_LORE)
    }
}