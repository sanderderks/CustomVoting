package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.switches.DoubleRewardDaySwitch
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import java.time.DayOfWeek

class DoubleRewardSettingsPage(private val plugin: CV, backPage: GUI?, private val setting: Setting) :
    GUI(plugin, backPage, PMessage.MESSAGE_SETTINGS_INVENTORY_NAME.toString(), { 18 })
{
    override fun newInstance(): GUI
    {
        return DoubleRewardSettingsPage(plugin, backPage, setting)
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
        for (day in DayOfWeek.values())
        {
            addItem(DoubleRewardDaySwitch(plugin, setting, day))
        }
    }
}