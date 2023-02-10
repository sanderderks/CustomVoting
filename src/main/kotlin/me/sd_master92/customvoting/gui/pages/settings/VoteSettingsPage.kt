package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.shortcuts.GeneralSettingsShortcut
import me.sd_master92.customvoting.gui.buttons.shortcuts.MessageSettingsShortcut
import me.sd_master92.customvoting.gui.buttons.shortcuts.RewardOverviewShortcut
import me.sd_master92.customvoting.gui.buttons.shortcuts.SupportSettingsShortcut
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent

class VoteSettingsPage(private val plugin: CV, backPage: GUI?) :
    GUI(plugin, backPage, PMessage.SETTINGS_INVENTORY_NAME.toString(), 9)
{
    override fun newInstance(): GUI
    {
        return VoteSettingsPage(plugin, backPage)
    }

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
        setItem(1, GeneralSettingsShortcut(plugin, this))
        setItem(3, RewardOverviewShortcut(plugin, this))
        setItem(5, MessageSettingsShortcut(plugin, this))
        setItem(7, SupportSettingsShortcut(plugin, this))
    }
}