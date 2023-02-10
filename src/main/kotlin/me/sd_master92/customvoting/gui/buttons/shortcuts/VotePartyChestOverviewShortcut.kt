package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.overviews.VotePartyChestOverview
import me.sd_master92.customvoting.subjects.voteparty.VotePartyChest
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VotePartyChestOverviewShortcut(
    private val plugin: CV,
    private val currentPage: GUI
) : BaseItem(
    Material.ENDER_CHEST,
    PMessage.VOTE_PARTY_ITEM_NAME_OVERVIEW.toString(),
    enchanted = true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        currentPage.cancelCloseEvent = true
        VotePartyChestOverview(plugin, currentPage).open(player)
    }

    init
    {
        val size = VotePartyChest.getAll(plugin).size
        setLore(
            PMessage.GENERAL_ITEM_LORE_CURRENT_XY.with(
                "$size",
                PMessage.VOTE_PARTY_UNIT_CHEST_MULTIPLE.toString()
            )
        )
    }
}