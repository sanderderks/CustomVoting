package me.sd_master92.customvoting.listeners

import me.sd_master92.core.errorLog
import me.sd_master92.core.infoLog
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.rewards.crate.VoteCrate
import me.sd_master92.customvoting.gui.voteparty.VotePartyRewards
import me.sd_master92.customvoting.sendText
import me.sd_master92.customvoting.subjects.CustomVote
import me.sd_master92.customvoting.subjects.VoteParty
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.tasks.UpdateChecker
import me.sd_master92.customvoting.tasks.VoteReminder
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.Sign
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scheduler.BukkitRunnable

class PlayerListener(private val plugin: CV) : Listener
{
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent)
    {
        val player = event.player
        executeQueue(player)
        VoteReminder.remindPlayer(plugin, player)
        UpdateChecker.checkUpdates(plugin, player)
    }

    @EventHandler
    fun onWorldChange(event: PlayerChangedWorldEvent)
    {
        executeQueue(event.player)
    }

    private fun executeQueue(player: Player)
    {
        val voter = Voter.get(plugin, player)
        val queue = voter.queue
        if (!voter.clearQueue())
        {
            plugin.errorLog("failed to delete queue of ${player.uniqueId}|${player.name}")
        }
        if (queue.isNotEmpty())
        {
            plugin.infoLog(queue.size.toString() + " queued votes found for " + player.name + ". Forwarding in 10 seconds...")
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
    fun onPlayerInteractEntity(event: PlayerInteractAtEntityEvent)
    {
        if (event.rightClicked.type == EntityType.ARMOR_STAND)
        {
            val section = plugin.data.getConfigurationSection(Data.VOTE_TOP_STANDS)
            if (section != null)
            {
                for (top in section.getKeys(false))
                {
                    for (sub in listOf("top", "name", "votes"))
                    {
                        if (section.getString("$top.$sub") == event.rightClicked.uniqueId.toString())
                        {
                            event.isCancelled = true
                            if (!plugin.config.getBoolean(Settings.DISABLED_MESSAGE_ARMOR_STAND.path))
                            {
                                event.player.sendText(plugin, Messages.VOTE_TOP_STANDS_DONT)
                            }
                        }
                    }
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
        val item = event.itemInHand
        if (item.type == VoteParty.VOTE_PARTY_ITEM.type && item.itemMeta?.displayName == VoteParty.VOTE_PARTY_ITEM.itemMeta?.displayName)
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
        if (event.action == Action.RIGHT_CLICK_BLOCK && event.clickedBlock != null)
        {
            if (event.clickedBlock!!.type == Material.ENDER_CHEST && !player.isSneaking)
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
            } else if (event.hasItem() && event.item!!.type == Material.TRIPWIRE_HOOK)
            {
                val item = event.item!!
                val name = item.itemMeta?.displayName
                if (name != null && name.contains("| crate key #"))
                {
                    val path = Data.VOTE_CRATES + "." + name.split("| crate key #")[1]
                    if (plugin.data.getString("$path.name") != null)
                    {
                        item.amount--
                        player.inventory.setItemInMainHand(item)

                        val crate = VoteCrate(plugin, player, path)
                        SoundType.OPEN.play(plugin, player)
                        player.openInventory(crate.inventory)
                        crate.run()
                    }
                }
            }
        }
    }
}