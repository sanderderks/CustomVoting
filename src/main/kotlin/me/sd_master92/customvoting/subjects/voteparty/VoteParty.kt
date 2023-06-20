package me.sd_master92.customvoting.subjects.voteparty

import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.addToInventoryOrDrop
import me.sd_master92.customvoting.constants.enumerations.*
import me.sd_master92.customvoting.gui.items.SimpleItem
import me.sd_master92.customvoting.withPlaceholders
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class VoteParty(private val plugin: CV)
{
    private var votePartyType = VotePartyType.valueOf(plugin.config.getNumber(Setting.VOTE_PARTY_TYPE.path))

    fun start()
    {
        if (!isActive)
        {
            isActive = true
            TaskTimer.repeat(plugin, 20, 0, plugin.config.getNumber(Setting.VOTE_PARTY_COUNTDOWN.path)) {
                when (it.count)
                {
                    30, 15, 10    ->
                    {
                        if (!plugin.config.getBoolean(Setting.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN.path))
                        {
                            val placeholders = HashMap<String, String>()
                            placeholders["%TIME%"] = "" + it.count
                            SoundType.NOTIFY.playForAll(plugin)
                            plugin.broadcastText(Message.VOTE_PARTY_COUNTDOWN, placeholders)
                        }
                    }

                    5, 4, 3, 2, 1 ->
                    {
                        if (!plugin.config.getBoolean(Setting.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING.path))
                        {
                            val placeholders = HashMap<String, String>()
                            placeholders["%TIME%"] = "" + it.count
                            placeholders["%s%"] = if (it.count == 1) "" else "s"
                            SoundType.NOTIFY.playForAll(plugin)
                            plugin.broadcastText(Message.VOTE_PARTY_COUNTDOWN_ENDING, placeholders)
                        }
                    }

                    0             ->
                    {
                        if (votePartyType == VotePartyType.RANDOMLY)
                        {
                            votePartyType = VotePartyType.random()
                        }
                        if (votePartyType == VotePartyType.PIG_HUNT)
                        {
                            SoundType.EXPLODE.playForAll(plugin)
                        } else
                        {
                            SoundType.VOTE_PARTY_START.playForAll(plugin)
                        }
                        val placeholders = HashMap<String, String>()
                        placeholders["%TYPE%"] = votePartyType.label()
                        plugin.broadcastText(Message.VOTE_PARTY_START, placeholders)
                        executeCommands()
                        when (votePartyType)
                        {
                            VotePartyType.EXPLODE_CHESTS ->
                            {
                                explode()
                            }

                            VotePartyType.SCARY          ->
                            {
                                scare()
                            }

                            VotePartyType.PIG_HUNT       ->
                            {
                                pigHunt()
                            }

                            else                         ->
                            {
                                dropChests()
                            }
                        }
                        it.cancel()
                    }
                }
            }.run()
        }
    }

    private fun executeCommands()
    {
        for (command in plugin.data.getStringList(Data.VOTE_PARTY_COMMANDS.path))
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
        val random = Random()

        TaskTimer.repeat(plugin, 10)
        {
            chests.removeIf { chest -> chest.isEmpty() }
            if (chests.isNotEmpty())
            {
                when (votePartyType)
                {
                    VotePartyType.ALL_CHESTS_AT_ONCE     ->
                    {
                        for (chest in chests)
                        {
                            chest.dropRandomItem()
                            chest.shootFirework()
                        }
                    }

                    VotePartyType.ONE_CHEST_AT_A_TIME    ->
                    {
                        val chest = chests.first()
                        chest.dropRandomItem()
                        chest.shootFirework()
                    }

                    VotePartyType.RANDOM_CHEST_AT_A_TIME ->
                    {
                        val chest = chests[random.nextInt(chests.size)]
                        chest.dropRandomItem()
                        chest.shootFirework()
                    }

                    VotePartyType.ADD_TO_INVENTORY       ->
                    {
                        val chest = chests[random.nextInt(chests.size)]
                        val players = ArrayList(Bukkit.getOnlinePlayers())
                        val player = players[random.nextInt(players.size)]

                        player.addToInventoryOrDrop(chest.popRandomItem())
                        SoundType.PICKUP.play(plugin, player)
                    }

                    else                                 ->
                    {
                    }
                }
            } else
            {
                stop(plugin)
                it.cancel()
            }
        }.run()
    }

    private fun explode()
    {
        val chests = VotePartyChest.getAll(plugin)
        val random = Random()

        TaskTimer.repeat(plugin, 30)
        {
            if (chests.isNotEmpty())
            {
                val chest = chests[random.nextInt(chests.size)]
                chest.explode()
                chests.remove(chest)
            } else
            {
                stop(plugin)
                it.cancel()
            }
        }.run()
    }

    private fun scare()
    {
        val chests = VotePartyChest.getAll(plugin)
        val random = Random()

        chests.firstOrNull()?.loc?.let { loc ->
            val players = loc.world!!.getNearbyEntities(loc, 50.0, 50.0, 50.0).filterIsInstance<Player>()
            for (player in players)
            {
                player.addPotionEffect(
                    PotionEffect(PotionEffectType.getByName("DARKNESS") ?: PotionEffectType.CONFUSION, 20 * 8, 1)
                )
                player.setPlayerTime(18000, false)
            }
            SoundType.SCARY_1.play(plugin, loc)
            TaskTimer.delay(plugin, 20 * 4)
            {
                SoundType.SCARY_2.play(plugin, loc)
            }.run().then(
                TaskTimer.delay(plugin, 20 * 2)
                {
                    SoundType.SCARY_3.play(plugin, loc)
                }).then(
                TaskTimer.delay(plugin, 20 * 6)
                {
                    for (player in players)
                    {
                        player.resetPlayerTime()
                    }
                }).then(
                TaskTimer.delay(plugin, 20)
                {
                    stop(plugin)
                })

            while (chests.isNotEmpty())
            {
                val chest = chests[random.nextInt(chests.size)]
                chest.scare()
                chests.remove(chest)
            }
        }
    }

    private fun pigHunt()
    {
        for (chest in VotePartyChest.getAll(plugin))
        {
            chest.convertToPig()
        }
    }

    companion object
    {
        val VOTE_PARTY_ITEM = SimpleItem(
            Material.ENDER_CHEST,
            PMessage.VOTE_PARTY_ITEM_NAME_CHEST.toString(),
            PMessage.VOTE_PARTY_ITEM_LORE_CHEST.toString()
        )
        private val queue: MutableList<VoteParty> = ArrayList()
        private var isActive = false

        fun stop(plugin: CV)
        {
            plugin.broadcastText(Message.VOTE_PARTY_END)

            isActive = false
            queue.removeFirstOrNull()
            queue.firstOrNull()?.start()
        }
    }

    init
    {
        queue.add(this)
    }
}