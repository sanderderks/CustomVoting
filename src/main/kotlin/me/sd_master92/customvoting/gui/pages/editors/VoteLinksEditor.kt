package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.core.input.PlayerStringInput
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class VoteLinksEditor constructor(private val plugin: CV) :
    GUI(plugin, PMessage.VOTE_LINKS_INVENTORY_NAME.toString(), 27, false, false)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        if (event.click == ClickType.RIGHT)
        {
            save(player, contents, false)
            cancelCloseEvent = true
            player.closeInventory()
            enterTitle(player, event.slot)
        }
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        save(player, contents, true)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
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
                val items = plugin.data.getItems(Data.VOTE_LINK_ITEMS.path)
                val item = items[slot]
                val meta = item.itemMeta
                if (meta != null)
                {
                    meta.setDisplayName(message)
                    item.itemMeta = meta
                }
                items[slot] = item

                @Suppress("UNCHECKED_CAST")
                save(player, items as Array<ItemStack?>, false)

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
                val items = plugin.data.getItems(Data.VOTE_LINK_ITEMS.path)
                val item = items[slot]
                val meta = item.itemMeta
                if (meta != null)
                {
                    val lore = if (add && meta.lore != null) meta.lore else ArrayList()
                    lore!!.add(message)
                    meta.lore = lore
                    item.itemMeta = meta
                }
                items[slot] = item

                @Suppress("UNCHECKED_CAST")
                save(player, items as Array<ItemStack?>, false)

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
                plugin.data[Data.VOTE_LINKS.path + ".$slot"] = input
                plugin.data.saveConfig()

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

    private fun save(player: Player, items: Array<ItemStack?>, notify: Boolean)
    {
        if (plugin.data.getItems(Data.VOTE_LINK_ITEMS.path).contentEquals(items.map { item ->
                item ?: ItemStack(Material.AIR)
            }.toTypedArray()))
        {
            if (notify)
            {
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_NOTHING_CHANGED.toString())
            }
        } else if (plugin.data.setItemsWithNull(Data.VOTE_LINK_ITEMS.path, items))
        {
            if (notify)
            {
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_SUCCESS_X.with(name))
            }
        } else
        {
            SoundType.FAILURE.play(plugin, player)
            player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_FAIL_X.toString())
        }
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