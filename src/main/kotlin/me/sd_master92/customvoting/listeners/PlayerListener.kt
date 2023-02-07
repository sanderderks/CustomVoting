package me.sd_master92.customvoting.listeners

import me.sd_master92.core.errorLog
import me.sd_master92.core.infoLog
import me.sd_master92.core.inventory.ConfirmGUI
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.*
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.gui.pages.editors.VotePartyItemRewardsEditor
import me.sd_master92.customvoting.gui.pages.menus.CrateMenu
import me.sd_master92.customvoting.sendText
import me.sd_master92.customvoting.stripColor
import me.sd_master92.customvoting.subjects.CustomVote
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.VoteTopStand
import me.sd_master92.customvoting.subjects.voteparty.VoteParty
import me.sd_master92.customvoting.tasks.UpdateChecker
import me.sd_master92.customvoting.tasks.VoteReminder
import org.bukkit.Bukkit
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
            plugin.errorLog(PMessage.QUEUE_ERROR_DELETE_XY.with(player.uniqueId.toString(), player.name))
        }
        if (queue.isNotEmpty())
        {
            plugin.infoLog(PMessage.QUEUE_MESSAGE_FORWARD_XY.with(queue.size.toString(), player.name))
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
            val section = plugin.data.getConfigurationSection(Data.VOTE_TOP_STANDS.path)
            if (section != null)
            {
                for (top in section.getKeys(false))
                {
                    for (sub in listOf("top", "name", "votes"))
                    {
                        if (section.getString("$top.$sub") == event.rightClicked.uniqueId.toString())
                        {
                            event.isCancelled = true
                            if (!plugin.config.getBoolean(Setting.DISABLED_MESSAGE_ARMOR_STAND.path))
                            {
                                event.player.sendText(plugin, Message.VOTE_TOP_STANDS_DONT)
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
            var chests = plugin.data.getLocations(Data.VOTE_PARTY.path)
            for (key in chests.keys)
            {
                if (chests[key] == event.block.location)
                {
                    if (player.hasPermission("çustomvoting.voteparty"))
                    {
                        if (plugin.data.deleteLocation(Data.VOTE_PARTY.path + ".$key"))
                        {
                            plugin.data.deleteItems(Data.VOTE_PARTY.path + ".$key")
                            event.isDropItems = false
                            event.player.sendMessage(PMessage.VOTE_PARTY_MESSAGE_CHEST_DELETED_X.with(key))
                        }
                    } else
                    {
                        event.isCancelled = true
                        player.sendMessage(PMessage.ACTION_ERROR_BREAK_BLOCK_NO_PERMISSION.toString())
                    }
                }
            }
            chests = plugin.data.getLocations(Data.VOTE_CRATES.path)
            for (key in chests.keys)
            {
                if (chests[key] == event.block.location)
                {
                    if (player.hasPermission("customvoting.crate"))
                    {
                        if (plugin.data.deleteLocation(Data.VOTE_CRATES.path + ".$key"))
                        {
                            val path = Data.VOTE_CRATES.path + ".$key"
                            plugin.data.getString("$path.stand")?.let {
                                val stand = Bukkit.getEntity(UUID.fromString(it))
                                stand?.remove()
                            }
                            event.player.sendMessage(
                                PMessage.GENERAL_MESSAGE_DELETE_SUCCESS_X.with(
                                    plugin.data.getString("$path.name") ?: PMessage.CRATE_NAME_DEFAULT_X.with(key)
                                )
                            )
                        }
                    } else
                    {
                        event.isCancelled = true
                        player.sendMessage(PMessage.ACTION_ERROR_BREAK_BLOCK_NO_PERMISSION.toString())
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
                val chests: Set<String> = plugin.data.getLocations(Data.VOTE_PARTY.path).keys
                var i = 1
                while (chests.contains("$i"))
                {
                    i++
                }
                plugin.data.setLocation(Data.VOTE_PARTY.path + ".$i", event.block.location)
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(PMessage.VOTE_PARTY_MESSAGE_CHEST_CREATED_X.with("$i"))
                player.inventory.setItemInMainHand(VoteParty.VOTE_PARTY_ITEM)
            } else
            {
                event.isCancelled = true
                player.sendMessage(PMessage.ACTION_ERROR_BREAK_BLOCK_NO_PERMISSION.toString())
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
                var chests = plugin.data.getLocations(Data.VOTE_PARTY.path)
                for (key in chests.keys)
                {
                    if (chests[key] == loc)
                    {
                        event.isCancelled = true
                        if (player.hasPermission("customvoting.voteparty"))
                        {
                            SoundType.OPEN.play(plugin, player)
                            VotePartyItemRewardsEditor(plugin, key).open(player)
                        } else
                        {
                            player.sendMessage(PMessage.ACTION_ERROR_OPEN_CHEST_NO_PERMISSION.toString())
                        }
                    }
                }
                chests = plugin.data.getLocations(Data.VOTE_CRATES.path)
                for (key in chests.keys)
                {
                    if (chests[key]?.equals(loc) == true &&
                        (event.item?.type != Material.TRIPWIRE_HOOK || event.item?.itemMeta?.lore?.get(0)
                            ?.contains("#") == false)
                    )
                    {
                        event.isCancelled = true
                        player.sendMessage(PMessage.ACTION_ERROR_OPEN_CHEST_NEED_KEY.toString())
                    }
                }
            }
            if (event.hasItem() && event.item!!.type == Material.TRIPWIRE_HOOK)
            {
                val item = event.item!!
                val lore = item.itemMeta?.lore?.get(0)
                if (lore != null && lore.contains("#"))
                {
                    val key = lore.split("#")[1].stripColor()
                    val path = Data.VOTE_CRATES.path + ".$key"
                    val name = plugin.data.getString("$path.name")

                    val meta = item.itemMeta!!
                    meta.setDisplayName(
                        PMessage.CRATE_ITEM_NAME_KEY_X.with(
                            name ?: PMessage.CRATE_NAME_DEFAULT_X.with(key)
                        )
                    )
                    item.itemMeta = meta
                    player.inventory.setItemInMainHand(item)

                    if (name != null)
                    {
                        if (plugin.data.getLocation(path) == null)
                        {
                            if (player.hasPermission("customvoting.crate"))
                            {
                                event.isCancelled = true
                                val loc = event.clickedBlock!!.location
                                if (!plugin.data.getLocations(Data.VOTE_CRATES.path).containsValue(loc))
                                {
                                    val confirm =
                                        object :
                                            ConfirmGUI(plugin, PMessage.CRATE_INVENTORY_NAME_PLACE_CONFIRM_X.with(name))
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
                                                stand.customName = PMessage.CRATE_NAME_STAND_X.with(name)
                                                stand.setGravity(false)
                                                plugin.data.set("$path.stand", stand.uniqueId.toString())

                                                plugin.data.saveConfig()

                                                SoundType.SUCCESS.play(plugin, player)
                                                player.closeInventory()
                                            }
                                        }
                                    SoundType.CLICK.play(plugin, player)
                                    confirm.open(player)
                                } else
                                {
                                    SoundType.FAILURE.play(plugin, player)
                                    player.sendMessage(PMessage.ACTION_ERROR_OPEN_CHEST_WRONG_KEY.toString())
                                }
                            } else
                            {
                                player.sendMessage(PMessage.ACTION_ERROR_OPEN_CHEST_NO_PERMISSION.toString())
                            }
                        } else if (event.clickedBlock!!.type == Material.ENDER_CHEST)
                        {
                            event.isCancelled = true
                            if (event.clickedBlock?.location?.equals(plugin.data.getLocation(path)) == true)
                            {
                                item.amount--
                                player.inventory.setItemInMainHand(item)

                                val crate = CrateMenu(plugin, player, path)
                                SoundType.OPEN.play(plugin, player)
                                crate.open(player)
                                crate.run()
                            } else
                            {
                                SoundType.FAILURE.play(plugin, player)
                                player.sendMessage(PMessage.ACTION_ERROR_OPEN_CHEST_WRONG_KEY.toString())
                            }
                        } else
                        {
                            event.isCancelled = true
                            player.sendMessage(PMessage.ACTION_ERROR_INTERACT_NEED_CRATE.toString())
                        }
                    }
                }
            }
        }
    }
}