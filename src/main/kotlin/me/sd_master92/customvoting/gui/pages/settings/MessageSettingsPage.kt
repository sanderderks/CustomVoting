package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.shortcuts.VoteLinksEditorShortcut
import me.sd_master92.customvoting.gui.buttons.switches.*
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class MessageSettingsPage(private val plugin: CV, backPage: GUI?) :
    GUI(plugin, backPage, PMessage.MESSAGE_SETTINGS_INVENTORY_NAME.toString(), { 18 })
{
    override fun newInstance(): GUI
    {
        return MessageSettingsPage(plugin, backPage)
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        if (event.slot == 10)
        {
            cancelCloseEvent = true
            TaskTimer.delay(plugin)
            {
                newInstance().open(player)
            }.run()
        }
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
        addItem(VoteBroadcastSwitch(plugin))
        addItem(MilestoneBroadcastSwitch(plugin))
        addItem(StreakBroadcastSwitch(plugin))
        addItem(VotePartyUntilBroadcastSwitch(plugin))
        addItem(VotePartyCountBroadcastSwitch(plugin))
        addItem(VotePartyCountEndBroadcastSwitch(plugin))
        addItem(ArmorStandBreakMessageSwitch(plugin))
        addItem(WorldEnabledMessageSwitch(plugin))
        addItem(VoteRemindMessageSwitch(plugin))

        setItem(9, VoteLinksEditorShortcut(plugin, this))
        setItem(10, VoteLinksEnabledSwitch(plugin))
        if (!plugin.config.getBoolean(Setting.VOTE_LINK_INVENTORY.path))
        {
            setItem(11, VoteCommandOverrideSwitch(plugin))
            setItem(12, VoteDashboardEnabledSwitch(plugin))
        } else
        {
            setItem(11, VoteDashboardEnabledSwitch(plugin))
        }
    }
}