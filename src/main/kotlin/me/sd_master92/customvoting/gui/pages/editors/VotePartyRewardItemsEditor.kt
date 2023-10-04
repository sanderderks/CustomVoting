package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.core.inventory.BackButton
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.buttons.actions.VotePartyCreateAction
import me.sd_master92.customvoting.gui.buttons.actions.VotePartyDeleteAction
import me.sd_master92.customvoting.gui.pages.abstracts.AbstractItemEditor
import me.sd_master92.customvoting.subjects.voteparty.VotePartyChest

class VotePartyRewardItemsEditor(
    private val plugin: CV,
    backPage: GUI?,
    private val key: String
) :
    AbstractItemEditor(
        plugin,
        backPage,
        Data.VOTE_PARTY_CHESTS.path + ".$key",
        PMessage.VOTE_PARTY_INVENTORY_NAME_CHEST_X.with(key),
        54,
        stack = false
    )
{
    override fun newInstance(): GUI
    {
        return VotePartyRewardItemsEditor(plugin, backPage, key)
    }

    init
    {
        if (backPage == null)
        {
            setItem(nonClickableSizeWithNull - 1, BackButton(this))
        }
        setItem(nonClickableSizeWithNull - 1, VotePartyDeleteAction(plugin, this, key))
        if(VotePartyChest.getByKey(plugin, key)?.loc == null)
        {
            setItem(nonClickableSizeWithNull - 1, VotePartyCreateAction(plugin, this, key))
        }
    }
}