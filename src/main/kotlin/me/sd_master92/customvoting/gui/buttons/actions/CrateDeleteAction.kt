package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.stripColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class CrateDeleteAction(
    private val plugin: CV,
    private val currentPage: GUI,
    private val number: Int,
    private val name: String
) : BaseItem(Material.RED_WOOL, PMessage.GENERAL_ITEM_NAME_DELETE.toString())
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.FAILURE.play(plugin, player)
        plugin.data.delete(Data.VOTE_CRATES.path + ".$number")
        player.sendMessage(
            PMessage.GENERAL_MESSAGE_DELETE_SUCCESS_X.with(
                PMessage.CRATE_NAME_X.with(name).stripColor()
            )
        )
        currentPage.cancelCloseEvent = true
        currentPage.backPage?.newInstance()?.open(player)
    }
}