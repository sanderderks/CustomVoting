package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.buttons.abstracts.ItemRewardsAbstractButton
import me.sd_master92.customvoting.gui.pages.editors.MilestoneItemRewardsEditor
import org.bukkit.entity.Player

class MilestoneRewardItemsShortcut(private val plugin: CV, gui: GUI, private val number: Int) :
    ItemRewardsAbstractButton(
        plugin,
        gui,
        "${Data.MILESTONES}.$number.${Data.ITEM_REWARDS}",
        PMessage.ITEM_REWARDS_ITEM_NAME.toString()
    )
{
    override fun onOpen(player: Player)
    {
        MilestoneItemRewardsEditor(plugin, number).open(player)
    }
}