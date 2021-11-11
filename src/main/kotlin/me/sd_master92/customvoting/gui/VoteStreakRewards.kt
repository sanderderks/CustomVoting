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

class VoteStreakRewards(private val plugin: Main, private val number: Int) : GUI(plugin, NAME + number, 9, false, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER       ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(VoteStreakSettings(plugin).inventory)
            }
            Material.RED_WOOL      ->
            {
                SoundType.FAILURE.play(plugin, player)
                plugin.data.delete(Data.VOTE_STREAKS + "." + number)
                cancelCloseEvent()
                player.openInventory(VoteStreakSettings(plugin).inventory)
            }
            Material.DIAMOND_SWORD ->
            {
                SoundType.CHANGE.play(plugin, player)
                PlayerListener.streakPermissionInput[player.uniqueId] = number
                cancelCloseEvent()
                player.closeInventory()
                player.sendMessage(
                    ChatColor.GREEN.toString() + "Please enter a permission to add or remove from " +
                            "the list"
                )
                player.sendMessage(ChatColor.GRAY.toString() + "Type 'cancel' to go back")
                player.sendMessage("")
                val permissions: List<String> =
                    plugin.data.getStringList(Data.VOTE_STREAKS + "." + PlayerListener.streakPermissionInput[player.uniqueId] + ".permissions")
                if (permissions.isEmpty())
                {
                    player.sendMessage(ChatColor.RED.toString() + "There are currently no permissions.")
                } else
                {
                    player.sendMessage(ChatColor.GRAY.toString() + "Permissions:")
                    for (permission in permissions)
                    {
                        player.sendMessage(ChatColor.GRAY.toString() + "-" + ChatColor.GREEN + permission)
                    }
                }
                object : BukkitRunnable()
                {
                    override fun run()
                    {
                        if (!PlayerListener.streakPermissionInput.containsKey(player.uniqueId))
                        {
                            player.openInventory(VoteStreakRewards(plugin, number).inventory)
                            cancelCloseEvent()
                            cancel()
                        } else if (!player.isOnline)
                        {
                            PlayerListener.streakPermissionInput.remove(player.uniqueId)
                            cancel()
                        }
                    }
                }.runTaskTimer(plugin, 0, 10)
            }
            Material.SHIELD        ->
            {
                SoundType.CHANGE.play(plugin, player)
                PlayerListener.streakCommandInput[player.uniqueId] = number
                cancelCloseEvent()
                player.closeInventory()
                player.sendMessage(
                    ChatColor.GREEN.toString() + "Please enter a command to add or remove from " +
                            "the list"
                )
                player.sendMessage(ChatColor.GREEN.toString() + "(with %PLAYER% as placeholder)")
                player.sendMessage(ChatColor.GRAY.toString() + "Type 'cancel' to go back")
                player.sendMessage("")
                val commands: List<String> =
                    plugin.data.getStringList(Data.VOTE_STREAKS + "." + PlayerListener.streakCommandInput[player.uniqueId] + ".commands")
                if (commands.isEmpty())
                {
                    player.sendMessage(ChatColor.RED.toString() + "There are currently no commands.")
                } else
                {
                    player.sendMessage(ChatColor.GRAY.toString() + "Commands:")
                    for (command in commands)
                    {
                        player.sendMessage(ChatColor.GRAY.toString() + "-" + ChatColor.GREEN + command)
                    }
                }
                object : BukkitRunnable()
                {
                    override fun run()
                    {
                        if (!PlayerListener.streakCommandInput.containsKey(player.uniqueId))
                        {
                            player.openInventory(VoteStreakRewards(plugin, number).inventory)
                            cancelCloseEvent()
                            cancel()
                        } else if (!player.isOnline)
                        {
                            PlayerListener.streakCommandInput.remove(player.uniqueId)
                            cancel()
                        }
                    }
                }.runTaskTimer(plugin, 0, 10)
            }
            else                   ->
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
        const val NAME = "Vote Streak Rewards #"
    }

    init
    {
        inventory.setItem(0, Data.getStreakPermissionRewardSetting(plugin, number))
        inventory.setItem(1, Data.getStreakCommandRewardSetting(plugin, number))
        inventory.setItem(7, createItem(Material.RED_WOOL, ChatColor.RED.toString() + "Delete"))
        inventory.setItem(8, BACK_ITEM)
    }
}