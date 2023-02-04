package me.sd_master92.customvoting.gui.support

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.BStatsData
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.items.SimpleItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import kotlin.math.roundToInt

class Statistics(private val plugin: CV) : GUI(plugin, PMessage.STATISTICS_INVENTORY_NAME.toString(), 9)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        Support(plugin).open(player)
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
        val voteSites = BStatsData.VOTE_SITES.sortedByDescending { it.y }
        addItem(
            SimpleItem(
                Material.CARVED_PUMPKIN, PMessage.STATISTICS_ITEM_NAME_VOTE_TOP_SITES.toString(),
                PMessage.STATISTICS_ITEM_LORE_VOTE_TOP_SITES.toString() +
                        voteSites.take(8).mapIndexed { i, site ->
                            ";" + PMessage.AQUA + "${i + 1}. " + (if (plugin.data.getStringList(Data.VOTE_SITES.path)
                                    .contains(site.name)
                            ) PMessage.GREEN else PMessage.RED) + site.name + PMessage.GRAY + " ${(site.y.toDouble() / voteSites.sumOf { it.y } * 100).roundToInt()}%"
                        }.joinToString("") + PMessage.STATISTICS_ITEM_LORE_VOTE_TOP_SITES_END.toString()
            )
        )
        addItem(
            SimpleItem(
                Material.GRASS_BLOCK, PMessage.STATISTICS_ITEM_NAME_MC_VERSION.toString(),
                PMessage.STATISTICS_ITEM_LORE_MC_VERSION_X.with(
                    BStatsData.MINECRAFT_VERSIONS.maxByOrNull { it.y }!!.name
                )
            )
        )
        val locations = BStatsData.COUNTRIES.sortedByDescending { it.y }
        addItem(
            SimpleItem(
                Material.FILLED_MAP, PMessage.STATISTICS_ITEM_NAME_COUNTRY.toString(),
                PMessage.STATISTICS_ITEM_LORE_COUNTRY.toString() +
                        locations.take(3).mapIndexed { i, country ->
                            ";" + PMessage.GRAY.toString() + "${i + 1}. " + PMessage.AQUA + country.name +
                                    PMessage.GRAY + " ${(country.y.toDouble() / locations.sumOf { it.y } * 100).roundToInt()}%"
                        }.joinToString("")
            )
        )
        setItem(7, object : BaseItem(Material.CLOCK, PMessage.GENERAL_ITEM_NAME_REFRESH.toString())
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                BStatsData.refresh()
                Statistics(plugin).open(player)
            }
        })
    }
}