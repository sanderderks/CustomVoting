package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.pages.abstracts.AbstractItemEditor
import me.sd_master92.customvoting.gui.pages.overviews.VotePartyChestOverview

class VotePartyItemRewardsEditor(private val plugin: CV, key: String, private val saveAndClose: Boolean = false) :
    AbstractItemEditor(
        plugin,
        Data.VOTE_PARTY_CHESTS.path + ".$key",
        PMessage.VOTE_PARTY_INVENTORY_NAME_CHEST_X.with(key),
        54,
        stack = false
    )
{
    override val previousPage: GUI?
        get() = if (saveAndClose) null else VotePartyChestOverview(plugin)
}