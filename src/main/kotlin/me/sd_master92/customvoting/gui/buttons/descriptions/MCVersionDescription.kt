package me.sd_master92.customvoting.gui.buttons.descriptions

import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.models.BStatsData
import me.sd_master92.customvoting.gui.items.SimpleItem
import org.bukkit.Material
import kotlin.math.roundToInt

class MCVersionDescription : SimpleItem(
    Material.GRASS_BLOCK, PMessage.STATISTICS_ITEM_NAME_MC_VERSION.toString()
)
{
    init
    {
        val versions = BStatsData.MINECRAFT_VERSIONS.sortedByDescending { it.y }
        var lore = PMessage.STATISTICS_ITEM_LORE_MC_VERSION.toString()
        for ((i, version) in versions.take(3).withIndex())
        {
            val number = i + 1
            lore += ";" + PMessage.GRAY.getColor() + "$number. " + PMessage.AQUA.getColor() + version.name
            val percentage = (version.y.toDouble() / versions.sumOf { it.y } * 100).roundToInt()
            lore += PMessage.GRAY.getColor() + " $percentage%"
        }
        setLore(lore)
    }
}