package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.input.PlayerNumberInput
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

class MilestoneCreateAction(private val plugin: CV, private val backPage: GUI) :
    BaseItem(Material.CRAFTING_TABLE, PMessage.MILESTONE_ITEM_NAME_ADD.toString())
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        backPage.cancelCloseEvent = true
        player.closeInventory()
        player.sendMessage(PMessage.MILESTONE_MESSAGE_NUMBER_ENTER.toString())
        player.sendMessage(PMessage.GENERAL_MESSAGE_CANCEL_BACK.toString())
        object : PlayerNumberInput(plugin, player)
        {
            override fun onNumberReceived(input: Int)
            {
                val name = PMessage.MILESTONE_ITEM_NAME_X.with("$input").stripColor()
                if (plugin.data.contains(Data.MILESTONES.path + ".$input"))
                {
                    player.sendMessage(PMessage.GENERAL_ERROR_ALREADY_EXIST_X.with(name))
                } else
                {
                    SoundType.SUCCESS.play(plugin, player)
                    plugin.data.set(Data.MILESTONES.path + ".$input.permissions", ArrayList<String>())
                    plugin.data.saveConfig()
                    player.sendMessage(PMessage.GENERAL_MESSAGE_CREATE_SUCCESS_X.with(name))
                    MilestoneOverviewPage(plugin, backPage).open(player)
                    cancel()
                }
            }

            override fun onCancel()
            {
                SoundType.FAILURE.play(plugin, player)
                MilestoneOverviewPage(plugin, backPage).open(player)
            }
        }
    }
}