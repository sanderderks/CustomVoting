package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.pages.abstracts.AbstractItemEditor

class MilestoneRewardItemsEditor(
    plugin: CV,
    backPage: GUI?,
    number: Int
) : AbstractItemEditor(
    plugin,
    backPage,
    "${Data.MILESTONES}.$number.${Data.ITEM_REWARDS}",
    PMessage.MILESTONE_ITEM_NAME_REWARDS_X.with("$number"),
    27
)