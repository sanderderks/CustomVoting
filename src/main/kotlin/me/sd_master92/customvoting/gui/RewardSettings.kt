package me.sd_master92.customvoting.gui

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.listeners.PlayerListener
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class RewardSettings(private val plugin: Main) : GUI(plugin, "Vote Rewards", 9, true, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER           ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(VoteSettings(plugin).inventory)
            }
            Material.CHEST             ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(ItemRewards(plugin).inventory)
            }
            Material.GOLD_INGOT        -> if (Main.economy != null)
            {
                SoundType.CHANGE.play(plugin, player)
                PlayerListener.moneyInput.add(player.uniqueId)
                cancelCloseEvent()
                player.closeInventory()
                player.sendMessage(
                    ChatColor.GREEN.toString() + "Please enter a number between 0 and 1.000" +
                            ".000"
                )
                player.sendMessage(ChatColor.GRAY.toString() + "Type 'cancel' to go back")
                object : BukkitRunnable()
                {
                    override fun run()
                    {
                        if (!PlayerListener.moneyInput.contains(player.uniqueId))
                        {
                            player.openInventory(RewardSettings(plugin).inventory)
                            cancelCloseEvent()
                            cancel()
                        } else if (!player.isOnline)
                        {
                            PlayerListener.moneyInput.remove(player.uniqueId)
                            cancel()
                        }
                    }
                }.runTaskTimer(plugin, 0, 10)
            } else
            {
                SoundType.FAILURE.play(plugin, player)
            }
            Material.EXPERIENCE_BOTTLE ->
            {
                SoundType.CHANGE.play(plugin, player)
                if (plugin.config.getNumber(Settings.VOTE_REWARD_EXPERIENCE) < 10)
                {
                    plugin.config.addNumber(Settings.VOTE_REWARD_EXPERIENCE, 1)
                } else
                {
                    plugin.config.setNumber(Settings.VOTE_REWARD_EXPERIENCE, 0)
                }
                event.currentItem = Settings.getExperienceRewardSetting(plugin)
            }
            Material.COMMAND_BLOCK     ->
            {
                SoundType.CHANGE.play(plugin, player)
                PlayerListener.commandInput.add(player.uniqueId)
                cancelCloseEvent()
                player.closeInventory()
                player.sendMessage(
                    ChatColor.GREEN.toString() + "Please enter a command to add or remove from " +
                            "the list"
                )
                player.sendMessage(ChatColor.GREEN.toString() + "(with %PLAYER% as placeholder)")
                player.sendMessage(ChatColor.GRAY.toString() + "Type 'cancel' to go back")
                player.sendMessage("")
                val commands: List<String> = plugin.data.getStringList(Data.VOTE_COMMANDS)
                if (commands.isEmpty())
                {
                    player.sendMessage(ChatColor.RED.toString() + "There are currently no commands.")
                } else
                {
                    player.sendMessage(ChatColor.GRAY.toString() + "Commands:")
                    for (command in commands)
                    {
                        player.sendMessage(ChatColor.GRAY.toString() + "/" + ChatColor.GREEN + command)
                    }
                }
                object : BukkitRunnable()
                {
                    override fun run()
                    {
                        if (!PlayerListener.commandInput.contains(player.uniqueId))
                        {
                            player.openInventory(RewardSettings(plugin).inventory)
                            cancelCloseEvent()
                            cancel()
                        } else if (!player.isOnline)
                        {
                            PlayerListener.commandInput.remove(player.uniqueId)
                            cancel()
                        }
                    }
                }.runTaskTimer(plugin, 0, 10)
            }
            Material.ENDER_CHEST       ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(LuckyRewards(plugin).inventory)
            }
            Material.ENDER_EYE         ->
            {
                SoundType.CHANGE.play(plugin, player)
                val chance: Int = plugin.config.getNumber(Settings.LUCKY_VOTE_CHANCE)
                if (chance < 10)
                {
                    plugin.config.addNumber(Settings.LUCKY_VOTE_CHANCE, 1)
                } else if (chance < 100)
                {
                    plugin.config.addNumber(Settings.LUCKY_VOTE_CHANCE, 5)
                } else
                {
                    plugin.config.setNumber(Settings.LUCKY_VOTE_CHANCE, 1)
                }
                event.currentItem = Settings.getLuckyVoteChanceSetting(plugin)
            }
            Material.NETHER_STAR       ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(VoteStreakSettings(plugin).inventory)
            }
            else                       ->
            {
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    init
    {
        inventory.setItem(
            0, createItem(
                Material.CHEST, ChatColor.LIGHT_PURPLE.toString() +
                        "Item Rewards",
                ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.data.getItems(Data.ITEM_REWARDS).size + ChatColor.GRAY + " item stacks"
            )
        )
        inventory.setItem(1, Settings.getMoneyRewardSetting(plugin))
        inventory.setItem(2, Settings.getExperienceRewardSetting(plugin))
        inventory.setItem(
            3, createItem(
                Material.COMMAND_BLOCK, ChatColor.LIGHT_PURPLE.toString() + "Command Rewards",
                ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.data.getStringList(Data.VOTE_COMMANDS).size + ChatColor.GRAY + " commands"
            )
        )
        inventory.setItem(
            4, createItem(
                Material.ENDER_CHEST, ChatColor.LIGHT_PURPLE.toString() +
                        "Lucky Rewards",
                ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.data.getItems(Data.LUCKY_REWARDS).size + ChatColor.GRAY + " item stacks"
            )
        )
        inventory.setItem(5, Settings.getLuckyVoteChanceSetting(plugin))
        inventory.setItem(6, createItem(Material.NETHER_STAR, ChatColor.LIGHT_PURPLE.toString() + "Vote Streak"))
        inventory.setItem(8, BACK_ITEM)
    }
}