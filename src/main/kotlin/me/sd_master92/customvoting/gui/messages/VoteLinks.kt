package me.sd_master92.customvoting.gui.messages

import me.sd_master92.core.input.PlayerStringInput
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.Strings
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class VoteLinks @JvmOverloads constructor(private val plugin: CV, private val isPublic: Boolean = false) :
    GUI(plugin, Strings.GUI_TITLE_VOTE_LINKS.toString(), 27, false)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        if (!isPublic)
        {
            if (event.click == ClickType.RIGHT)
            {
                save(player, inventory.contents, false)
                cancelCloseEvent = true
                player.closeInventory()
                enterTitle(player, event.slot)
            }
        } else
        {
            event.isCancelled = true
            val voteLink: String = plugin.data.getMessage(Data.VOTE_LINKS + "." + event.slot)
            if (voteLink.isNotEmpty())
            {
                SoundType.SUCCESS.play(plugin, player)
                player.closeInventory()
                player.sendMessage(voteLink)
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        if (!isPublic)
        {
            save(player, inventory.contents, true)
        } else
        {
            SoundType.CLOSE.play(plugin, player)
        }
    }

    private fun enterTitle(player: Player, slot: Int)
    {
        player.sendMessage(arrayOf(Strings.INPUT_VOTE_LINK_TITLE.toString(), Strings.INPUT_CANCEL_CONTINUE.toString()))
        object : PlayerStringInput(plugin, player)
        {
            override fun onInputReceived(input: String)
            {
                val message = ChatColor.translateAlternateColorCodes('&', input)
                val items = plugin.data.getItems(Data.VOTE_LINK_ITEMS)
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
                    Strings.INPUT_VOTE_LINK_LORE.toString(),
                    Strings.INPUT_CANCEL_CONTINUE.toString()
                )
            )
        }
        object : PlayerStringInput(plugin, player)
        {
            override fun onInputReceived(input: String)
            {
                val message = ChatColor.translateAlternateColorCodes('&', input)
                val items = plugin.data.getItems(Data.VOTE_LINK_ITEMS)
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
                        Strings.INPUT_VOTE_LINK_MORE_LORE.toString(), Strings.INPUT_CANCEL_CONTINUE.toString()
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
                Strings.INPUT_VOTE_LINK_URL.toString(), Strings.INPUT_CANCEL_CONTINUE.toString()
            )
        )
        object : PlayerStringInput(plugin, player)
        {
            override fun onInputReceived(input: String)
            {
                plugin.data[Data.VOTE_LINKS + ".$slot"] = input
                plugin.data.saveConfig()

                SoundType.SUCCESS.play(plugin, player)
                player.openInventory(VoteLinks(plugin).inventory)
                cancel()
            }

            override fun onCancel()
            {
                SoundType.SUCCESS.play(plugin, player)
                player.openInventory(VoteLinks(plugin).inventory)
            }
        }
    }

    private fun save(player: Player, items: Array<ItemStack?>, notify: Boolean)
    {
        if (plugin.data.getItems(Data.VOTE_LINK_ITEMS).contentEquals(items.map { item ->
                item ?: ItemStack(Material.AIR)
            }.toTypedArray()))
        {
            SoundType.SUCCESS.play(plugin, player)
            player.sendMessage(Strings.UPDATE_NOTHING_CHANGED.toString())
        } else if (plugin.data.setItemsWithNull(Data.VOTE_LINK_ITEMS, items))
        {
            if (notify)
            {
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(Strings.UPDATE_SUCCESS_X.with(name))
            }
        } else
        {
            SoundType.FAILURE.play(plugin, player)
            player.sendMessage(Strings.UPDATE_FAIL_X.toString())
        }
    }

    init
    {
        val items = plugin.data.getItems(Data.VOTE_LINK_ITEMS)
        for (i in items.indices)
        {
            inventory.setItem(i, items[i])
        }
    }
}