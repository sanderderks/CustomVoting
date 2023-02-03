package me.sd_master92.customvoting.gui.items

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Strings
import org.bukkit.Material

class ItemsRewardItem(plugin: CV, path: String, name: String? = null) : BaseItem(
    Material.CHEST, name ?: Strings.GUI_ITEM_REWARDS.toString(),
    Strings.GUI_CURRENT_XY.with("" + plugin.data.getItems(path).size, "item stacks")
)