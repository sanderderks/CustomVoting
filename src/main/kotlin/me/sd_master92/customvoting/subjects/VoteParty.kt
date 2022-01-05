package me.sd_master92.customvoting.subjects

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.VotePartyType
import me.sd_master92.customvoting.gui.GUI
import me.sd_master92.customvoting.helpers.ParticleHelper
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.function.Consumer

class VoteParty(private val plugin: CV)
{
    private val votePartyType: Int = plugin.config.getNumber(Settings.VOTE_PARTY_TYPE)
    private var count: Int = plugin.config.getNumber(Settings.VOTE_PARTY_COUNTDOWN)
    fun start()
    {
        queue.add(this)
        if (!isActive)
        {
            isActive = true
            object : BukkitRunnable()
            {
                override fun run()
                {
                    when (count)
                    {
                        30, 15, 10    ->
                        {
                            if (!plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN))
                            {
                                val placeholders = HashMap<String, String>()
                                placeholders["%TIME%"] = "" + count
                                SoundType.NOTIFY.playForAll(plugin)
                                plugin.server.broadcastMessage(
                                    Messages.VOTE_PARTY_COUNTDOWN.getMessage(
                                        plugin,
                                        placeholders
                                    )
                                )
                            }
                        }
                        5, 4, 3, 2, 1 ->
                        {
                            if (!plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING))
                            {
                                val placeholders = HashMap<String, String>()
                                placeholders["%TIME%"] = "" + count
                                placeholders["%s%"] = if (count == 1) "" else "s"
                                SoundType.NOTIFY.playForAll(plugin)
                                plugin.server.broadcastMessage(
                                    Messages.VOTE_PARTY_COUNTDOWN_ENDING.getMessage(
                                        plugin,
                                        placeholders
                                    )
                                )
                            }
                        }
                        0             ->
                        {
                            SoundType.VOTE_PARTY_START.playForAll(plugin)
                            plugin.server.broadcastMessage(Messages.VOTE_PARTY_START.getMessage(plugin))
                            when (votePartyType)
                            {
                                VotePartyType.RANDOM_CHEST_AT_A_TIME.value ->
                                {
                                    dropChestsRandomly()
                                }
                                VotePartyType.ADD_TO_INVENTORY.value       ->
                                {
                                    addToInventory()
                                }
                                else                                       ->
                                {
                                    dropChests()
                                }
                            }
                            cancel()
                        }
                    }
                    count--
                }
            }.runTaskTimer(plugin, 0, 20)
        }
    }

    private fun stop()
    {
        isActive = false
        queue.removeFirstOrNull()
        if (queue.isNotEmpty())
        {
            queue[0].start()
        }
    }

    private fun dropChests()
    {
        val locations = plugin.data.getLocations(Data.VOTE_PARTY)
        val random = Random()
        val tasks: MutableMap<Int, Boolean> = HashMap()
        for (key in locations.keys)
        {
            val chest: MutableList<ItemStack> = ArrayList(listOf(*plugin.data.getItems(Data.VOTE_PARTY + "." + key)))
            val loc = locations[key]!!.clone().add(0.5, 0.0, 0.5)
            val dropLoc = Location(loc.world, loc.x, loc.y - 1, loc.z)
            val fireworkLoc = Location(loc.world, loc.x, loc.y + 1, loc.z)
            object : BukkitRunnable()
            {
                override fun run()
                {
                    if (!tasks.containsKey(taskId))
                    {
                        tasks[taskId] = false
                    }
                    if (votePartyType == VotePartyType.ALL_CHESTS_AT_ONCE.value || !tasks.containsValue(true) || tasks[taskId]!!)
                    {
                        if (votePartyType == VotePartyType.ONE_CHEST_AT_A_TIME.value)
                        {
                            tasks[taskId] = true
                        }
                        if (chest.isNotEmpty())
                        {
                            val i = random.nextInt(chest.size)
                            if (dropLoc.world != null)
                            {
                                dropLoc.world!!.dropItem(dropLoc, chest.removeAt(i))
                            }
                            if (random.nextInt(2) == 0)
                            {
                                ParticleHelper.shootFirework(plugin, fireworkLoc)
                            }
                        } else
                        {
                            tasks.remove(taskId)
                            if (tasks.isEmpty())
                            {
                                plugin.server.broadcastMessage(Messages.VOTE_PARTY_END.getMessage(plugin))
                                stop()
                            }
                            cancel()
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 10)
        }
    }

    private fun dropChestsRandomly()
    {
        val locations = plugin.data.getLocations(Data.VOTE_PARTY)
        val chests: MutableMap<String, MutableList<ItemStack>> = HashMap()
        val keys: MutableList<String> = ArrayList(locations.keys)
        keys.forEach(Consumer { key: String ->
            val items = plugin.data.getItems(
                Data.VOTE_PARTY + "." + key
            )
            if (items.isNotEmpty())
            {
                chests[key] = ArrayList(listOf(*items))
            }
        })
        val random = Random()
        object : BukkitRunnable()
        {
            override fun run()
            {
                if (keys.isNotEmpty() && chests.isNotEmpty())
                {
                    val key = keys[random.nextInt(keys.size)]
                    val chest = chests[key]!!
                    val loc = locations[key]!!.clone().add(0.5, 0.0, 0.5)
                    val dropLoc = Location(loc.world, loc.x, loc.y - 1, loc.z)
                    val fireworkLoc = Location(loc.world, loc.x, loc.y + 1, loc.z)
                    if (dropLoc.world != null)
                    {
                        dropLoc.world!!.dropItem(dropLoc, chest.removeAt(random.nextInt(chest.size)))
                        if (chest.isEmpty())
                        {
                            keys.remove(key)
                        } else
                        {
                            chests[key] = chest
                        }
                    }
                    if (random.nextInt(3) == 0)
                    {
                        ParticleHelper.shootFirework(plugin, fireworkLoc)
                    }
                } else
                {
                    plugin.server.broadcastMessage(Messages.VOTE_PARTY_END.getMessage(plugin))
                    stop()
                    cancel()
                }
            }
        }.runTaskTimer(plugin, 0, 10)
    }

    private fun addToInventory()
    {
        val random = Random()
        val tasks: MutableMap<Int, Boolean> = HashMap()
        for (key in plugin.data.getLocations(Data.VOTE_PARTY).keys)
        {
            val chest: MutableList<ItemStack> = ArrayList(listOf(*plugin.data.getItems(Data.VOTE_PARTY + "." + key)))
            object : BukkitRunnable()
            {
                override fun run()
                {
                    if (!tasks.containsKey(taskId))
                    {
                        tasks[taskId] = false
                    }
                    if (!tasks.containsValue(true) || tasks[taskId]!!)
                    {
                        tasks[taskId] = true
                        if (chest.isNotEmpty())
                        {
                            val players: List<Player> = ArrayList(Bukkit.getOnlinePlayers())
                            val player = players[random.nextInt(players.size)]
                            val item: ItemStack = chest.removeAt(random.nextInt(chest.size))
                            for (left in player.inventory.addItem(item).values)
                            {
                                player.world.dropItem(player.location, left!!)
                            }
                            SoundType.PICKUP.play(plugin, player)
                        } else
                        {
                            tasks.remove(taskId)
                            if (tasks.isEmpty())
                            {
                                plugin.server.broadcastMessage(Messages.VOTE_PARTY_END.getMessage(plugin))
                                stop()
                            }
                            cancel()
                        }
                    }
                }
            }.runTaskTimer(plugin, 0, 10)
        }
    }

    companion object
    {
        val VOTE_PARTY_ITEM = GUI.createItem(
            Material.ENDER_CHEST, ChatColor.LIGHT_PURPLE.toString() +
                    "Vote Party Chest",
            ChatColor.GRAY.toString() + "Place this chest somewhere in the sky.;" + ChatColor.GRAY + "The contents of this chest" +
                    " will;" +
                    ChatColor.GRAY + "start dropping when the voteparty starts."
        )
        private val queue: MutableList<VoteParty> = ArrayList()
        private var isActive = false
    }

}