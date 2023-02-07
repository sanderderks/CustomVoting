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
    private val path: String,
    name: String,
    size: Int,
    private val withNull: Boolean = false,
    back: Boolean = true,
    save: Boolean = true,
    stack: Boolean = true
) :
    GUI(plugin, name, size, false, back, save)
{
    abstract val previousPage: GUI?

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        event.isCancelled = true
        SoundType.CLICK.play(plugin, player)
        back(player)
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        save(player, event.inventory.contents)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
        event.isCancelled = true
        save(player, event.inventory.contents)
        back(player)
    }

    fun save(player: Player, items: Array<ItemStack?>, notify: Boolean = true)
    {
        if (save && back)
        {
            items[size - 2] = null
            items[size - 1] = null
        } else if (save || back)
        {
            items[size - 1] = null
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

    private fun back(player: Player)
    {
        if (previousPage == null)
        {
            player.closeInventory()
        } else
        {
            cancelCloseEvent = true
            previousPage!!.open(player)
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