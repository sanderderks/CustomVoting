package me.sd_master92.customvoting.gui.pages.menus

import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.addToInventoryOrDrop
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.items.CrateKeyItem
import me.sd_master92.customvoting.gui.items.SimpleItem
import me.sd_master92.customvoting.helpers.ParticleHelper
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class CrateMenu(private val plugin: CV, private val player: Player, private val key: String) :
    GUI(
        plugin,
        null,
        (plugin.data.getString(Data.VOTE_CRATES.path + ".$key.name") ?: PMessage.CRATE_INVENTORY_NAME.toString()),
        { 45 }
    )
{
    private var searching = true
    private var rewards = mutableMapOf<Int, Array<ItemStack>>()
    private var allRewards = mutableListOf<ItemStack>()
    private val tasks = mutableListOf<TaskTimer>()
    private var chanceBox: Int? = null

    override fun newInstance(): GUI
    {
        return this
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        if (keepAlive)
        {
            TaskTimer.delay(plugin) { open(player) }.run()
        } else if (tasks.isNotEmpty())
        {
            tasks.forEach { it.cancel() }
            tasks.clear()
            giveReward()
        } else
        {
            SoundType.CLOSE.play(plugin, player)
        }
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    private fun giveReward()
    {
        val alwaysReward = plugin.data.getBoolean(Data.VOTE_CRATES.path + ".$key.always")

        if (alwaysReward && (chanceBox == null || this.rewards[chanceBox]?.isEmpty() == true))
        {
            chanceBox = Data.CRATE_REWARD_CHANCES.firstOrNull { this.rewards[it]?.isNotEmpty() == true }
        }

        val rewards = this.rewards[chanceBox]
        val reward = if (rewards?.isNotEmpty() == true) rewards.random() else null
        val none = SimpleItem(
            Material.BARRIER,
            PMessage.CRATE_ITEM_NAME_NO_PRIZE.toString()
        )

        clear()
        setItem(22, reward ?: none)

        if (chanceBox != null && reward != null)
        {
            player.addToInventoryOrDrop(reward)
            ParticleHelper.shootFirework(plugin, player.location)
            player.sendMessage(PMessage.CRATE_MESSAGE_REWARD_X.with("$chanceBox"))
        } else
        {
            SoundType.FAILURE.play(plugin, player)
            player.sendMessage(PMessage.CRATE_MESSAGE_NO_PRICE.toString())
        }
        tasks.clear()
        keepAlive = false
    }

    private fun shuffle()
    {
        var shuffle = 1
        var wait = 0L
        while (shuffle < 20)
        {
            wait += shuffle
            tasks.add(
                TaskTimer.delay(plugin, wait)
                {
                    for (i in 0 until size)
                    {
                        if (i != 22)
                        {
                            setItem(i, allRewards.random())
                        }
                    }
                    SoundType.SUCCESS.play(plugin, player)
                    player.updateInventory()
                }.run()
            )
            shuffle++
        }
        tasks.add(TaskTimer.delay(plugin, wait) {
            giveReward()
        }.run())
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
                    chanceBox = number
                    searching = false
                    shuffle()
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
            player.sendMessage(PMessage.CRATE_ERROR_EMPTY.toString())
            player.inventory.addItem(CrateKeyItem(plugin, key.toInt()))
        }
    }

    init
    {
        for (key in Data.CRATE_REWARD_CHANCES)
        {
            rewards[key] = plugin.data.getItems(Data.VOTE_CRATES.path + ".${this.key}.${Data.ITEM_REWARDS}.$key")
        }

        for (key in rewards.keys)
        {
            allRewards.addAll(rewards[key]!!)
        }

        if (!plugin.config.getBoolean(Setting.ALLOW_CRATE_CLOSE.path))
        {
            keepAlive = true
        }
    }
}