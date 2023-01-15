package me.sd_master92.customvoting.gui.support

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.models.BStatsData
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.roundToInt

class Statistics(private val plugin: CV) : GUI(plugin, "Statistics", 9)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(Support(plugin).inventory)
            }

            Material.CLOCK   ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                BStatsData.refresh()
                player.openInventory(Statistics(plugin).inventory)
            }

            else             ->
            {
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    init
    {
        val voteSites = BStatsData.VOTE_SITES.sortedByDescending { it.y }
        inventory.addItem(
            BaseItem(
                Material.CARVED_PUMPKIN, ChatColor.LIGHT_PURPLE.toString() + "Top vote sites",
                ChatColor.GRAY.toString() + "Top vote sites by;" + ChatColor.GRAY + "CustomVoting users:;" +
                        voteSites.take(8).mapIndexed { i, site ->
                            ";" + ChatColor.AQUA + "${i + 1}. " + (if (plugin.data.getStringList(Data.VOTE_SITES)
                                    .contains(site.name)
                            ) ChatColor.GREEN else ChatColor.RED) + site.name + ChatColor.GRAY + " ${(site.y.toDouble() / voteSites.sumOf { it.y } * 100).roundToInt()}%"
                        }.joinToString("") + ";;" + ChatColor.GRAY + "RED = not setup yet"
            )
        )
        inventory.addItem(
            BaseItem(
                Material.GRASS_BLOCK, ChatColor.LIGHT_PURPLE.toString() + "Minecraft Version",
                ChatColor.GRAY.toString() + "Most popular version: " + ChatColor.GREEN +
                        BStatsData.MINECRAFT_VERSIONS.maxByOrNull { it.y }!!.name
            )
        )
        val locations = BStatsData.COUNTRIES.sortedByDescending { it.y }
        inventory.addItem(
            BaseItem(
                Material.FILLED_MAP, ChatColor.LIGHT_PURPLE.toString() + "Country",
                ChatColor.GRAY.toString() + "Most popular in:;" +
                        locations.take(3).mapIndexed { i, country ->
                            ";" + ChatColor.GRAY.toString() + "${i + 1}. " + ChatColor.AQUA + country.name +
                                    ChatColor.GRAY + " ${(country.y.toDouble() / locations.sumOf { it.y } * 100).roundToInt()}%"
                        }.joinToString("")
            )
        )
        inventory.setItem(7, BaseItem(Material.CLOCK, ChatColor.RED.toString() + "Refresh"))
        inventory.setItem(8, BACK_ITEM)
    }
}