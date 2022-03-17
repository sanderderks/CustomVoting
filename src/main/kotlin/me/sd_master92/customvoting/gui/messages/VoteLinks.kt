package me.sd_master92.customvoting.gui.messages

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.listeners.PlayerListener
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class VoteLinks @JvmOverloads constructor(private val plugin: CV, private val isPublic: Boolean = false) :
    GUI(plugin, NAME, 27, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        if (!isPublic)
        {
            if (event.click == ClickType.RIGHT)
            {
                save(plugin, player, inventory.contents, false)
                PlayerListener.voteLinkInput[player.uniqueId] = event.slot
                cancelCloseEvent()
                player.closeInventory()
                player.sendMessage(
                    arrayOf(
                        ChatColor.GREEN.toString() + "Enter a title for this item (with & colors)",
                        ChatColor.GRAY.toString() +
                                "Type 'cancel' to continue"
                    )
                )
                object : BukkitRunnable()
                {
                    override fun run()
                    {
                        if (!PlayerListener.voteLinkInput.containsKey(player.uniqueId))
                        {
                            player.openInventory(VoteLinks(plugin).inventory)
                            cancelCloseEvent()
                            cancel()
                        } else if (!player.isOnline)
                        {
                            PlayerListener.voteLinkInput.remove(player.uniqueId)
                            cancel()
                        }
                    }
                }.runTaskTimer(plugin, 0, 10)
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
            save(plugin, player, inventory.contents, true)
        } else
        {
            SoundType.CLOSE.play(plugin, player)
        }
    }

    companion object
    {
        const val NAME = "Vote Links"
        fun save(plugin: CV, player: Player, items: Array<ItemStack?>, notify: Boolean)
        {
            if (plugin.data.setItemsWithNull(Data.VOTE_LINK_ITEMS, items))
            {
                if (notify)
                {
                    SoundType.SUCCESS.play(plugin, player)
                    player.sendMessage(ChatColor.GREEN.toString() + "Successfully updated the $NAME!")
                }
            } else
            {
                SoundType.FAILURE.play(plugin, player)
                player.sendMessage(ChatColor.RED.toString() + "Failed to update the $NAME!")
            }
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