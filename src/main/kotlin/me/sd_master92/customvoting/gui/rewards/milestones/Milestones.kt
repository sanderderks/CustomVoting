package me.sd_master92.customvoting.gui.rewards.milestones

import me.sd_master92.core.input.PlayerNumberInput
import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.gui.rewards.RewardSettings
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class Milestones(private val plugin: CV) :
    GUI(plugin, Strings.MILESTONE_INVENTORY_NAME_OVERVIEW.toString(), getInventorySize(plugin))
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER        ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(RewardSettings(plugin).inventory)
            }

            Material.ENDER_PEARL    ->
            {
                val key = item.itemMeta?.displayName?.split("#")?.get(1)
                try
                {
                    val number: Int = key!!.toInt()
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent = true
                    player.openInventory(MilestoneSettings(plugin, number).inventory)
                } catch (e: Exception)
                {
                    SoundType.FAILURE.play(plugin, player)
                }
            }

            Material.CRAFTING_TABLE ->
            {
                SoundType.CHANGE.play(plugin, player)
                cancelCloseEvent = true
                player.closeInventory()
                player.sendMessage(Strings.MILESTONE_MESSAGE_NUMBER_ENTER.toString())
                player.sendMessage(Strings.GENERAL_MESSAGE_CANCEL_BACK.toString())
                object : PlayerNumberInput(plugin, player)
                {
                    override fun onNumberReceived(input: Int)
                    {
                        val name = Strings.MILESTONE_NAME_X.with("$input")
                        if (plugin.data.contains(Data.MILESTONES.path + ".$input"))
                        {
                            player.sendMessage(Strings.GENERAL_ERROR_ALREADY_EXIST_X.with(name))
                        } else
                        {
                            SoundType.SUCCESS.play(plugin, player)
                            plugin.data.set(Data.MILESTONES.path + ".$input.permissions", ArrayList<String>())
                            plugin.data.saveConfig()
                            player.sendMessage(Strings.GENERAL_MESSAGE_CREATE_SUCCESS_X.with(name))
                            player.openInventory(Milestones(plugin).inventory)
                            cancel()
                        }
                    }

                    override fun onCancel()
                    {
                        SoundType.FAILURE.play(plugin, player)
                        player.openInventory(Milestones(plugin).inventory)
                    }
                }
            }

            else                    ->
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
        inventory.setItem(7, BaseItem(Material.CRAFTING_TABLE, Strings.MILESTONE_ITEM_NAME_ADD.toString()))
        inventory.setItem(8, BACK_ITEM)

        for (key in plugin.data.getConfigurationSection(Data.MILESTONES.path)?.getKeys(false)?.mapNotNull { key ->
            key.toIntOrNull()
        }?.sorted() ?: ArrayList())
        {
            inventory.addItem(
                BaseItem(
                    Material.ENDER_PEARL,
                    ChatColor.LIGHT_PURPLE.toString() + Strings.MILESTONE_NAME_X.with("$key")
                )
            )
        }
    }
}