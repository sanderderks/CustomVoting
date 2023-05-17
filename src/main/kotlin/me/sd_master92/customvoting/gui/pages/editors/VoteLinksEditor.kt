package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.core.input.PlayerStringInput
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.abstracts.AbstractItemEditor
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class VoteLinksEditor(private val plugin: CV) :
    AbstractItemEditor(
        plugin,
        null,
        Data.VOTE_LINK_ITEMS.path,
        PMessage.VOTE_LINKS_INVENTORY_NAME.toString(),
        27,
        true,
        false,
        false
    )
{
    override fun newInstance(): GUI
    {
        return this
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        if (event.click == ClickType.RIGHT)
        {
            save(player, notifyNoChanges = false)
            cancelCloseEvent = true
            player.closeInventory()
            enterTitle(player, event.slot)
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        save(player)
    }

    private fun enterTitle(player: Player, slot: Int)
    {
        player.sendMessage(
            arrayOf(
                PMessage.VOTE_LINKS_MESSAGE_TITLE_ENTER.toString(),
                PMessage.GENERAL_MESSAGE_CANCEL_CONTINUE.toString()
            )
        )
        object : PlayerStringInput(plugin, player)
        {
            override fun onInputReceived(input: String)
            {
                val message = ChatColor.translateAlternateColorCodes('&', input)
                val item = contents[slot]
                val meta = item?.itemMeta
                if (meta != null)
                {
                    meta.setDisplayName(message)
                    item.itemMeta = meta
                }
                if (item != null)
                {
                    setItem(slot, item)
                }
                save(player, false)

                SoundType.SUCCESS.play(plugin, player)
                enterMessage(player, slot)
                cancel()
            }

            override fun onCancel()
            {
                SoundType.SUCCESS.play(plugin, player)
                enterMessage(player, slot)
            }
        }
    }

    private fun enterMessage(player: Player, slot: Int, add: Boolean = false)
    {
        if (!add)
        {
            player.sendMessage(
                arrayOf(
                    PMessage.VOTE_LINKS_MESSAGE_LORE_ENTER.toString(),
                    PMessage.GENERAL_MESSAGE_CANCEL_CONTINUE.toString()
                )
            )
        }
        object : PlayerStringInput(plugin, player)
        {
            override fun onInputReceived(input: String)
            {
                val message = ChatColor.translateAlternateColorCodes('&', input)
                val item = contents[slot]
                val meta = item?.itemMeta
                if (meta != null)
                {
                    val lore = if (add && meta.lore != null) meta.lore else ArrayList()
                    lore!!.add(message)
                    meta.lore = lore
                    item.itemMeta = meta
                }
                if (item != null)
                {
                    setItem(slot, item)
                }
                save(player, false)

                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(
                    arrayOf(
                        PMessage.VOTE_LINKS_MESSAGE_LORE_ENTER_MORE.toString(),
                        PMessage.GENERAL_MESSAGE_CANCEL_CONTINUE.toString()
                    )
                )
                enterMessage(player, slot, true)
                cancel()
            }

            override fun onCancel()
            {
                SoundType.SUCCESS.play(plugin, player)
                enterLink(player, slot)
            }
        }
    }

    private fun enterLink(player: Player, slot: Int)
    {
        player.sendMessage(
            arrayOf(
                PMessage.VOTE_LINKS_MESSAGE_URL.toString(), PMessage.GENERAL_MESSAGE_CANCEL_CONTINUE.toString()
            )
        )
        object : PlayerStringInput(plugin, player)
        {
            override fun onInputReceived(input: String)
            {
                val message = ChatColor.translateAlternateColorCodes('&', input)
                val item = contents[slot]
                val meta = item?.itemMeta
                if (meta != null)
                {
                    val lore = if (meta.hasLore()) meta.lore!!.toMutableList() else ArrayList()
                    lore.add(PMessage.GRAY.getColor() + LINK_SIGN)
                    lore.add(message)
                    lore.add(PMessage.GRAY.getColor() + LINK_SIGN)
                    meta.lore = lore
                    item.itemMeta = meta
                }
                if (item != null)
                {
                    setItem(slot, item)
                }

                SoundType.SUCCESS.play(plugin, player)
                VoteLinksEditor(plugin).open(player)
                cancel()
            }

            override fun onCancel()
            {
                SoundType.SUCCESS.play(plugin, player)
                VoteLinksEditor(plugin).open(player)
            }
        }
    }

    companion object
    {
        val LINK_SIGN = PMessage.UNDERLINE.getColor() + "!URL!"
    }
}