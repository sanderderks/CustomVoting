package me.sd_master92.customvoting.gui.buttons.descriptions

import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.models.BStatsData
import me.sd_master92.customvoting.gui.items.SimpleItem
import org.bukkit.Material
import kotlin.math.roundToInt

class CountryDescription : SimpleItem(
    Material.FILLED_MAP, PMessage.STATISTICS_ITEM_NAME_COUNTRY.toString()
)
{
    init
    {
        val countries = BStatsData.COUNTRIES.sortedByDescending { it.y }
        var lore = PMessage.STATISTICS_ITEM_LORE_COUNTRY.toString()
        for ((i, country) in countries.take(3).withIndex())
        {
            val number = i + 1
            lore += ";" + PMessage.GRAY + "$number. " + PMessage.AQUA + country.name
            val percentage = (country.y.toDouble() / countries.sumOf { it.y } * 100).roundToInt()
            lore += PMessage.GRAY.toString() + " $percentage%"
        }
        setLore(lore)
    }
}