package me.sd_master92.customvoting.gui.rewards.crate

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

class VoteCrates(private val plugin: CV) :
    GUI(plugin, "Vote Crates", getInventorySize(plugin), false, true)
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

            Material.TRIPWIRE_HOOK  ->
            {
                val key = item.itemMeta?.displayName?.split("#")?.get(1)
                try
                {
                    val number: Int = key!!.toInt()
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent()
                    player.openInventory(VoteCrateSettings(plugin, number).inventory)
                } catch (e: Exception)
                {
                    SoundType.FAILURE.play(plugin, player)
                }
            }

            Material.CRAFTING_TABLE ->
            {
                val numbers = plugin.data.getConfigurationSection(Data.VOTE_CRATES)?.getKeys(false)?.map {
                    try
                    {
                        it.toInt()
                    } catch (e: Exception)
                    {
                        0
                    }
                } ?: listOf()
                var i = 1
                while (numbers.contains(i))
                {
                    i++
                }
                SoundType.SUCCESS.play(plugin, player)
                plugin.data.set(Data.VOTE_CRATES + ".$i.name", "Crate $i")
                plugin.data.saveConfig()
                player.closeInventory()
                player.openInventory(VoteCrates(plugin).inventory)
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
            val streaks = (plugin.data.getConfigurationSection(Data.VOTE_CRATES)?.getKeys(false)?.size ?: 0) + 2
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
        for (key in plugin.data.getConfigurationSection(Data.VOTE_CRATES)?.getKeys(false)?.mapNotNull { key ->
            key.toIntOrNull()
        }?.sorted() ?: listOf())
        {
            inventory.addItem(
                BaseItem(
                    Material.TRIPWIRE_HOOK,
                    ChatColor.LIGHT_PURPLE.toString() + (plugin.data.getString(Data.VOTE_CRATES + ".$key.name")
                        ?: "Vote Crate") + " #" + key
                )
            )
        }
        inventory.setItem(7, BaseItem(Material.CRAFTING_TABLE, ChatColor.GREEN.toString() + "Add Crate"))
        inventory.setItem(8, BACK_ITEM)
    }
}