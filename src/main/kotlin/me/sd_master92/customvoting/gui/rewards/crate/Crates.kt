package me.sd_master92.customvoting.gui.rewards.crate

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.rewards.RewardSettings
import me.sd_master92.customvoting.stripColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class Crates(private val plugin: CV) :
    GUI(plugin, PMessage.CRATE_OVERVIEW_INVENTORY_NAME.toString(), getInventorySize(plugin))
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

            Material.TRIPWIRE_HOOK  ->
            {
                val key = item.itemMeta?.lore?.get(0)?.split("#")?.get(1)?.stripColor()
                try
                {
                    val number: Int = key!!.toInt()
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent = true
                    player.openInventory(CrateSettings(plugin, number).inventory)
                } catch (e: Exception)
                {
                    SoundType.FAILURE.play(plugin, player)
                }
            }

            Material.CRAFTING_TABLE ->
            {
                val numbers = plugin.data.getConfigurationSection(Data.VOTE_CRATES.path)?.getKeys(false)?.map {
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
                plugin.data.set(Data.VOTE_CRATES.path + ".$i.name", PMessage.CRATE_NAME_DEFAULT_X.with("$i"))
                plugin.data.saveConfig()
                cancelCloseEvent = true
                player.openInventory(Crates(plugin).inventory)
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
            val crates = (plugin.data.getConfigurationSection(Data.VOTE_CRATES.path)?.getKeys(false)?.size ?: 0) + 2
            return if (crates % 9 == 0)
            {
                crates
            } else
            {
                crates + (9 - (crates % 9))
            }
        }
    }

    init
    {
        for (key in plugin.data.getConfigurationSection(Data.VOTE_CRATES.path)?.getKeys(false)?.mapNotNull { key ->
            key.toIntOrNull()
        }?.sorted() ?: listOf())
        {
            inventory.addItem(
                BaseItem(
                    Material.TRIPWIRE_HOOK,
                    PMessage.CRATE_NAME_X.with(
                        plugin.data.getString(Data.VOTE_CRATES.path + ".$key.name")
                            ?: PMessage.CRATE_NAME_DEFAULT_X.with("$key")
                    ),
                    PMessage.CRATE_ITEM_LORE_KEY_X.with("$key"), true
                )
            )
        }
        inventory.setItem(7, BaseItem(Material.CRAFTING_TABLE, PMessage.CRATE_ITEM_NAME_ADD.toString()))
        inventory.setItem(8, BACK_ITEM)
    }
}