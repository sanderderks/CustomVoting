package me.sd_master92.customvoting.gui.items

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Strings
import org.bukkit.Material

class CommandsRewardItem(plugin: CV, path: String, mat: Material, name: String? = null) : BaseItem(
    mat,
    name ?: Strings.COMMAND_REWARDS_ITEM_NAME.toString(),
    Strings.GENERAL_ITEM_LORE_CURRENT_XY.with("" + plugin.data.getStringList(path).size, "commands")
)