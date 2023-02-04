package me.sd_master92.customvoting.gui.rewards.milestones

import me.sd_master92.core.input.PlayerNumberInput
import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.rewards.RewardSettings
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class Milestones(private val plugin: CV) :
    GUI(plugin, PMessage.MILESTONE_INVENTORY_NAME_OVERVIEW.toString(), getInventorySize(plugin))
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        RewardSettings(plugin).open(player)
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

    companion object
    {
        private fun getInventorySize(plugin: CV): Int
        {
            val milestones = (plugin.data.getConfigurationSection(Data.MILESTONES.path)?.getKeys(false)?.size ?: 0) + 2
            return if (milestones % 9 == 0)
            {
                milestones
            } else
            {
                milestones + (9 - (milestones % 9))
            }
        }
    }

    init
    {
        setItem(7, object : BaseItem(Material.CRAFTING_TABLE, PMessage.MILESTONE_ITEM_NAME_ADD.toString())
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
            {
                SoundType.CHANGE.play(plugin, player)
                cancelCloseEvent = true
                player.closeInventory()
                player.sendMessage(PMessage.MILESTONE_MESSAGE_NUMBER_ENTER.toString())
                player.sendMessage(PMessage.GENERAL_MESSAGE_CANCEL_BACK.toString())
                object : PlayerNumberInput(plugin, player)
                {
                    override fun onNumberReceived(input: Int)
                    {
                        val name = PMessage.MILESTONE_NAME_X.with("$input")
                        if (plugin.data.contains(Data.MILESTONES.path + ".$input"))
                        {
                            player.sendMessage(PMessage.GENERAL_ERROR_ALREADY_EXIST_X.with(name))
                        } else
                        {
                            SoundType.SUCCESS.play(plugin, player)
                            plugin.data.set(Data.MILESTONES.path + ".$input.permissions", ArrayList<String>())
                            plugin.data.saveConfig()
                            player.sendMessage(PMessage.GENERAL_MESSAGE_CREATE_SUCCESS_X.with(name))
                            Milestones(plugin).open(player)
                            cancel()
                        }
                    }

                    override fun onCancel()
                    {
                        SoundType.FAILURE.play(plugin, player)
                        Milestones(plugin).open(player)
                    }
                }
            }
        })
        for (key in plugin.data.getConfigurationSection(Data.MILESTONES.path)?.getKeys(false)?.mapNotNull { key ->
            key.toIntOrNull()
        }?.sorted() ?: ArrayList())
        {
            addItem(
                object : BaseItem(
                    Material.ENDER_PEARL,
                    PMessage.PURPLE.toString() + PMessage.MILESTONE_NAME_X.with("$key")
                )
                {
                    override fun onClick(event: InventoryClickEvent, player: Player)
                    {
                        SoundType.CLICK.play(plugin, player)
                        cancelCloseEvent = true
                        MilestoneSettings(plugin, key).open(player)
                    }
                }
            )
        }
    }
}