package me.sd_master92.customvoting.listeners

import me.sd_master92.core.errorLog
import me.sd_master92.core.infoLog
import me.sd_master92.core.inventory.ConfirmGUI
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.*
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.gui.pages.editors.VotePartyRewardItemsEditor
import me.sd_master92.customvoting.sendText
import me.sd_master92.customvoting.stripColor
import me.sd_master92.customvoting.subjects.CustomVote
import me.sd_master92.customvoting.subjects.VoteCrate
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.voteparty.VoteParty
import me.sd_master92.customvoting.subjects.voteparty.VotePartyChest
import me.sd_master92.customvoting.tasks.UpdateChecker
import me.sd_master92.customvoting.tasks.VoteReminder
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.Sign
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent

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
    fun onPlayerInteractEntity(event: EntityDamageByEntityEvent)
    {
        if (event.entity is ArmorStand && event.damager is Player)
        {
            val section = plugin.data.getConfigurationSection(Data.VOTE_TOP_STANDS.path)
            if (section != null)
            {
                for (top in section.getKeys(false))
                {
                    for (sub in listOf("top", "name", "votes"))
                    {
                        if (section.getString("$top.$sub") == event.entity.uniqueId.toString())
                        {
                            event.isCancelled = true
                            if (!plugin.config.getBoolean(Setting.DISABLED_MESSAGE_ARMOR_STAND.path))
                            {
                                event.damager.sendText(plugin, Message.VOTE_TOP_STANDS_DONT)
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
            val votePartyChest = VotePartyChest.getByLocation(plugin, event.block.location)
            if (votePartyChest != null)
            {
                if (player.hasPermission("çustomvoting.voteparty"))
                {
                    event.isDropItems = false
                    votePartyChest.delete(player)
                } else
                {
                    event.isCancelled = true
                    player.sendMessage(PMessage.ACTION_ERROR_BREAK_BLOCK_NO_PERMISSION.toString())
                }
            }
            val voteCrate = VoteCrate.getByLocation(plugin, event.block.location)
            if (voteCrate != null)
            {
                if (player.hasPermission("customvoting.crate"))
                {
                    voteCrate.delete(player)
                } else
                {
                    event.isCancelled = true
                    player.sendMessage(PMessage.ACTION_ERROR_BREAK_BLOCK_NO_PERMISSION.toString())
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
                VotePartyChest.create(plugin, event.block.location, player)
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
        val clicked = event.clickedBlock

        if (event.action != Action.RIGHT_CLICK_BLOCK || clicked == null)
        {
            return
        }

        if (clicked.type == Material.ENDER_CHEST)
        {
            val votePartyChest = VotePartyChest.getByLocation(plugin, clicked.location)
            if (votePartyChest != null)
            {
                event.isCancelled = true

                if (player.hasPermission("customvoting.voteparty"))
                {
                    SoundType.OPEN.play(plugin, player)
                    VotePartyRewardItemsEditor(plugin, null, votePartyChest.key).open(player)
                } else
                {
                    player.sendMessage(PMessage.ACTION_ERROR_OPEN_CHEST_NO_PERMISSION.toString())
                }
                return
            }
        }

        var voteCrate = VoteCrate.getByLocation(plugin, clicked.location)
        if (voteCrate != null && (event.item?.type != Material.TRIPWIRE_HOOK || event.item?.itemMeta?.lore?.get(0)
                ?.contains("#") == false)
        )
        {
            event.isCancelled = true
            player.sendMessage(PMessage.ACTION_ERROR_OPEN_CHEST_NEED_KEY.toString())
            return
        }

        if (!event.hasItem() || event.item!!.type != Material.TRIPWIRE_HOOK)
        {
            return
        }

        val item = event.item!!
        val lore = item.itemMeta?.lore?.get(0)

        if (lore == null || !lore.contains("#"))
        {
            return
        }

        val key = lore.split("#")[1].stripColor()
        voteCrate = VoteCrate.getByKey(plugin, key)

        if (voteCrate == null)
        {
            return
        }

        val meta = item.itemMeta!!
        meta.setDisplayName(PMessage.CRATE_ITEM_NAME_KEY_X.with(voteCrate.name))
        item.itemMeta = meta
        player.inventory.setItemInMainHand(item)

        if (voteCrate.loc == null)
        {
            if (player.hasPermission("customvoting.crate"))
            {
                event.isCancelled = true
                val loc = clicked.location
                val otherVoteCrate = VoteCrate.getByLocation(plugin, loc)

                if (otherVoteCrate == null)
                {
                    val confirm = object : ConfirmGUI(
                        plugin,
                        PMessage.CRATE_INVENTORY_NAME_PLACE_CONFIRM_X.with(voteCrate.name)
                    )
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
                            voteCrate.create(loc, player)
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
        } else if (clicked.location == voteCrate.loc)
        {
            event.isCancelled = true
            item.amount--
            player.inventory.setItemInMainHand(item)
            voteCrate.open(player)
        } else
        {
            event.isCancelled = true
            player.sendMessage(PMessage.ACTION_ERROR_INTERACT_NEED_CRATE.toString())
        }
    }
}