package me.sd_master92.customvoting.gui.voteparty

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class VotePartyRewards(private val plugin: CV, private val key: String) : GUI(
    plugin, PMessage.VOTE_PARTY_INVENTORY_NAME_CHEST_X.with(key), 54, false
)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        val path = Data.VOTE_PARTY.path + ".$key"
        if (plugin.data.getItems(path).contentEquals(event.inventory.contents.filterNotNull().toTypedArray()))
        {
            SoundType.SUCCESS.play(plugin, player)
            player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_NOTHING_CHANGED.toString())
        } else if (plugin.data.setItems(path, event.inventory.contents))
        {
            SoundType.SUCCESS.play(plugin, player)
            player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_SUCCESS_X.with(name))
        } else
        {
            SoundType.FAILURE.play(plugin, player)
            player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_FAIL_X.with(name))
        }
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    init
    {
        setAll(plugin.data.getItems(Data.VOTE_PARTY.path + "." + key))
    }
}