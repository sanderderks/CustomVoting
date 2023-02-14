package me.sd_master92.customvoting.gui.pages.menus

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.editors.VoteLinksEditor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class VoteLinksMenu constructor(private val plugin: CV) :
    GUI(plugin, null, Message.VOTE_LINKS_TITLE.getMessage(plugin), 27, true, false)
{
    private val clickable = mutableMapOf<Int, String>()

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        event.isCancelled = true
        var url = clickable[event.slot]
        if (url != null)
        {
            SoundType.SUCCESS.play(plugin, player)
            player.closeInventory()
            player.sendMessage(url)
        } else
        {
            url = plugin.data.getMessage(Data.VOTE_LINKS.path + "." + event.slot)
            if (url.isNotEmpty())
            {
                SoundType.SUCCESS.play(plugin, player)
                player.closeInventory()
                player.sendMessage(url)
            }
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

    private fun filterUrlFromLore(slot: Int, item: ItemStack): ItemStack
    {
        if (item.hasItemMeta() && item.itemMeta!!.hasLore())
        {
            val newLore = mutableListOf<String>()
            val lore = item.itemMeta!!.lore!!.iterator()
            while (lore.hasNext())
            {
                val line = lore.next()
                if (line.contains(VoteLinksEditor.LINK_SIGN) && lore.hasNext())
                {
                    clickable[slot] = lore.next()
                    break
                }
                newLore.add(line)
            }
            val meta = item.itemMeta
            meta!!.lore = newLore
            item.itemMeta = meta
        }
        return item
    }

    init
    {
        val items = plugin.data.getItems(Data.VOTE_LINK_ITEMS.path)
        for (i in items.indices)
        {
            setItem(i, filterUrlFromLore(i, items[i]))
        }
    }
}