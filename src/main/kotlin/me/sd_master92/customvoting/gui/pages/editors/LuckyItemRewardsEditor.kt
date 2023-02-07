package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.pages.abstracts.AbstractItemEditor
import me.sd_master92.customvoting.gui.pages.settings.RewardSettingsPage

class LuckyItemRewardsEditor(plugin: CV) :
    AbstractItemEditor(plugin, Data.LUCKY_REWARDS.path, PMessage.LUCKY_REWARDS_INVENTORY_NAME.toString(), 27)
{
    override val previousPage = RewardSettingsPage(plugin)
}