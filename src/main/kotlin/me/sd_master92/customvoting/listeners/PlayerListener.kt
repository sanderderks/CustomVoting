package me.sd_master92.customvoting.listeners

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.database.PlayerRow
import me.sd_master92.customvoting.gui.messages.VoteLinks
import me.sd_master92.customvoting.gui.voteparty.VotePartyRewards
import me.sd_master92.customvoting.sendText
import me.sd_master92.customvoting.subjects.CustomVote
import me.sd_master92.customvoting.subjects.VoteParty
import me.sd_master92.customvoting.subjects.VoteTopSign
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
import org.bukkit.event.player.*
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class PlayerListener(private val plugin: CV) : Listener
{
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent)
    {
        val player = event.player
        executeQueue(player)
        if (player.isOp && plugin.config.getBoolean(Settings.INGAME_UPDATES) && !plugin.isUpToDate())
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

    @EventHandler
    fun onWorldChange(event: PlayerChangedWorldEvent)
    {
        executeQueue(event.player)
    }

    private fun executeQueue(player: Player)
    {
        val queue: MutableList<String> = ArrayList()
        if (plugin.hasDatabaseConnection())
        {
            val playerRow = PlayerRow(plugin, player)
            for (i in 0 until playerRow.queue)
            {
                queue.add("unknown.com")
            }
            playerRow.clearQueue()
        } else
        {
            val voteFile = VoteFile(player, plugin)
            queue.addAll(voteFile.queue)
            voteFile.clearQueue()
        }
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
                            if (!plugin.config.getBoolean(Settings.DISABLED_MESSAGE_ARMOR_STAND))
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
    fun onPlayerChat(event: AsyncPlayerChatEvent)
    {
        val player = event.player
        when (player.uniqueId)
        {
            in loreVoteLinkInput ->
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
            in linkVoteLinkInput ->
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
            in voteLinkInput     ->
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

    companion object
    {
        var voteLinkInput: MutableMap<UUID, Int> = HashMap()
        var linkVoteLinkInput: MutableList<UUID> = ArrayList()
        var loreVoteLinkInput: MutableList<UUID> = ArrayList()
    }
}