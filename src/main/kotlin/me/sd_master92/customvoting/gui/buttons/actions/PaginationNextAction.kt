package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

abstract class PaginationNextAction(
    private val plugin: CV,
    private val currentPage: GUI,
    private val page: Int,
    private val itemSize: Int
) : BaseItem(Material.FEATHER, PMessage.GENERAL_ITEM_NAME_NEXT.toString())
{
    abstract fun onNext(player: Player, newPage: Int)

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        if (itemSize > (page + 1) * 51)
        {
            SoundType.CLICK.play(plugin, player)
            currentPage.cancelCloseEvent = true
            onNext(player, page + 1)
        }
    }
}