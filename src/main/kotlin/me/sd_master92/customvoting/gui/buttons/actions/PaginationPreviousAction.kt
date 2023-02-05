package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

abstract class PaginationPreviousAction(private val plugin: CV, private val gui: GUI, private val page: Int) :
    BaseItem(Material.FEATHER, PMessage.GENERAL_ITEM_NAME_PREVIOUS.toString())
{
    abstract fun onPrevious(player: Player, newPage: Int)

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        if (page > 0)
        {
            SoundType.CLICK.play(plugin, player)
            gui.cancelCloseEvent = true
            onPrevious(player, page - 1)
        }
    }
}