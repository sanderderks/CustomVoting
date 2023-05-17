package me.sd_master92.customvoting.gui.pages.abstracts

import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.withAir
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

abstract class AbstractItemEditor(
    private val plugin: CV,
    backPage: GUI?,
    val path: String,
    name: String,
    size: Int,
    private val withNull: Boolean = false,
    save: Boolean = true,
    stack: Boolean = true,
    val page: Int? = null
) :
    GUI(plugin, backPage, if (page != null) "$name #${page + 1}" else name, size, false, save)
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

    fun save(player: Player, notify: Boolean = true, notifyNoChanges: Boolean = true)
    {
        val items = contents.filterIndexed { i, _ -> i !in clickableItems.keys }.toTypedArray()
        if (contentEquals(
                if (page == null) plugin.data.getItems(path) else plugin.data.getItemsWithPagination(
                    path,
                    page,
                    size - clickableItems.size
                ), items
            )
        )
        {
            if (notifyNoChanges)
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
        return original.contentEquals(if (withNull) new.withAir() else new.filterNotNull().toTypedArray())
    }

    private fun setItems(new: Array<ItemStack?>): Boolean
    {
        return if (page == null)
        {
            plugin.data.setItems(path, if (withNull) new.withAir() else new)
        } else
        {
            plugin.data.setItemsWithPagination(
                path,
                if (withNull) new.withAir() else new,
                page,
                size - clickableItems.size
            )
        }
    }

    init
    {
        val items = if (page != null) plugin.data.getItemsWithPagination(
            path,
            page,
            nonClickableSizeWithNull - 2
        ) else plugin.data.getItems(path)

        for ((i, item) in items.withIndex())
        {
            if (withNull)
            {
                setItem(i, item)
            } else
            {
                addItem(item, stack)
            }
        }
    }
}