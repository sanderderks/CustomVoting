package me.sd_master92.customvoting.gui.voteparty

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.Strings
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class VotePartyRewards(private val plugin: CV, private val key: String) : GUI(
    plugin, Strings.GUI_TITLE_VOTE_PARTY_CHEST_X.with(key), 54, false
)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        val path = Data.VOTE_PARTY.path + "." + key
        if (plugin.data.getItems(path).contentEquals(event.inventory.contents.filterNotNull().toTypedArray()))
        {
            SoundType.SUCCESS.play(plugin, player)
            player.sendMessage(Strings.UPDATE_NOTHING_CHANGED.toString())
        } else if (plugin.data.setItems(path, event.inventory.contents))
        {
            SoundType.SUCCESS.play(plugin, player)
            player.sendMessage(Strings.UPDATE_SUCCESS_X.with(name))
        } else
        {
            SoundType.FAILURE.play(plugin, player)
            player.sendMessage(Strings.UPDATE_FAIL_X.with(name))
        }
    }

    init
    {
        inventory.contents = plugin.data.getItems(Data.VOTE_PARTY.path + "." + key)
    }
}