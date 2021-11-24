package me.sd_master92.customvoting.gui

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class VotePartyRewards(private val plugin: Main, private val key: String) : GUI(plugin, NAME + key, 54, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        if (plugin.data.setItems(Data.VOTE_PARTY + "." + key, event.inventory.contents))
        {
            SoundType.SUCCESS.play(plugin, player)
            player.sendMessage(ChatColor.GREEN.toString() + "Successfully updated Vote Party Chest #" + key)
        } else
        {
            SoundType.FAILURE.play(plugin, player)
            player.sendMessage(ChatColor.RED.toString() + "Failed to update Vote Party Chest #" + key)
        }
    }

    companion object
    {
        const val NAME = "Vote Party Chest #"
    }

    init
    {
        inventory.contents = plugin.data.getItems(Data.VOTE_PARTY + "." + key)
    }
}