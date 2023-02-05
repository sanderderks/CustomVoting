package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.overviews.MilestoneOverviewPage
import me.sd_master92.customvoting.stripColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class MilestoneDeleteAction(private val plugin: CV, private val gui: GUI, private val number: Int) :
    BaseItem(Material.RED_WOOL, PMessage.GENERAL_ITEM_NAME_DELETE.toString())
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.FAILURE.play(plugin, player)
        plugin.data.delete(Data.MILESTONES.path + ".$number")
        player.sendMessage(
            PMessage.GENERAL_MESSAGE_DELETE_SUCCESS_X.with(
                PMessage.MILESTONE_ITEM_NAME_X.with("$number").stripColor()
            )
        )
        gui.cancelCloseEvent = true
        MilestoneOverviewPage(plugin).open(player)
    }
}