package me.sd_master92.customvoting.gui.support

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.BStatsData
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.Strings
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.roundToInt

class Statistics(private val plugin: CV) : GUI(plugin, Strings.GUI_TITLE_STATISTICS.toString(), 9)
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
                Material.CARVED_PUMPKIN, Strings.STATISTICS_VOTE_TOP_SITES.toString(),
                Strings.STATISTICS_VOTE_TOP_SITES_LORE.toString() +
                        voteSites.take(8).mapIndexed { i, site ->
                            ";" + ChatColor.AQUA + "${i + 1}. " + (if (plugin.data.getStringList(Data.VOTE_SITES)
                                    .contains(site.name)
                            ) ChatColor.GREEN else ChatColor.RED) + site.name + ChatColor.GRAY + " ${(site.y.toDouble() / voteSites.sumOf { it.y } * 100).roundToInt()}%"
                        }.joinToString("") + Strings.STATISTICS_VOTE_TOP_SITES_LORE_END.toString()
            )
        )
        inventory.addItem(
            BaseItem(
                Material.GRASS_BLOCK, Strings.STATISTICS_MC_VERSION.toString(),
                Strings.STATISTICS_MC_VERSION_LORE_X.with(
                    BStatsData.MINECRAFT_VERSIONS.maxByOrNull { it.y }!!.name
                )
            )
        )
        val locations = BStatsData.COUNTRIES.sortedByDescending { it.y }
        inventory.addItem(
            BaseItem(
                Material.FILLED_MAP, Strings.STATISTICS_COUNTRY.toString(),
                Strings.STATISTICS_COUNTRY_LORE.toString() +
                        locations.take(3).mapIndexed { i, country ->
                            ";" + ChatColor.GRAY.toString() + "${i + 1}. " + ChatColor.AQUA + country.name +
                                    ChatColor.GRAY + " ${(country.y.toDouble() / locations.sumOf { it.y } * 100).roundToInt()}%"
                        }.joinToString("")
            )
        )
        inventory.setItem(7, BaseItem(Material.CLOCK, Strings.REFRESH.toString()))
        inventory.setItem(8, BACK_ITEM)
    }
}