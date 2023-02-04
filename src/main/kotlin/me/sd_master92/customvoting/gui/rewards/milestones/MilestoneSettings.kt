package me.sd_master92.customvoting.gui.rewards.milestones

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.items.CommandsRewardItem
import me.sd_master92.customvoting.gui.items.ItemsRewardItem
import me.sd_master92.customvoting.listeners.PlayerCommandInput
import me.sd_master92.customvoting.listeners.PlayerPermissionInput
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class MilestoneSettings(private val plugin: CV, private val number: Int) :
    GUI(plugin, PMessage.MILESTONE_INVENTORY_NAME_X.with("$number"), 9)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        Milestones(plugin).open(player)
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
        addItem(object : ItemsRewardItem(plugin, "${Data.MILESTONES}.$number.${Data.ITEM_REWARDS}")
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                MilestoneItemRewards(plugin, number).open(player)
            }
        })
        addItem(object : CommandsRewardItem(plugin, "${Data.MILESTONES}.$number.commands", Material.SHIELD)
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
            {
                SoundType.CHANGE.play(plugin, player)
                cancelCloseEvent = true
                player.closeInventory()
                object : PlayerCommandInput(plugin, player, "${Data.MILESTONES}.$number.commands")
                {
                    override fun onCommandReceived()
                    {
                        SoundType.SUCCESS.play(plugin, player)
                        MilestoneSettings(plugin, number).open(player)
                    }

                    override fun onCancel()
                    {
                        SoundType.FAILURE.play(plugin, player)
                        MilestoneSettings(plugin, number).open(player)
                    }
                }
            }
        })
        addItem(object : BaseItem(
            Material.DIAMOND_SWORD, PMessage.PERMISSION_REWARDS_ITEM_NAME.toString(),
            PMessage.GENERAL_ITEM_LORE_CURRENT_XY.with(
                "" + plugin.data.getStringList("${Data.MILESTONES}.$number.permissions").size,
                "permissions"
            )
        )
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
            {
                SoundType.CHANGE.play(plugin, player)
                cancelCloseEvent = true
                player.closeInventory()
                object : PlayerPermissionInput(plugin, player, "${Data.MILESTONES}.$number.permissions")
                {
                    override fun onPermissionReceived()
                    {
                        SoundType.SUCCESS.play(plugin, player)
                        MilestoneSettings(plugin, number).open(player)
                    }

                    override fun onCancel()
                    {
                        SoundType.FAILURE.play(plugin, player)
                        MilestoneSettings(plugin, number).open(player)
                    }
                }
            }
        })
        setItem(7, object : BaseItem(Material.RED_WOOL, PMessage.GENERAL_ITEM_NAME_DELETE.toString())
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
            {
                SoundType.FAILURE.play(plugin, player)
                plugin.data.delete(Data.MILESTONES.path + ".$number")
                player.sendMessage(PMessage.MILESTONE_MESSAGE_DELETED_X.with(PMessage.MILESTONE_NAME_X.with("$number")))
                cancelCloseEvent = true
                Milestones(plugin).open(player)
            }
        })
    }
}