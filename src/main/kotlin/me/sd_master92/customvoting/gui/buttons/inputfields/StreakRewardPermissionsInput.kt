package me.sd_master92.customvoting.gui.buttons.inputfields

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.listeners.PlayerPermissionInput
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class StreakRewardPermissionsInput(
    private val plugin: CV,
    private val currentPage: GUI,
    private val number: Int
) :
    BaseItem(
        Material.DIAMOND_SWORD, PMessage.PERMISSION_REWARDS_ITEM_NAME.toString()
    )
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        currentPage.cancelCloseEvent = true
        player.closeInventory()
        object : PlayerPermissionInput(plugin, player, "${Data.STREAKS}.$number.permissions")
        {
            override fun onPermissionReceived()
            {
                SoundType.SUCCESS.play(plugin, player)
                event.currentItem = StreakRewardPermissionsInput(plugin, currentPage, number)
                currentPage.open(player)
            }

            override fun onCancel()
            {
                SoundType.FAILURE.play(plugin, player)
                currentPage.open(player)
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