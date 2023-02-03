package me.sd_master92.customvoting.gui.rewards.crate

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.addToInventoryOrDrop
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.helpers.ParticleHelper
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class VoteCrate(private val plugin: CV, private val player: Player, path: String) :
    GUI(plugin, (plugin.data.getString("$path.name") ?: Strings.GUI_TITLE_VOTE_CRATE.toString()), 45)
{
    private var searching = true
    private var rewards = mutableMapOf<Int, Array<ItemStack>>()
    private var allRewards = mutableListOf<ItemStack>()

    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        if (keepAlive)
        {
            TaskTimer.delay(plugin)
            {
                player.openInventory(inventory)
            }.run()
        } else
        {
            SoundType.CLOSE.play(plugin, player)
        }
    }

    private fun giveReward(number: Int? = null)
    {
        val rewards = if (number != null && this.rewards.isNotEmpty()) this.rewards[number] else null
        val reward = if (rewards?.isNotEmpty() == true) rewards.random() else null

        inventory.clear()
        inventory.setItem(
            22, reward ?: BaseItem(
                Material.BARRIER,
                Strings.VOTE_CRATE_NO_PRICE.toString()
            )
        )

        if (number != null && reward != null)
        {
            player.addToInventoryOrDrop(reward)
            ParticleHelper.shootFirework(plugin, player.location)
            player.sendMessage(Strings.VOTE_CRATE_REWARD_X.with("$number"))
        } else
        {
            SoundType.FAILURE.play(plugin, player)
            player.sendMessage(Strings.VOTE_CRATE_NO_PRICE_MESSAGE.toString())
        }
        keepAlive = false
    }

    private fun shuffle(number: Int? = null)
    {
        var shuffle = 1
        var wait = 0L
        while (shuffle < 20)
        {
            wait += shuffle
            TaskTimer.delay(plugin, wait)
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
            }.run()
            shuffle++
        }
        TaskTimer.delay(plugin, wait) { giveReward(number) }.run()
    }

    fun run()
    {
        if (allRewards.isNotEmpty())
        {
            val chance = Random().nextInt(100)
            var min = 0
            for (number in rewards.keys.sortedBy { it })
            {
                if (chance in min until number + min)
                {
                    searching = false
                    shuffle(number)
                    break
                } else
                {
                    min += number
                }
            }
            if (searching)
            {
                shuffle()
            }
        } else
        {
            keepAlive = false
            player.closeInventory()
            player.sendMessage(Strings.VOTE_CRATE_EMPTY.toString())
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
        keepAlive = true
    }
}