package me.sd_master92.customvoting.gui.buttons.descriptions

import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.models.BStatsData
import me.sd_master92.customvoting.gui.items.SimpleItem
import org.bukkit.Material

class MCVersionDescription : SimpleItem(
    Material.GRASS_BLOCK, PMessage.STATISTICS_ITEM_NAME_MC_VERSION.toString()
)
{
    init
    {
        val version = BStatsData.MINECRAFT_VERSIONS.maxBy { it.y }.name
        setLore(PMessage.STATISTICS_ITEM_LORE_MC_VERSION_X.with(version))
    }
}