package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.WorldExclusionType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class WorldExclusionTypeCarousel(private val plugin: CV, private val currentPage: GUI) : BaseItem(
    Material.GRASS_BLOCK,
    PMessage.DISABLED_WORLD_ITEM_NAME_TYPE.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        WorldExclusionType.toggleValue(plugin)
        currentPage.cancelCloseEvent = true
        val newPage = currentPage.newInstance()
        newPage.backPage = currentPage.backPage?.newInstance()
        newPage.open(player)
    }

    init
    {
        val worldExclusionType = WorldExclusionType.getCurrentValue(plugin)
        setLore(PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(worldExclusionType.label()))
    }
}