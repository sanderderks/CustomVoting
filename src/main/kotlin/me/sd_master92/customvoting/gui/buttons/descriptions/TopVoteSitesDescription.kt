package me.sd_master92.customvoting.gui.buttons.descriptions

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.models.BStatsData
import me.sd_master92.customvoting.gui.items.SimpleItem
import me.sd_master92.customvoting.subjects.VoteSite
import org.bukkit.Material
import kotlin.math.roundToInt

class TopVoteSitesDescription(plugin: CV) : SimpleItem(
    Material.CARVED_PUMPKIN, PMessage.STATISTICS_ITEM_NAME_VOTE_TOP_SITES.toString()
)
{
    init
    {
        val voteSites = BStatsData.VOTE_SITES.filter { site -> site.name != "Other" }.sortedByDescending { it.y }
        var lore = PMessage.STATISTICS_ITEM_LORE_VOTE_TOP_SITES.toString()
        for ((i, site) in voteSites.take(8).withIndex())
        {
            val number = i + 1
            lore += ";" + PMessage.AQUA.getColor() + "$number. "
            lore += if (VoteSite.exists(plugin, site.name))
            {
                PMessage.GREEN.getColor()
            } else
            {
                PMessage.RED.getColor()
            }
            lore += site.name
            val percentage = (site.y.toDouble() / voteSites.sumOf { it.y } * 100).roundToInt()
            lore += PMessage.GRAY.getColor() + " $percentage%"
        }
        lore += PMessage.STATISTICS_ITEM_LORE_VOTE_TOP_SITES_END.toString()
        setLore(lore)
    }
}