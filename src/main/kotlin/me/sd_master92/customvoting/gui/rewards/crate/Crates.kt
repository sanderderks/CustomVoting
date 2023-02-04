package me.sd_master92.customvoting.gui.rewards.crate

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

class Crates(private val plugin: CV) :
    GUI(plugin, PMessage.CRATE_OVERVIEW_INVENTORY_NAME.toString(), getInventorySize(plugin))
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
            addItem(
                object : BaseItem(
                    Material.TRIPWIRE_HOOK,
                    PMessage.CRATE_NAME_X.with(
                        plugin.data.getString(Data.VOTE_CRATES.path + ".$key.name")
                            ?: PMessage.CRATE_NAME_DEFAULT_X.with("$key")
                    ),
                    PMessage.CRATE_ITEM_LORE_KEY_X.with("$key"), true
                )
                {
                    override fun onClick(event: InventoryClickEvent, player: Player)
                    {
                        SoundType.CLICK.play(plugin, player)
                        cancelCloseEvent = true
                        CrateSettings(plugin, key).open(player)
                    }
                }
            )
        }
        setItem(7, object : BaseItem(Material.CRAFTING_TABLE, PMessage.CRATE_ITEM_NAME_ADD.toString())
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
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
                Crates(plugin).open(player)
            }
        })
    }
}