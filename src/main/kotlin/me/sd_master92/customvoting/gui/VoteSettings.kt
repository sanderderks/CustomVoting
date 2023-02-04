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

class VoteSettings(private val plugin: CV) : GUI(plugin, PMessage.SETTINGS_INVENTORY_NAME.toString(), 9, back = false)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
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

    init
    {
        setItem(1, object : BaseItem(
            Material.COMMAND_BLOCK, PMessage.SETTINGS_ITEM_NAME_GENERAL.toString(),
            null, true
        )
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                GeneralSettings(plugin).open(player)
            }
        })
        setItem(3, object : BaseItem(
            Material.DIAMOND, PMessage.SETTINGS_ITEM_NAME_REWARDS.toString(),
            null, true
        )
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                RewardSettings(plugin).open(player)
            }
        })
        setItem(5, object : BaseItem(
            Material.WRITABLE_BOOK, PMessage.SETTINGS_ITEM_NAME_MESSAGES.toString(),
            null, true
        )
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                MessageSettings(plugin).open(player)
            }
        })
        setItem(7, object : BaseItem(
            VMaterial.SPYGLASS.get(), PMessage.SETTINGS_ITEM_NAME_SUPPORT.toString(),
            null, true
        )
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                Support(plugin).open(player)
            }
        })
    }
}