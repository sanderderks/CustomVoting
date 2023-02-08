package me.sd_master92.customvoting.gui.buttons.abstracts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import org.bukkit.Material

abstract class AbstractRewardCommandsButton(plugin: CV, val path: String, mat: Material, name: String? = null) :
    BaseItem(
        mat,
        name ?: PMessage.COMMAND_REWARDS_ITEM_NAME.toString(),
        PMessage.GENERAL_ITEM_LORE_CURRENT_XY.with(
            "" + plugin.data.getStringList(path).size,
            PMessage.COMMAND_REWARDS_UNIT_MULTIPLE.toString()
        )
    )