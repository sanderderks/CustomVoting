package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.pages.abstracts.AbstractItemEditor

class LuckyRewardItemsEditor(plugin: CV, backPage: GUI?) :
    AbstractItemEditor(
        plugin,
        backPage,
        Data.LUCKY_REWARDS.path,
        PMessage.LUCKY_INVENTORY_NAME_REWARDS.toString(),
        27
    )