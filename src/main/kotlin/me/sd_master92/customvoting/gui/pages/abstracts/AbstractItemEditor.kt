package me.sd_master92.customvoting.gui.pages.abstracts

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

abstract class AbstractItemEditor(
    private val plugin: CV,
    backPage: GUI?,
    private val path: String,
    name: String,
    size: Int,
    private val withNull: Boolean = false,
    save: Boolean = true,
    stack: Boolean = true
) :
    GUI(plugin, backPage, name, size, false, save)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        save(player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
        save(player)
    }

    fun save(player: Player, notify: Boolean = true)
    {
        val items = contents
        for (i in clickableItems.keys)
        {
            items[i] = null
        }
        if (contentEquals(plugin.data.getItems(path), items))
        {
            if (notify)
            {
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_NOTHING_CHANGED.toString())
            }
        } else if (setItems(items))
        {
            if (notify)
            {
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_SUCCESS_X.with(name))
            }
        } else
        {
            SoundType.FAILURE.play(plugin, player)
            player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_FAIL_X.with(name))
        }
    }

    private fun contentEquals(original: Array<ItemStack>, new: Array<ItemStack?>): Boolean
    {
        return if (withNull)
        {
            original.contentEquals(new.map { it ?: ItemStack(Material.AIR) }.toTypedArray())
        } else
        {
            original.contentEquals(new.filterNotNull().toTypedArray())
        }
    }

    private fun setItems(new: Array<ItemStack?>): Boolean
    {
        return if (withNull)
        {
            plugin.data.setItemsWithNull(Data.VOTE_LINK_ITEMS.path, new)
        } else
        {
            plugin.data.setItems(path, new)
        }
    }

    init
    {
        val items = plugin.data.getItems(path)
        if (withNull)
        {
            for (i in items.indices)
            {
                setItem(i, items[i])
            }
        } else
        {
            for (item in items)
            {
                addItem(item, stack)
            }
        }
    }
}