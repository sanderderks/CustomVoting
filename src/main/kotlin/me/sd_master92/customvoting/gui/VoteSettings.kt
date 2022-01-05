package me.sd_master92.customvoting.gui

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Materials
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.general.GeneralSettings
import me.sd_master92.customvoting.gui.items.BaseItem
import me.sd_master92.customvoting.gui.messages.MessageSettings
import me.sd_master92.customvoting.gui.rewards.RewardSettings
import me.sd_master92.customvoting.gui.support.Support
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class VoteSettings(private val plugin: CV) : GUI(plugin, "Vote Settings", 9, false, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.COMMAND_BLOCK   ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(GeneralSettings(plugin).inventory)
            }
            Material.DIAMOND         ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(RewardSettings(plugin).inventory)
            }
            Material.WRITABLE_BOOK   ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(MessageSettings(plugin).inventory)
            }
            Materials.SPYGLASS.get() ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(Support(plugin).inventory)
            }
            else                     ->
            {
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    companion object
    {
        val GENERAL_SETTINGS = BaseItem(
            Material.COMMAND_BLOCK, ChatColor.AQUA.toString() + "General",
            null, true
        )
        val REWARD_SETTINGS = BaseItem(
            Material.DIAMOND, ChatColor.LIGHT_PURPLE.toString() + "Rewards",
            null, true
        )
        val MESSAGES = BaseItem(
            Material.WRITABLE_BOOK, ChatColor.YELLOW.toString() + "Messages",
            null, true
        )
        val SUPPORT = BaseItem(
            Materials.SPYGLASS.get(), ChatColor.GREEN.toString() + "Support",
            null, true
        )
    }

    init
    {
        inventory.setItem(1, GENERAL_SETTINGS)
        inventory.setItem(3, REWARD_SETTINGS)
        inventory.setItem(5, MESSAGES)
        inventory.setItem(7, SUPPORT)
    }
}