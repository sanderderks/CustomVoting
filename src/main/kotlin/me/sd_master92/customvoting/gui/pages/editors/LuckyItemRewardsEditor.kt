package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.settings.RewardSettingsPage
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

class LuckyItemRewardsEditor(private val plugin: CV) : GUI(plugin, name, 27, false, save = true)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        event.isCancelled = true
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        RewardSettingsPage(plugin).open(player)
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        save(plugin, player, event.inventory)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
        event.isCancelled = true
        save(plugin, player, event.inventory)
        cancelCloseEvent = true
        RewardSettingsPage(plugin).open(player)
    }

    companion object
    {
        private val name = PMessage.LUCKY_REWARDS_INVENTORY_NAME.toString()
        fun save(plugin: CV, player: Player, inv: Inventory)
        {
            inv.setItem(25, null)
            inv.setItem(26, null)
            if (plugin.data.getItems(Data.LUCKY_REWARDS.path)
                    .contentEquals(inv.contents.filterNotNull().toTypedArray())
            )
            {
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_NOTHING_CHANGED.toString())
            } else if (plugin.data.setItems(Data.LUCKY_REWARDS.path, inv.contents))
            {
                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_SUCCESS_X.with(name))
            } else
            {
                SoundType.FAILURE.play(plugin, player)
                player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_FAIL_X.with(name))
            }
        }
    }

    init
    {
        for (reward in plugin.data.getItems(Data.LUCKY_REWARDS.path))
        {
            addItem(reward)
        }
    }
}