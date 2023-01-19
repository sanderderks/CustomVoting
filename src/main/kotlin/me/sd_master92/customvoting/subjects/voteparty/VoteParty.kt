package me.sd_master92.customvoting.subjects.voteparty

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.addToInventoryOrDrop
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.VotePartyType
import me.sd_master92.customvoting.withPlaceholders
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class VoteParty(private val plugin: CV)
{
    private val votePartyType: Int = plugin.config.getNumber(Settings.VOTE_PARTY_TYPE.path)
    private var count: Int = plugin.config.getNumber(Settings.VOTE_PARTY_COUNTDOWN.path)
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
                            if (!plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN.path))
                            {
                                val placeholders = HashMap<String, String>()
                                placeholders["%TIME%"] = "" + count
                                SoundType.NOTIFY.playForAll(plugin)
                                plugin.broadcastText(Messages.VOTE_PARTY_COUNTDOWN, placeholders)
                            }
                        }

                        5, 4, 3, 2, 1 ->
                        {
                            if (!plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING.path))
                            {
                                val placeholders = HashMap<String, String>()
                                placeholders["%TIME%"] = "" + count
                                placeholders["%s%"] = if (count == 1) "" else "s"
                                SoundType.NOTIFY.playForAll(plugin)
                                plugin.broadcastText(Messages.VOTE_PARTY_COUNTDOWN_ENDING, placeholders)
                            }
                        }

                        0             ->
                        {
                            SoundType.VOTE_PARTY_START.playForAll(plugin)
                            plugin.server.broadcastMessage(Messages.VOTE_PARTY_START.getMessage(plugin))
                            executeCommands()
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

    private fun executeCommands()
    {
        for (command in plugin.data.getStringList(Data.VOTE_PARTY_COMMANDS))
        {
            if (command.contains("%PLAYER%"))
            {
                for (player in Bukkit.getOnlinePlayers())
                {
                    plugin.runCommand(command.replace("%PLAYER%", player.name).withPlaceholders(player))
                }
            } else
            {
                plugin.runCommand(command)
            }
        }
    }

    private fun dropChests()
    {
        val chests = VotePartyChest.getAll(plugin)
        val tasks: MutableMap<Int, Boolean> = HashMap()

        for (chest in chests)
        {
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
                            chest.dropRandomItem()
                            chest.shootFirework()
                        } else
                        {
                            tasks.remove(taskId)
                            if (tasks.isEmpty())
                            {
                                plugin.broadcastText(Messages.VOTE_PARTY_END)
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
        val chests = VotePartyChest.getAll(plugin)
        val random = Random()
        object : BukkitRunnable()
        {
            override fun run()
            {
                if (chests.isNotEmpty())
                {
                    val chest = chests[random.nextInt(chests.size)]
                    chest.dropRandomItem()
                    if (chest.isEmpty())
                    {
                        chests.remove(chest)
                    }
                    chest.shootFirework()
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
        val chests = VotePartyChest.getAll(plugin)
        val random = Random()
        val tasks: MutableMap<Int, Boolean> = HashMap()
        for (chest in chests)
        {
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
                            player.addToInventoryOrDrop(chest.popRandomItem())
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
        val VOTE_PARTY_ITEM = BaseItem(
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