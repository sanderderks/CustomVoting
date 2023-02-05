package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.BStatsData
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.overviews.StatisticOverviewPage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class BStatsRefreshAction(private val plugin: CV, private val gui: GUI) :
    BaseItem(Material.CLOCK, PMessage.GENERAL_ITEM_NAME_REFRESH.toString())
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        gui.cancelCloseEvent = true
        BStatsData.refresh()
        StatisticOverviewPage(plugin).open(player)
    }
}