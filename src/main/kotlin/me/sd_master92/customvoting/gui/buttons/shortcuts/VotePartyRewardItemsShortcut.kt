package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractRewardItemsButton
import me.sd_master92.customvoting.gui.pages.editors.VotePartyRewardItemsEditor
import me.sd_master92.customvoting.subjects.voteparty.VotePartyChest
import org.bukkit.Material
import org.bukkit.entity.Player

class VotePartyRewardItemsShortcut(
    private val plugin: CV,
    private val currentPage: GUI,
    private val key: String
) : AbstractRewardItemsButton(
    plugin,
    currentPage,
    Data.VOTE_PARTY_CHESTS.path + ".$key",
    PMessage.VOTE_PARTY_ITEM_NAME_CHEST_X.with(key),
    Material.ENDER_CHEST
)
{
    override fun onOpen(player: Player)
    {
        VotePartyRewardItemsEditor(plugin, currentPage, key).open(player)
    }

    init
    {
        if(VotePartyChest.getByKey(plugin, key)?.loc == null)
        {
            addLore(
                PMessage.GENERAL_ITEM_LORE_STATUS_X.with(PMessage.VOTE_PARTY_ITEM_LORE_LOCATION_STATUS.toString())
            )
        }
    }
}