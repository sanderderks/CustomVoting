package me.sd_master92.customvoting.gui.rewards.streak

import me.sd_master92.core.input.PlayerNumberInput
import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.rewards.RewardSettings
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class VoteStreakSettings(private val plugin: CV) :
    GUI(plugin, "Vote Streak Settings", getInventorySize(plugin), false, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER        ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(RewardSettings(plugin).inventory)
            }
            Material.ENDER_PEARL    ->
            {
                val key = item.itemMeta?.displayName?.split("#")?.get(1)
                try
                {
                    val number: Int = key!!.toInt()
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent()
                    player.openInventory(VoteStreakRewards(plugin, number).inventory)
                } catch (e: Exception)
                {
                    SoundType.FAILURE.play(plugin, player)
                }
            }
            Material.CRAFTING_TABLE ->
            {
                SoundType.CHANGE.play(plugin, player)
                cancelCloseEvent()
                player.closeInventory()
                player.sendMessage(ChatColor.GREEN.toString() + "Please enter a streak number")
                player.sendMessage(ChatColor.GRAY.toString() + "Type 'cancel' to go back")
                object : PlayerNumberInput(plugin, player)
                {
                    override fun onNumberReceived(input: Int)
                    {
                        if (plugin.data.contains(Data.VOTE_STREAKS + ".$input"))
                        {
                            player.sendMessage(ChatColor.RED.toString() + "That streak already exists.")
                        } else
                        {
                            SoundType.SUCCESS.play(plugin, player)
                            plugin.data.set(Data.VOTE_STREAKS + "." + input + ".permissions", ArrayList<String>())
                            plugin.data.saveConfig()
                            player.sendMessage(ChatColor.GREEN.toString() + "Streak #$input created!")
                            player.openInventory(VoteStreakSettings(plugin).inventory)
                            cancel()
                        }
                    }

                    override fun onCancel()
                    {
                        SoundType.FAILURE.play(plugin, player)
                        player.openInventory(VoteStreakSettings(plugin).inventory)
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
            val streaks = (plugin.data.getConfigurationSection(Data.VOTE_STREAKS)?.getKeys(false)?.size ?: 0) + 2
            return if (streaks % 9 == 0)
            {
                streaks
            } else
            {
                streaks + (9 - (streaks % 9))
            }
        }
    }

    init
    {
        inventory.setItem(7, BaseItem(Material.CRAFTING_TABLE, ChatColor.GREEN.toString() + "Add Streak"))
        inventory.setItem(8, BACK_ITEM)

        for (key in plugin.data.getConfigurationSection(Data.VOTE_STREAKS)?.getKeys(false)?.mapNotNull { key ->
            key.toIntOrNull()
        }?.sorted() ?: ArrayList())
        {
            inventory.addItem(
                BaseItem(
                    Material.ENDER_PEARL,
                    ChatColor.LIGHT_PURPLE.toString() + "Streak #$key"
                )
            )
        }
    }
}