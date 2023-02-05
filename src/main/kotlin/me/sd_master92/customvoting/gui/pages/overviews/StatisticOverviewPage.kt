package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.BStatsRefreshAction
import me.sd_master92.customvoting.gui.buttons.descriptions.CountryDescription
import me.sd_master92.customvoting.gui.buttons.descriptions.MCVersionDescription
import me.sd_master92.customvoting.gui.buttons.descriptions.TopVoteSitesDescription
import me.sd_master92.customvoting.gui.pages.settings.SupportPage
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class StatisticOverviewPage(private val plugin: CV) : GUI(plugin, PMessage.STATISTICS_INVENTORY_NAME.toString(), 9)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        SupportPage(plugin).open(player)
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