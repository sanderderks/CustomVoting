package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.carousel.VotePartyCountCarousel
import me.sd_master92.customvoting.gui.buttons.carousel.VotePartyTypeCarousel
import me.sd_master92.customvoting.gui.buttons.carousel.VotePartyVotesCarousel
import me.sd_master92.customvoting.gui.buttons.inputfields.VotePartyCommandsInput
import me.sd_master92.customvoting.gui.buttons.inputfields.VotePartyPigHuntDamage
import me.sd_master92.customvoting.gui.buttons.inputfields.VotePartyPigHuntHealth
import me.sd_master92.customvoting.gui.buttons.shortcuts.VotePartyChestOverviewShortcut
import me.sd_master92.customvoting.gui.buttons.switches.VotePartyEnabledSwitch
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class VotePartySettingsPage(private val plugin: CV, backPage: GUI?) :
    GUI(plugin, backPage, PMessage.VOTE_PARTY_INVENTORY_NAME_SETTINGS.toString(), 9)
{
    override fun newInstance(): GUI
    {
        return VotePartySettingsPage(plugin, backPage)
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
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
        addItem(VotePartyEnabledSwitch(plugin))
        addItem(VotePartyTypeCarousel(plugin))
        addItem(VotePartyCommandsInput(plugin, this))
        addItem(VotePartyVotesCarousel(plugin))
        addItem(VotePartyCountCarousel(plugin))
        addItem(VotePartyChestOverviewShortcut(plugin, this))
        addItem(VotePartyPigHuntHealth(plugin, this))
        addItem(VotePartyPigHuntDamage(plugin, this))
    }
}

