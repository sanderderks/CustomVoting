package me.sd_master92.customvoting.gui

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.appendWhenTrue
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.ItemRewardType
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.items.*
import me.sd_master92.customvoting.listeners.PlayerListener
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class RewardSettings(private val plugin: CV, private val op: Boolean = false) :
    GUI(plugin, "Vote Rewards".appendWhenTrue(op, " (permission based)"), 18, true, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER           ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                if (op)
                {
                    player.openInventory(RewardSettings(plugin).inventory)
                } else
                {
                    player.openInventory(VoteSettings(plugin).inventory)
                }
            }
            Material.CHEST             ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(ItemRewards(plugin, op).inventory)
            }
            Material.REPEATER          ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.setNumber(
                    Settings.ITEM_REWARD_TYPE,
                    ItemRewardType.next(plugin).value
                )
                event.currentItem = ItemsRewardTypeItem(plugin)
            }
            Material.GOLD_INGOT        -> if (CV.ECONOMY != null)
            {
                SoundType.CHANGE.play(plugin, player)
                PlayerListener.moneyInput[player.uniqueId] = op
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
                        if (!PlayerListener.moneyInput.containsKey(player.uniqueId))
                        {
                            player.openInventory(RewardSettings(plugin, op).inventory)
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
                var path = Settings.VOTE_REWARD_EXPERIENCE
                if (op)
                {
                    path += Data.OP_REWARDS
                }
                if (plugin.config.getNumber(path) < 10)
                {
                    plugin.config.addNumber(path, 1)
                } else
                {
                    plugin.config.setNumber(path, 0)
                }
                event.currentItem = ExperienceRewardItem.getInstance(plugin, op)
            }
            Material.COMMAND_BLOCK     ->
            {
                SoundType.CHANGE.play(plugin, player)
                PlayerListener.commandInput[player.uniqueId] = op
                cancelCloseEvent()
                player.closeInventory()
                player.sendMessage(
                    ChatColor.GREEN.toString() + "Please enter a command to add or remove from " +
                            "the list"
                )
                player.sendMessage(ChatColor.GREEN.toString() + "(with %PLAYER% as placeholder)")
                player.sendMessage(ChatColor.GRAY.toString() + "Type 'cancel' to go back")
                player.sendMessage("")
                var path = Data.VOTE_COMMANDS
                if (op)
                {
                    path += Data.OP_REWARDS
                }
                val commands: List<String> = plugin.data.getStringList(path)
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
                            player.openInventory(RewardSettings(plugin, op).inventory)
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
                event.currentItem = LuckyVoteChanceItem(plugin)
            }
            Material.NETHER_STAR       ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(VoteStreakSettings(plugin).inventory)
            }
            Material.DIAMOND_BLOCK     ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(RewardSettings(plugin, true).inventory)
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
        var itemsPath = Data.ITEM_REWARDS
        var commandsPath = Data.VOTE_COMMANDS
        if (op)
        {
            itemsPath += Data.OP_REWARDS
            commandsPath += Data.OP_REWARDS
        }
        inventory.setItem(0, ItemsRewardItem(plugin, itemsPath))
        inventory.setItem(1, ItemsRewardTypeItem(plugin))
        inventory.setItem(2, MoneyRewardItem.getInstance(plugin, op))
        inventory.setItem(3, ExperienceRewardItem.getInstance(plugin, op))
        inventory.setItem(4, CommandsRewardItem(plugin, commandsPath))
        if (!op)
        {
            inventory.setItem(5, LuckyRewardItem(plugin))
            inventory.setItem(6, LuckyVoteChanceItem(plugin))
            inventory.setItem(7, StreakItem())
            inventory.setItem(8, PermissionBasedItem())
        }
        inventory.setItem(17, BACK_ITEM)
    }
}