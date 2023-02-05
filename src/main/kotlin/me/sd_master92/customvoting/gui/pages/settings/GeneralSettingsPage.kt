package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.carousel.VotePartyCountCarousel
import me.sd_master92.customvoting.gui.buttons.carousel.VotePartyTypeCarousel
import me.sd_master92.customvoting.gui.buttons.carousel.VotePartyVotesCarousel
import me.sd_master92.customvoting.gui.buttons.shortcuts.WorldDisabledOverviewShortcut
import me.sd_master92.customvoting.gui.buttons.switches.*
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class GeneralSettingsPage(private val plugin: CV) : GUI(plugin, PMessage.GENERAL_SETTINGS_INVENTORY_NAME.toString(), 18)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        VoteSettingsPage(plugin).open(player)
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
        addItem(MonthlyResetSwitch(plugin))
        addItem(MonthlyVotesSwitch(plugin))
        addItem(SoundEffectsSwitch(plugin))
        addItem(FireworkSwitch(plugin))
        addItem(LuckyVoteEnabledSwitch(plugin))
        addItem(VotePartyEnabledSwitch(plugin))
        addItem(VotePartyTypeCarousel(plugin))
        addItem(VotePartyVotesCarousel(plugin))
        addItem(VotePartyCountCarousel(plugin))
        addItem(WorldDisabledOverviewShortcut(plugin, this))
        addItem(UuidSupportSwitch(plugin))
    }
}

