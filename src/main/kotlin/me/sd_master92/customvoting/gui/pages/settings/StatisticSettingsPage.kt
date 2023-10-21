package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.BStatsRefreshAction
import me.sd_master92.customvoting.gui.buttons.descriptions.CountryDescription
import me.sd_master92.customvoting.gui.buttons.descriptions.MCVersionDescription
import me.sd_master92.customvoting.gui.buttons.descriptions.TopVoteSitesDescription
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class StatisticSettingsPage(private val plugin: CV, backPage: GUI?) :
    GUI(plugin, backPage, PMessage.STATISTICS_INVENTORY_NAME.toString())
{
    override fun newInstance(): GUI
    {
        return StatisticSettingsPage(plugin, backPage)
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
        addItem(TopVoteSitesDescription(plugin))
        addItem(MCVersionDescription())
        addItem(CountryDescription())
        setItem(7, BStatsRefreshAction(plugin, this))
    }
}