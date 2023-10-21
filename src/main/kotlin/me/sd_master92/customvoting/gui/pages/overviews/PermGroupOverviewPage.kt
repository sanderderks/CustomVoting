package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.switches.PermGroupEnabledSwitch
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class PermGroupOverviewPage(private val plugin: CV, backPage: GUI?) :
    GUI(plugin, backPage, PMessage.PERM_GROUP_OVERVIEW_INVENTORY_NAME.toString(), { 36 })
{
    override fun newInstance(): GUI
    {
        return PermGroupOverviewPage(plugin, backPage)
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    init
    {
        if (CV.PERMISSION != null)
        {
            for (group in CV.PERMISSION!!.groups)
            {
                addItem(PermGroupEnabledSwitch(plugin, group).update())
            }
        }
    }
}


