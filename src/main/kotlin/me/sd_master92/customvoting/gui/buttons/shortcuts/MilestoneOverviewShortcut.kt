package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.overviews.MilestoneOverviewPage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class MilestoneOverviewShortcut(
    private val plugin: CV,
    private val backPage: GUI
) :
    BaseItem(Material.NETHER_STAR, PMessage.MILESTONE_ITEM_NAME_OVERVIEW.toString())
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        backPage.cancelCloseEvent = true
        MilestoneOverviewPage(plugin, backPage).open(player)
    }
}