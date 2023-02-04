package me.sd_master92.customvoting.gui

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.VMaterial
import me.sd_master92.customvoting.gui.general.GeneralSettings
import me.sd_master92.customvoting.gui.messages.MessageSettings
import me.sd_master92.customvoting.gui.rewards.RewardSettings
import me.sd_master92.customvoting.gui.support.Support
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

class VoteSettings(private val plugin: CV) : GUI(plugin, PMessage.SETTINGS_INVENTORY_NAME.toString(), 9)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.COMMAND_BLOCK   ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(GeneralSettings(plugin).inventory)
            }

            Material.DIAMOND         ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(RewardSettings(plugin).inventory)
            }

            Material.WRITABLE_BOOK   ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(MessageSettings(plugin).inventory)
            }

            VMaterial.SPYGLASS.get() ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
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

    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent)
    {
        if (isThisInventory(event) && !plugin.config.getBoolean(Setting.SETTINGS_ENABLED.path))
        {
            event.isCancelled = true
            event.player.sendMessage(PMessage.SETTINGS_ERROR_DISABLED.toString())
        }
    }

    companion object
    {
        val GENERAL_SETTINGS = BaseItem(
            Material.COMMAND_BLOCK, PMessage.SETTINGS_ITEM_NAME_GENERAL.toString(),
            null, true
        )
        val REWARD_SETTINGS = BaseItem(
            Material.DIAMOND, PMessage.SETTINGS_ITEM_NAME_REWARDS.toString(),
            null, true
        )
        val MESSAGES = BaseItem(
            Material.WRITABLE_BOOK, PMessage.SETTINGS_ITEM_NAME_MESSAGES.toString(),
            null, true
        )
        val SUPPORT = BaseItem(
            VMaterial.SPYGLASS.get(), PMessage.SETTINGS_ITEM_NAME_SUPPORT.toString(),
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