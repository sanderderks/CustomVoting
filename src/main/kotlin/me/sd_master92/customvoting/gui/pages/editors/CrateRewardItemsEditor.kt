package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.pages.abstracts.AbstractItemEditor

class CrateRewardItemsEditor(
    plugin: CV,
    backPage: GUI?,
    number: Int,
    percentage: Int
) :
    AbstractItemEditor(
        plugin,
        backPage,
        "${Data.VOTE_CRATES}.$number.${Data.ITEM_REWARDS}.$percentage",
        PMessage.CRATE_INVENTORY_NAME_PERC_REWARDS_XY.with(
            "$percentage",
            plugin.data.getString(Data.VOTE_CRATES.path + ".$number.name")
        ),
        27
    )