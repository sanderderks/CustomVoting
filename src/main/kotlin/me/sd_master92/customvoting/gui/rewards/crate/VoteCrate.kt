package me.sd_master92.customvoting.gui.rewards.crate

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.addToInventoryOrDrop
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.helpers.ParticleHelper
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class VoteCrate(private val plugin: CV, private val player: Player, path: String) :
    GUI(plugin, (plugin.data.getString("$path.name") ?: "Vote Crate"), 45, false, true)
{
    private var searching = true
    private var closeInv = false
    private var rewards = mutableMapOf<Int, Array<ItemStack>>()
    private var allRewards = mutableListOf<ItemStack>()

    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        if (!closeInv)
        {
            object : BukkitRunnable()
            {
                override fun run()
                {
                    player.openInventory(inventory)
                }
            }.runTaskLater(plugin, 2)
        } else
        {
            SoundType.CLOSE.play(plugin, player)
        }
    }

    private fun giveReward(number: Int? = null)
    {
        val reward = if (number != null) rewards[number]!!.random() else BaseItem(
            Material.BARRIER,
            ChatColor.RED.toString() + "No price!"
        )

        inventory.clear()
        inventory.setItem(22, reward)

        if (number != null)
        {
            player.addToInventoryOrDrop(reward)
            ParticleHelper.shootFirework(plugin, player.location)
            player.sendMessage(
                ChatColor.GREEN.toString() + "You received a " +
                        ChatColor.AQUA + "$number% " + ChatColor.GREEN + "chance reward!"
            )
        } else
        {
            SoundType.FAILURE.play(plugin, player)
            player.sendMessage(ChatColor.RED.toString() + "No price this time :\"(")
        }
        closeInv = true
    }

    private fun shuffle(number: Int? = null)
    {
        searching = false
        var shuffle = 1
        var wait = 0L
        while (shuffle < 20)
        {
            wait += shuffle
            object : BukkitRunnable()
            {
                override fun run()
                {
                    for (i in 0 until inventory.size)
                    {
                        if (i != 22)
                        {
                            inventory.setItem(i, allRewards.random())
                        }
                    }
                    SoundType.SUCCESS.play(plugin, player)
                    player.updateInventory()
                }
            }.runTaskLater(plugin, wait)
            shuffle++
        }
        object : BukkitRunnable()
        {
            override fun run()
            {
                giveReward(number)
            }
        }.runTaskLater(plugin, wait)
    }

    fun run()
    {
        val chance = Random().nextInt(100)
        for (number in Data.CRATE_REWARD_CHANCES.sortedByDescending { it })
        {
            if (chance >= number)
            {
                if (searching)
                {
                    shuffle(number)
                } else
                {
                    break
                }
            }
        }
        if (searching)
        {
            shuffle()
        }
    }

    init
    {
        for (key in Data.CRATE_REWARD_CHANCES)
        {
            rewards[key] = plugin.data.getItems("$path.${Data.ITEM_REWARDS}.$key")
        }

        for (key in rewards.keys)
        {
            allRewards.addAll(rewards[key]!!)
        }
    }
}