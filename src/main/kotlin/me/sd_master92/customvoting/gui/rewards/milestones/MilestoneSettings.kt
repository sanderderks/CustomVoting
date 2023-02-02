package me.sd_master92.customvoting.gui.rewards.milestones

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.items.CommandsRewardItem
import me.sd_master92.customvoting.gui.items.ItemsRewardItem
import me.sd_master92.customvoting.listeners.PlayerCommandInput
import me.sd_master92.customvoting.listeners.PlayerPermissionInput
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class MilestoneSettings(private val plugin: CV, private val number: Int) :
    GUI(plugin, "Vote Milestone Settings #$number", 9)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER       ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(Milestones(plugin).inventory)
            }

            Material.RED_WOOL      ->
            {
                SoundType.FAILURE.play(plugin, player)
                plugin.data.delete(Data.MILESTONES + ".$number")
                cancelCloseEvent = true
                player.openInventory(Milestones(plugin).inventory)
            }

            Material.DIAMOND_SWORD ->
            {
                SoundType.CHANGE.play(plugin, player)
                cancelCloseEvent = true
                player.closeInventory()
                object : PlayerPermissionInput(plugin, player, Data.MILESTONES + ".$number.permissions")
                {
                    override fun onPermissionReceived()
                    {
                        SoundType.SUCCESS.play(plugin, player)
                        player.openInventory(MilestoneSettings(plugin, number).inventory)
                    }

                    override fun onCancel()
                    {
                        SoundType.FAILURE.play(plugin, player)
                        player.openInventory(MilestoneSettings(plugin, number).inventory)
                    }
                }
            }

            Material.SHIELD        ->
            {
                SoundType.CHANGE.play(plugin, player)
                cancelCloseEvent = true
                player.closeInventory()
                object : PlayerCommandInput(plugin, player, Data.MILESTONES + ".$number.commands")
                {
                    override fun onCommandReceived()
                    {
                        SoundType.SUCCESS.play(plugin, player)
                        player.openInventory(MilestoneSettings(plugin, number).inventory)
                    }

                    override fun onCancel()
                    {
                        SoundType.FAILURE.play(plugin, player)
                        player.openInventory(MilestoneSettings(plugin, number).inventory)
                    }
                }
            }

            Material.CHEST         ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(MilestoneItemRewards(plugin, number).inventory)
            }

            else                   ->
            {
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    companion object
    {
        val DELETE_ITEM = BaseItem(Material.RED_WOOL, ChatColor.RED.toString() + "Delete")
    }

    init
    {
        inventory.addItem(ItemsRewardItem(plugin, "${Data.MILESTONES}.$number.${Data.ITEM_REWARDS}"))
        inventory.addItem(CommandsRewardItem(plugin, "${Data.MILESTONES}.$number.commands", Material.SHIELD))
        inventory.addItem(PermissionsRewardItem(plugin, "${Data.MILESTONES}.$number.permissions"))
        inventory.setItem(7, DELETE_ITEM)
        inventory.setItem(8, BACK_ITEM)
    }
}

class PermissionsRewardItem(plugin: CV, path: String) : BaseItem(
    Material.DIAMOND_SWORD, ChatColor.LIGHT_PURPLE.toString() + "Permission Rewards",
    ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.data.getStringList(path).size + ChatColor.GRAY + " permissions"
)