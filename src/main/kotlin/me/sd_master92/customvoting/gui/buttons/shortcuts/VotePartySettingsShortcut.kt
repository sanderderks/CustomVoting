package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.settings.VotePartySettingsPage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VotePartySettingsShortcut(
    private val plugin: CV,
    private val currentPage: GUI
) : BaseItem(
    Material.ENDER_CHEST,
    PMessage.VOTE_PARTY_ITEM_NAME_SETTINGS.toString(),
    PMessage.VOTE_PARTY_ITEM_LORE_SETTINGS.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        currentPage.cancelCloseEvent = true
        VotePartySettingsPage(plugin, currentPage).open(player)
    }
}