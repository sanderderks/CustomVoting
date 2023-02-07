package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.core.appendWhenTrue
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.pages.abstracts.AbstractItemEditor
import me.sd_master92.customvoting.gui.pages.settings.RewardSettingsPage

class ItemRewardsEditor(private val plugin: CV, private val op: Boolean = false) :
    AbstractItemEditor(
        plugin,
        Data.ITEM_REWARDS.path.appendWhenTrue(op, Data.OP_REWARDS),
        PMessage.ITEM_REWARDS_INVENTORY_NAME.toString(),
        27
    )
{
    override val previousPage: GUI
        get() = RewardSettingsPage(plugin, op)
}