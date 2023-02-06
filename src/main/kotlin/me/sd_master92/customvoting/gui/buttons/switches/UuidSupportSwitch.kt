package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material

class UuidSupportSwitch(plugin: CV) : AbstractStatusSwitch(
    plugin,
    Material.PLAYER_HEAD, Setting.UUID_STORAGE,
    PMessage.UUID_STORAGE_ITEM_NAME
)
{
    override fun newInstance(plugin: CV): AbstractStatusSwitch
    {
        return UuidSupportSwitch(plugin)
    }
}