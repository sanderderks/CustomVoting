package me.sd_master92.customvoting.gui.pages.menus

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class VoteLinksMenu constructor(private val plugin: CV) :
    GUI(plugin, null, Message.VOTE_LINKS_TITLE.getMessage(plugin), 27, true, false)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        event.isCancelled = true
        val voteLink: String = plugin.data.getMessage(Data.VOTE_LINKS.path + "." + event.slot)
        if (voteLink.isNotEmpty())
        {
            SoundType.SUCCESS.play(plugin, player)
            player.closeInventory()
            player.sendMessage(voteLink)
        }
    }

    override fun newInstance(): GUI
    {
        return this
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    init
    {
        val items = plugin.data.getItems(Data.VOTE_LINK_ITEMS.path)
        for (i in items.indices)
        {
            setItem(i, items[i])
        }
    }
}