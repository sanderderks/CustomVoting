package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.subjects.voteparty.VoteParty
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VotePartyCreateAction :
    BaseItem(Material.CRAFTING_TABLE, PMessage.VOTE_PARTY_ITEM_NAME_CREATE.toString())
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        player.closeInventory()
        player.inventory.addItem(VoteParty.VOTE_PARTY_ITEM)
        player.sendMessage(PMessage.VOTE_PARTY_MESSAGE_CHEST_RECEIVED.toString())
    }
}