package me.sd_master92.customvoting.gui.buttons.editors

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.settings.MilestoneSettingsPage
import me.sd_master92.customvoting.listeners.PlayerPermissionInput
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class MilestoneRewardPermissionsEditor(private val plugin: CV, private val gui: GUI, private val number: Int) :
    BaseItem(
        Material.DIAMOND_SWORD, PMessage.PERMISSION_REWARDS_ITEM_NAME.toString()
    )
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        gui.cancelCloseEvent = true
        player.closeInventory()
        object : PlayerPermissionInput(plugin, player, "${Data.MILESTONES}.$number.permissions")
        {
            override fun onPermissionReceived()
            {
                SoundType.SUCCESS.play(plugin, player)
                MilestoneSettingsPage(plugin, number).open(player)
            }

            override fun onCancel()
            {
                SoundType.FAILURE.play(plugin, player)
                MilestoneSettingsPage(plugin, number).open(player)
            }
        }
    }

    init
    {
        val size = plugin.data.getStringList("${Data.MILESTONES}.$number.permissions").size
        setLore(
            PMessage.GENERAL_ITEM_LORE_CURRENT_XY.with(
                "$size",
                PMessage.PERMISSION_REWARDS_UNIT_MULTIPLE.toString()
            )
        )
    }
}