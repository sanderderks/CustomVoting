package me.sd_master92.customvoting.listeners

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.database.PlayerRow
import me.sd_master92.customvoting.gui.VoteLinks
import me.sd_master92.customvoting.gui.VotePartyRewards
import me.sd_master92.customvoting.subjects.CustomVote
import me.sd_master92.customvoting.subjects.VoteParty
import me.sd_master92.customvoting.subjects.VoteTopSign
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class PlayerListener(private val plugin: Main) : Listener
{
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent)
    {
        val player = event.player
        if (plugin.hasDatabaseConnection())
        {
            val queue: MutableList<String> = ArrayList()
            val playerRow = PlayerRow(plugin, player)
            for (i in 0 until playerRow.queue)
            {
                queue.add("unknown.com")
            }
            executeQueue(queue, player)
            playerRow.clearQueue()
        } else
        {
            val voteFile = VoteFile(player, plugin)
            executeQueue(voteFile.queue, player)
            voteFile.clearQueue()
        }
        if (player.isOp && plugin.config.getBoolean(Settings.INGAME_UPDATES) && !plugin.isUpToDate)
        {
            object : BukkitRunnable()
            {
                override fun run()
                {
                    plugin.sendDownloadUrl(player)
                    player.sendMessage("")
                    player.sendMessage(ChatColor.GRAY.toString() + "Updates can be turned off in the /votesettings")
                }
            }.runTaskLater(plugin, (20 * 5).toLong())
        }
    }

    private fun executeQueue(queue: List<String>, player: Player)
    {
        if (queue.isNotEmpty())
        {
            plugin.infoLog(
                queue.size.toString() + " queued votes found for " + player.name + ". Forwarding in 10 seconds.." +
                        "."
            )
            val iterator = queue.iterator()
            object : BukkitRunnable()
            {
                override fun run()
                {
                    if (iterator.hasNext())
                    {
                        CustomVote.create(plugin, player.name, iterator.next())
                    } else
                    {
                        cancel()
                    }
                }
            }.runTaskTimer(plugin, 200L, 20L)
        }
    }

    @EventHandler
    fun onCommandProcess(event: PlayerCommandPreprocessEvent)
    {
        val player = event.player
        if (moneyInput.contains(player.uniqueId))
        {
            event.isCancelled = true
            player.sendMessage(ChatColor.RED.toString() + "Enter a number")
        }
        if (commandInput.contains(player.uniqueId))
        {
            event.isCancelled = true
            val command = event.message
            checkStreakCommand(command, player)
        }
    }

    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent)
    {
        val player = event.player
        when (player.uniqueId)
        {
            in moneyInput            ->
            {
                event.isCancelled = true
                if (event.message.equals("cancel", true))
                {
                    moneyInput.remove(player.uniqueId)
                    SoundType.FAILURE.play(plugin, player)
                } else
                {
                    try
                    {
                        val input = event.message.toDouble()
                        if (input < 0 || input > 1000000)
                        {
                            player.sendMessage(ChatColor.RED.toString() + "Enter a number between 0 and 1.000.000")
                        } else
                        {
                            var path = Settings.VOTE_REWARD_MONEY
                            if (moneyInput[player.uniqueId] == true)
                            {
                                path += Data.OP_REWARDS
                            }
                            plugin.config[path] = input
                            plugin.config.saveConfig()
                            moneyInput.remove(player.uniqueId)
                            SoundType.SUCCESS.play(plugin, player)
                            player.sendMessage(ChatColor.GREEN.toString() + "Successfully updated the money reward!")
                        }
                    } catch (e: Exception)
                    {
                        player.sendMessage(ChatColor.RED.toString() + "Enter a number")
                    }
                }
            }
            in commandInput          ->
            {
                event.isCancelled = true
                if (event.message.equals("cancel", true))
                {
                    commandInput.remove(player.uniqueId)
                    SoundType.FAILURE.play(plugin, player)
                } else
                {
                    checkCommand(event.message, player)
                }
            }
            in loreVoteLinkInput     ->
            {
                event.isCancelled = true
                if (event.message.equals("cancel", true))
                {
                    loreVoteLinkInput.remove(player.uniqueId)
                    linkVoteLinkInput.add(player.uniqueId)
                    SoundType.SUCCESS.play(plugin, player)
                    player.sendMessage(
                        arrayOf(
                            ChatColor.GREEN.toString() + "Add a link to this item", ChatColor.GRAY.toString() +
                                    "Type 'cancel' to continue"
                        )
                    )
                } else
                {
                    val message = ChatColor.translateAlternateColorCodes('&', event.message)
                    val items = plugin.data.getItems(Data.VOTE_LINK_ITEMS)
                    val i = voteLinkInput[player.uniqueId]!!
                    val item = items[i]
                    val meta = item.itemMeta
                    if (meta != null)
                    {
                        val lore = if (meta.lore != null) meta.lore else ArrayList()
                        lore!!.add(message)
                        meta.lore = lore
                        item.itemMeta = meta
                    }
                    items[i] = item
                    VoteLinks.save(plugin, player, items as Array<ItemStack?>, false)
                    SoundType.SUCCESS.play(plugin, player)
                    player.sendMessage(
                        arrayOf(
                            ChatColor.GREEN.toString() + "Add more lore (subtext) to this item",
                            ChatColor.GRAY.toString() +
                                    "Type 'cancel' to continue"
                        )
                    )
                }
            }
            in linkVoteLinkInput     ->
            {
                event.isCancelled = true
                if (!event.message.equals("cancel", true))
                {
                    val i = voteLinkInput[player.uniqueId]!!
                    plugin.data[Data.VOTE_LINKS + "." + i] = event.message
                    plugin.data.saveConfig()
                }
                linkVoteLinkInput.remove(player.uniqueId)
                voteLinkInput.remove(player.uniqueId)
                SoundType.SUCCESS.play(plugin, player)
            }
            in voteLinkInput         ->
            {
                event.isCancelled = true
                if (!event.message.equals("cancel", true))
                {
                    val message = ChatColor.translateAlternateColorCodes('&', event.message)
                    val items = plugin.data.getItems(Data.VOTE_LINK_ITEMS)
                    val i = voteLinkInput[player.uniqueId]!!
                    val item = items[i]
                    val meta = item.itemMeta
                    if (meta != null)
                    {
                        meta.setDisplayName(message)
                        item.itemMeta = meta
                    }
                    items[i] = item
                    VoteLinks.save(plugin, player, items as Array<ItemStack?>, false)
                }
                loreVoteLinkInput.add(player.uniqueId)
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(
                    arrayOf(
                        ChatColor.GREEN.toString() + "Add lore (subtext) to this item", ChatColor.GRAY.toString() +
                                "Type 'cancel' to continue"
                    )
                )
            }
            in streakInput           ->
            {
                event.isCancelled = true
                if (event.message.equals("cancel", true))
                {
                    streakInput.remove(player.uniqueId)
                    SoundType.FAILURE.play(plugin, player)
                } else
                {
                    try
                    {
                        val number = event.message.toInt()
                        if (plugin.data.contains(Data.VOTE_STREAKS + "." + number))
                        {
                            player.sendMessage(ChatColor.RED.toString() + "That streak already exists.")
                        } else
                        {
                            plugin.data.set(Data.VOTE_STREAKS + "." + number + ".permissions", ArrayList<String>())
                            plugin.data.saveConfig()
                            player.sendMessage(ChatColor.GREEN.toString() + "Streak #" + number + " created!")
                            streakInput.remove(player.uniqueId)
                        }
                    } catch (e: Exception)
                    {
                        player.sendMessage(ChatColor.RED.toString() + "Streak must be a number.")
                    }
                }
            }
            in streakPermissionInput ->
            {
                event.isCancelled = true
                if (event.message.equals("cancel", true))
                {
                    streakPermissionInput.remove(player.uniqueId)
                    SoundType.FAILURE.play(plugin, player)
                } else
                {
                    checkStreakPermission(event.message, player)
                }
            }
            in streakCommandInput    ->
            {
                event.isCancelled = true
                if (event.message.equals("cancel", true))
                {
                    streakCommandInput.remove(player.uniqueId)
                    SoundType.FAILURE.play(plugin, player)
                } else
                {
                    checkStreakCommand(event.message, player)
                }
            }
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent)
    {
        val player = event.player
        val block = event.block
        if (block.type == Material.ENDER_CHEST)
        {
            val chests = plugin.data.getLocations(Data.VOTE_PARTY)
            for (key in chests.keys)
            {
                if (chests[key] == event.block.location)
                {
                    if (player.hasPermission("çustomvoting.voteparty"))
                    {
                        if (plugin.data.deleteLocation(Data.VOTE_PARTY + "." + key))
                        {
                            plugin.data.deleteItems(Data.VOTE_PARTY + "." + key)
                            event.isDropItems = false
                            event.player.sendMessage(ChatColor.RED.toString() + "Vote Party Chest #" + key + " deleted.")
                        }
                    } else
                    {
                        event.isCancelled = true
                        player.sendMessage(ChatColor.RED.toString() + "You do not have permission to break this block.")
                    }
                }
            }
        } else if (block.state is Sign)
        {
            val voteTop = VoteTopSign[block.location]
            voteTop?.delete(player)
        } else
        {
            val locations: MutableList<Location> = ArrayList()
            for (face in arrayOf(
                BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH,
                BlockFace.WEST
            ))
            {
                if (block.getRelative(face).state is Sign)
                {
                    locations.add(block.getRelative(face).location)
                }
            }
            if (locations.isNotEmpty())
            {
                for (loc in locations)
                {
                    val voteTop = VoteTopSign[loc]
                    voteTop?.delete(player)
                }
            }
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent)
    {
        if (event.itemInHand == VoteParty.VOTE_PARTY_ITEM)
        {
            val player = event.player
            if (player.hasPermission("customvoting.voteparty"))
            {
                val chests: Set<String> = plugin.data.getLocations(Data.VOTE_PARTY).keys
                var i = 1
                while (chests.contains("" + i))
                {
                    i++
                }
                plugin.data.setLocation(Data.VOTE_PARTY + "." + i, event.block.location)
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(ChatColor.GREEN.toString() + "Vote Party Chest #" + i + " registered.")
                player.inventory.setItemInMainHand(VoteParty.VOTE_PARTY_ITEM)
            } else
            {
                event.isCancelled = true
                player.sendMessage(ChatColor.RED.toString() + "You do not have permission to place this block.")
            }
        }
    }

    @EventHandler
    fun onBlockInteract(event: PlayerInteractEvent)
    {
        val player = event.player
        if (event.action == Action.RIGHT_CLICK_BLOCK && event.clickedBlock != null && event.clickedBlock!!.type == Material.ENDER_CHEST && !player.isSneaking)
        {
            val loc = event.clickedBlock!!.location
            val chests = plugin.data.getLocations(Data.VOTE_PARTY)
            for (key in chests.keys)
            {
                if (chests[key] == loc)
                {
                    event.isCancelled = true
                    if (player.hasPermission("customvoting.voteparty"))
                    {
                        SoundType.OPEN.play(plugin, player)
                        player.openInventory(VotePartyRewards(plugin, key).inventory)
                    } else
                    {
                        player.sendMessage(ChatColor.RED.toString() + "You do not have permission to open this chest.")
                    }
                }
            }
        }
    }

    private fun checkCommand(command_: String, player: Player)
    {
        var command = command_
        for (forbidden in plugin.config.getStringList(Settings.FORBIDDEN_COMMANDS))
        {
            if (command.lowercase().contains(forbidden))
            {
                player.sendMessage(ChatColor.RED.toString() + "This command is forbidden.")
                return
            }
        }
        if (command.startsWith("/"))
        {
            command = command.substring(1)
        }

        var path = Data.VOTE_COMMANDS
        if (commandInput[player.uniqueId] == true)
        {
            path += Data.OP_REWARDS
        }

        val commands = plugin.data.getStringList(path)
        if (commands.contains(command))
        {
            commands.remove(command)
            player.sendMessage(ChatColor.RED.toString() + "Removed /" + command + " from commands")
        } else
        {
            commands.add(command)
            player.sendMessage(ChatColor.GREEN.toString() + "Added /" + command + " to commands")
        }
        plugin.data[path] = commands
        plugin.data.saveConfig()
        commandInput.remove(player.uniqueId)
    }

    private fun checkStreakPermission(permission: String, player: Player)
    {
        val permissions =
            plugin.data.getStringList(Data.VOTE_STREAKS + "." + streakPermissionInput[player.uniqueId] + ".permissions")
        if (permissions.contains(permission))
        {
            permissions.remove(permission)
            player.sendMessage(ChatColor.RED.toString() + "Removed " + permission + " from permissions")
        } else
        {
            permissions.add(permission)
            player.sendMessage(ChatColor.GREEN.toString() + "Added " + permission + " to permissions")
        }
        plugin.data[Data.VOTE_STREAKS + "." + streakPermissionInput.remove(player.uniqueId) + ".permissions"] =
            permissions
        plugin.data.saveConfig()

    }

    private fun checkStreakCommand(command_: String, player: Player)
    {
        var command = command_
        for (forbidden in plugin.config.getStringList(Settings.FORBIDDEN_COMMANDS))
        {
            if (command.lowercase().contains(forbidden))
            {
                player.sendMessage(ChatColor.RED.toString() + "This command is forbidden.")
                return
            }
        }
        if (command.startsWith("/"))
        {
            command = command.substring(1)
        }
        val commands =
            plugin.data.getStringList(Data.VOTE_STREAKS + "." + streakCommandInput[player.uniqueId] + ".commands")
        if (commands.contains(command))
        {
            commands.remove(command)
            player.sendMessage(ChatColor.RED.toString() + "Removed /" + command + " from commands")
        } else
        {
            commands.add(command)
            player.sendMessage(ChatColor.GREEN.toString() + "Added /" + command + " to commands")
        }
        plugin.data[Data.VOTE_STREAKS + "." + streakCommandInput.remove(player.uniqueId) + ".commands"] = commands
        plugin.data.saveConfig()
    }

    companion object
    {
        var moneyInput: MutableMap<UUID, Boolean> = HashMap()
        var commandInput: MutableMap<UUID, Boolean> = HashMap()
        var streakInput: MutableList<UUID> = ArrayList()
        var streakPermissionInput: MutableMap<UUID, Int> = HashMap()
        var streakCommandInput: MutableMap<UUID, Int> = HashMap()
        var voteLinkInput: MutableMap<UUID, Int> = HashMap()
        var linkVoteLinkInput: MutableList<UUID> = ArrayList()
        var loreVoteLinkInput: MutableList<UUID> = ArrayList()
    }
}