package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.pages.abstracts.AbstractItemEditor
import me.sd_master92.customvoting.gui.pages.settings.MilestoneSettingsPage

class MilestoneItemRewardsEditor(plugin: CV, number: Int) : AbstractItemEditor(
    plugin,
    "${Data.MILESTONES}.$number.${Data.ITEM_REWARDS}",
    PMessage.MILESTONE_ITEM_NAME_REWARDS_X.with("$number"), 27
)
{
    override val previousPage = MilestoneSettingsPage(plugin, number)
}