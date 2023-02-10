package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractRewardItemsButton
import me.sd_master92.customvoting.gui.pages.editors.MilestoneRewardItemsEditor
import org.bukkit.entity.Player

class MilestoneRewardItemsShortcut(
    private val plugin: CV,
    private val currentPage: GUI,
    private val number: Int
) :
    AbstractRewardItemsButton(
        plugin,
        currentPage,
        "${Data.MILESTONES}.$number.${Data.ITEM_REWARDS}",
        PMessage.ITEM_REWARDS_ITEM_NAME.toString()
    )
{
    override fun onOpen(player: Player)
    {
        MilestoneRewardItemsEditor(plugin, currentPage, number).open(player)
    }
}