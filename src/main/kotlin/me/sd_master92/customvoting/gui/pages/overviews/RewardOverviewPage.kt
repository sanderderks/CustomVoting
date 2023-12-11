package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.shortcuts.*
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class RewardOverviewPage(private val plugin: CV, backPage: GUI?) :
    GUI(plugin, backPage, PMessage.REWARD_SETTINGS_INVENTORY_NAME_OVERVIEW.toString())
{
    override fun newInstance(): GUI
    {
        return RewardOverviewPage(plugin, backPage)
    }

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
        addItem(VoteRewardSettingsShortcut(plugin, this))
        addItem(PowerRewardSettingsShortcut(plugin, this))
        addItem(LuckySettingsShortcut(plugin, this))
        addItem(MilestoneOverviewShortcut(plugin, this))
        addItem(StreaksSettingsShortcut(plugin, this))
        addItem(VotePartySettingsShortcut(plugin, this))
        addItem(CrateOverviewShortcut(plugin, this))
    }
}