package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class AllowCrateClose(private val plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.OAK_TRAPDOOR, Setting.ALLOW_CRATE_CLOSE,
    PMessage.CRATE_ITEM_NAME_ALLOW_CLOSE
)
{
    override fun newInstance(): ItemStack
    {
        return AllowCrateClose(plugin)
    }

    init
    {
        addLore(";" + PMessage.CRATE_ITEM_LORE_ALLOW_CLOSE.toString())
    }
}