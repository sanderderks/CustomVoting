package me.sd_master92.customvoting.gui.rewards.milestones

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.gui.items.CommandsRewardItem
import me.sd_master92.customvoting.gui.items.ItemsRewardItem
import me.sd_master92.customvoting.listeners.PlayerCommandInput
import me.sd_master92.customvoting.listeners.PlayerPermissionInput
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class MilestoneSettings(private val plugin: CV, private val number: Int) :
    GUI(plugin, Strings.MILESTONE_SETTINGS_TITLE_X.with("$number"), 9)
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
                plugin.data.delete(Data.MILESTONES.path + ".$number")
                player.sendMessage(Strings.MILESTONE_DELETED_X.with(Strings.MILESTONE_NAME_X.with("$number")))
                cancelCloseEvent = true
                player.openInventory(Milestones(plugin).inventory)
            }

            Material.DIAMOND_SWORD ->
            {
                SoundType.CHANGE.play(plugin, player)
                cancelCloseEvent = true
                player.closeInventory()
                object : PlayerPermissionInput(plugin, player, Data.MILESTONES.path + ".$number.permissions")
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
                object : PlayerCommandInput(plugin, player, Data.MILESTONES.path + ".$number.commands")
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
        val DELETE_ITEM = BaseItem(Material.RED_WOOL, Strings.GUI_DELETE.toString())
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
    Material.DIAMOND_SWORD, Strings.PERMISSION_REWARDS.toString(),
    Strings.GUI_CURRENT_XY.with("" + plugin.data.getStringList(path).size, "permissions")
)