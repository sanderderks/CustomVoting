package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.core.inventory.BackButton
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.pages.abstracts.AbstractItemEditor

class VotePartyRewardItemsEditor(plugin: CV, backPage: GUI?, key: String) :
    AbstractItemEditor(
        plugin,
        backPage,
        Data.VOTE_PARTY_CHESTS.path + ".$key",
        PMessage.VOTE_PARTY_INVENTORY_NAME_CHEST_X.with(key),
        54,
        stack = false
    )
{
    init
    {
        if (backPage == null)
        {
            setItem(52, BackButton(this))
        }
    }
}