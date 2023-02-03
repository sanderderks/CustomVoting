package me.sd_master92.customvoting.listeners

import me.sd_master92.core.errorLog
import me.sd_master92.core.infoLog
import me.sd_master92.core.inventory.ConfirmGUI
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.gui.rewards.crate.VoteCrate
import me.sd_master92.customvoting.gui.voteparty.VotePartyRewards
import me.sd_master92.customvoting.sendText
import me.sd_master92.customvoting.subjects.CustomVote
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.VoteTopStand
import me.sd_master92.customvoting.subjects.voteparty.VoteParty
import me.sd_master92.customvoting.tasks.UpdateChecker
import me.sd_master92.customvoting.tasks.VoteReminder
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.Sign
import org.bukkit.block.data.Directional
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*

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
            plugin.errorLog(Strings.QUEUE_DELETE_ERROR_XY.with(player.uniqueId.toString(), player.name))
        }
        if (queue.isNotEmpty())
        {
            plugin.infoLog(Strings.QUEUE_FORWARD_XY.with(queue.size.toString(), player.name))
            val iterator = queue.iterator()
            TaskTimer.repeat(plugin, 20, 200)
            {
                if (iterator.hasNext())
                {
                    CustomVote.create(plugin, player.name, iterator.next())
                } else
                {
                    it.cancel()
                }
            }.run()
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
            var chests = plugin.data.getLocations(Data.VOTE_PARTY)
            for (key in chests.keys)
            {
                if (chests[key] == event.block.location)
                {
                    if (player.hasPermission("çustomvoting.voteparty"))
                    {
                        if (plugin.data.deleteLocation(Data.VOTE_PARTY + ".$key"))
                        {
                            plugin.data.deleteItems(Data.VOTE_PARTY + ".$key")
                            event.isDropItems = false
                            event.player.sendMessage(Strings.VOTE_PARTY_CHEST_DELETED_X.with(key))
                        }
                    } else
                    {
                        event.isCancelled = true
                        player.sendMessage(Strings.BREAK_BLOCK_NO_PERMISSION.toString())
                    }
                }
            }
            chests = plugin.data.getLocations(Data.VOTE_CRATES)
            for (key in chests.keys)
            {
                if (chests[key] == event.block.location)
                {
                    if (player.hasPermission("customvoting.crate"))
                    {
                        if (plugin.data.deleteLocation(Data.VOTE_CRATES + ".$key"))
                        {
                            val path = Data.VOTE_CRATES + ".$key"
                            val stand = Bukkit.getEntity(
                                UUID.fromString(plugin.data.getString("$path.stand") ?: "")
                            )
                            stand?.remove()
                            plugin.data.delete("$path.stand")
                            event.player.sendMessage(
                                Strings.VOTE_CRATE_DELETED_X.with(
                                    plugin.data.getString("$path.name") ?: ""
                                )
                            )
                        }
                    } else
                    {
                        event.isCancelled = true
                        player.sendMessage(Strings.BREAK_BLOCK_NO_PERMISSION.toString())
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
                while (chests.contains("$i"))
                {
                    i++
                }
                plugin.data.setLocation(Data.VOTE_PARTY + ".$i", event.block.location)
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(Strings.VOTE_PARTY_CHEST_CREATED_X.with("$i"))
                player.inventory.setItemInMainHand(VoteParty.VOTE_PARTY_ITEM)
            } else
            {
                event.isCancelled = true
                player.sendMessage(Strings.BREAK_BLOCK_NO_PERMISSION.toString())
            }
        }
    }

    @EventHandler
    fun onBlockInteract(event: PlayerInteractEvent)
    {
        val player = event.player
        if (event.action == Action.RIGHT_CLICK_BLOCK && event.clickedBlock != null)
        {
            if (event.clickedBlock!!.type == Material.ENDER_CHEST)
            {
                val loc = event.clickedBlock!!.location
                var chests = plugin.data.getLocations(Data.VOTE_PARTY)
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
                            player.sendMessage(Strings.OPEN_CHEST_NO_PERMISSION.toString())
                        }
                    }
                }
                chests = plugin.data.getLocations(Data.VOTE_CRATES)
                for (key in chests.keys)
                {
                    if (chests[key]?.equals(loc) == true &&
                        (event.item?.type != Material.TRIPWIRE_HOOK || event.item?.itemMeta?.lore?.get(0)
                            ?.contains("#") == false)
                    )
                    {
                        event.isCancelled = true
                        player.sendMessage(Strings.OPEN_CHEST_NEED_KEY.toString())
                    }
                }
            }
            if (event.hasItem() && event.item!!.type == Material.TRIPWIRE_HOOK)
            {
                val item = event.item!!
                val lore = item.itemMeta?.lore?.get(0)
                if (lore != null && lore.contains("#"))
                {
                    val path = Data.VOTE_CRATES + "." + lore.split("#")[1]
                    val name = plugin.data.getString("$path.name")
                    if (name != null)
                    {
                        if (plugin.data.getLocation(path) == null)
                        {
                            if (player.hasPermission("customvoting.crate"))
                            {
                                event.isCancelled = true
                                val loc = event.clickedBlock!!.location
                                if (!plugin.data.getLocations(Data.VOTE_CRATES).containsValue(loc))
                                {
                                    val confirm =
                                        object : ConfirmGUI(plugin, Strings.VOTE_CRATE_PLACE_HERE_X.with(name))
                                        {
                                            override fun onCancel(event: InventoryClickEvent, player: Player)
                                            {
                                                SoundType.FAILURE.play(plugin, player)
                                                player.closeInventory()
                                            }

                                            override fun onClose(event: InventoryCloseEvent, player: Player)
                                            {
                                                SoundType.FAILURE.play(plugin, player)
                                            }

                                            override fun onConfirm(event: InventoryClickEvent, player: Player)
                                            {
                                                val location = Location(loc.world, loc.x, loc.y + 1, loc.z)
                                                location.block.type = Material.ENDER_CHEST
                                                val directional = location.block.blockData as Directional
                                                directional.facing = player.facing.oppositeFace
                                                location.block.blockData = directional
                                                plugin.data.setLocation(path, location)

                                                val stand =
                                                    VoteTopStand.spawnArmorStand(
                                                        Location(
                                                            loc.world,
                                                            loc.x + 0.5,
                                                            loc.y + 1,
                                                            loc.z + 0.5
                                                        )
                                                    )
                                                stand.customName = ChatColor.AQUA.toString() + name
                                                stand.setGravity(false)
                                                plugin.data.set("$path.stand", stand.uniqueId.toString())

                                                plugin.data.saveConfig()

                                                SoundType.SUCCESS.play(plugin, player)
                                                player.closeInventory()
                                            }
                                        }
                                    SoundType.CLICK.play(plugin, player)
                                    player.openInventory(confirm.inventory)
                                } else
                                {
                                    SoundType.FAILURE.play(plugin, player)
                                    player.sendMessage(Strings.OPEN_CHEST_WRONG_KEY.toString())
                                }
                            }
                        } else if (event.clickedBlock!!.type == Material.ENDER_CHEST && event.clickedBlock?.location?.equals(
                                plugin.data.getLocation(path)
                            ) == true
                        )
                        {
                            event.isCancelled = true
                            item.amount--
                            player.inventory.setItemInMainHand(item)

                            val crate = VoteCrate(plugin, player, path)
                            SoundType.OPEN.play(plugin, player)
                            player.openInventory(crate.inventory)
                            crate.run()
                        } else
                        {
                            event.isCancelled = true
                            player.sendMessage(Strings.INTERACT_OPEN_CRATE.toString())
                        }
                    }
                }
            }
        }
    }
}