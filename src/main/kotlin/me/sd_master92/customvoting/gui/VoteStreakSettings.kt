package me.sd_master92.customvoting.gui

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.listeners.PlayerListener
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class VoteStreakSettings(private val plugin: Main) :
    GUI(plugin, "Vote Streak Settings", getVoteStreakInventorySize(plugin), false, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER        ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(RewardSettings(plugin).inventory)
            }
            Material.ENDER_PEARL    ->
            {
                val key = item.itemMeta?.displayName?.split("#")?.get(1)
                try
                {
                    val number: Int = key!!.toInt()
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent()
                    player.openInventory(VoteStreakRewards(plugin, number).inventory)
                } catch (e: Exception)
                {
                    SoundType.FAILURE.play(plugin, player)
                }
            }
            Material.CRAFTING_TABLE ->
            {
                SoundType.CHANGE.play(plugin, player)
                PlayerListener.streakInput.add(player.uniqueId)
                cancelCloseEvent()
                player.closeInventory()
                player.sendMessage(ChatColor.GREEN.toString() + "Please enter a streak number")
                player.sendMessage(ChatColor.GRAY.toString() + "Type 'cancel' to go back")
                player.sendMessage("")
                object : BukkitRunnable()
                {
                    override fun run()
                    {
                        if (!PlayerListener.streakInput.contains(player.uniqueId))
                        {
                            player.openInventory(VoteStreakSettings(plugin).inventory)
                            cancelCloseEvent()
                            cancel()
                        } else if (!player.isOnline)
                        {
                            PlayerListener.streakInput.remove(player.uniqueId)
                            cancel()
                        }
                    }
                }.runTaskTimer(plugin, 0, 10)
            }
            else                    ->
            {
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    companion object
    {
        fun getVoteStreakInventorySize(plugin: Main): Int
        {
            val streaks = (plugin.data.getConfigurationSection(Data.VOTE_STREAKS)?.getKeys(false)?.size ?: 0) + 2
            return if (streaks % 9 == 0)
            {
                streaks
            } else
            {
                streaks + (9 - (streaks % 9))
            }
        }
    }

    init
    {
        inventory.setItem(7, createItem(Material.CRAFTING_TABLE, ChatColor.GREEN.toString() + "Add Streak"))
        inventory.setItem(8, BACK_ITEM)

        try
        {
            for (key in plugin.data.getConfigurationSection(Data.VOTE_STREAKS)?.getKeys(false)?.sortedBy { key ->
                key.toInt()
            } ?: ArrayList<String>())
            {
                inventory.addItem(
                    createItem(
                        Material.ENDER_PEARL,
                        ChatColor.LIGHT_PURPLE.toString() + "Streak #" + key
                    )
                )
            }
        } catch (_: Exception)
        {
        }
    }
}