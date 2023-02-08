package me.sd_master92.customvoting.gui.buttons.inputfields

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractRewardCommandsButton
import me.sd_master92.customvoting.gui.pages.settings.MilestoneSettingsPage
import me.sd_master92.customvoting.listeners.PlayerCommandInput
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class MilestoneRewardCommandsInput(private val plugin: CV, private val currentPage: GUI, private val number: Int) :
    AbstractRewardCommandsButton(plugin, "${Data.MILESTONES}.$number.commands", Material.SHIELD)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        currentPage.cancelCloseEvent = true
        player.closeInventory()
        object : PlayerCommandInput(plugin, player, "${Data.MILESTONES}.$number.commands")
        {
            override fun onCommandReceived()
            {
                SoundType.SUCCESS.play(plugin, player)
                MilestoneSettingsPage(plugin, currentPage, number).open(player)
            }

            override fun onCancel()
            {
                SoundType.FAILURE.play(plugin, player)
                MilestoneSettingsPage(plugin, currentPage, number).open(player)
            }
        }
    }
}