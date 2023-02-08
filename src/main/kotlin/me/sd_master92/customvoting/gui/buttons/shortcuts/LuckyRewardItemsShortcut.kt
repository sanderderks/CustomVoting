package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractRewardItemsButton
import me.sd_master92.customvoting.gui.pages.editors.LuckyRewardItemsEditor
import org.bukkit.Material
import org.bukkit.entity.Player

class LuckyRewardItemsShortcut(
    private val plugin: CV,
    private val currentPage: GUI
) : AbstractRewardItemsButton(
    plugin, currentPage, Data.LUCKY_REWARDS.path, PMessage.LUCKY_ITEM_NAME_REWARDS.toString(), Material.ENDER_CHEST
)
{
    override fun onOpen(player: Player)
    {
        LuckyRewardItemsEditor(plugin, currentPage).open(player)
    }

    init
    {
        addLore(";" + PMessage.LUCKY_ITEM_LORE)
    }
}