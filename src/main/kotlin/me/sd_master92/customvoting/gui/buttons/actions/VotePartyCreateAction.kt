package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.items.VotePartyItem
import me.sd_master92.customvoting.subjects.voteparty.VotePartyChest
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VotePartyCreateAction(private val plugin: CV, private val currentPage: GUI, private val key: String? = null) :
    BaseItem(
        if (key != null) Material.ENDER_EYE else Material.CRAFTING_TABLE,
        if (key != null) PMessage.VOTE_PARTY_ITEM_NAME_LOCATION_SET.toString() else PMessage.VOTE_PARTY_ITEM_NAME_CREATE.toString(),
        PMessage.VOTE_PARTY_ITEM_LORE_LOCATION_SET.toString()
    )
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.SUCCESS.play(plugin, player)
        if (key != null)
        {
            player.inventory.addItem(VotePartyItem(key))
            player.sendMessage(PMessage.VOTE_PARTY_MESSAGE_CHEST_RECEIVED.toString())
        } else
        {
            VotePartyChest.create(plugin, player)
            currentPage.newInstance().open(player)
        }
    }
}