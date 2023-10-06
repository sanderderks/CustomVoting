package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.subjects.voteparty.VotePartyChest
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VotePartyDeleteAction(private val plugin: CV, private val currentPage: GUI, private val key: String) :
    BaseItem(Material.RED_WOOL, PMessage.GENERAL_ITEM_NAME_DELETE.toString())
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.FAILURE.play(plugin, player)
        currentPage.cancelCloseEvent = true
        if (currentPage.backPage != null)
        {
            currentPage.backPage!!.newInstance().open(player)
        } else
        {
            player.closeInventory()
        }
        VotePartyChest.getByKey(plugin, key)?.delete(player)
    }
}