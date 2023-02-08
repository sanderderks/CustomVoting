package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.shortcuts.VoteLinksEditorShortcut
import me.sd_master92.customvoting.gui.buttons.switches.*
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class MessageSettingsPage(private val plugin: CV, backPage: GUI?) :
    GUI(plugin, backPage, PMessage.MESSAGE_SETTINGS_INVENTORY_NAME.toString(), 18)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
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

    init
    {
        addItem(VoteLinksEditorShortcut(plugin, this))
        addItem(VoteLinksEnabledSwitch(plugin))
        addItem(VoteBroadcastSwitch(plugin))
        addItem(MilestoneBroadcastSwitch(plugin))
        addItem(VotePartyUntilBroadcastSwitch(plugin))
        addItem(VotePartyCountBroadcastSwitch(plugin))
        addItem(VotePartyCountEndBroadcastSwitch(plugin))
        addItem(ArmorStandBreakMessageSwitch(plugin))
        addItem(WorldEnabledMessageSwitch(plugin))
        addItem(VoteRemindMessageSwitch(plugin))
    }
}