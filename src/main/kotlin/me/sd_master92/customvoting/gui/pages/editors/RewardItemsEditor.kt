package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.core.appendWhenTrue
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.pages.abstracts.AbstractItemEditor

class RewardItemsEditor(plugin: CV, backPage: GUI?, power: Boolean = false) :
    AbstractItemEditor(
        plugin,
        backPage,
        Data.ITEM_REWARDS.path.appendWhenTrue(power, Data.POWER_REWARDS),
        PMessage.ITEM_REWARDS_INVENTORY_NAME.toString(),
        27
    )