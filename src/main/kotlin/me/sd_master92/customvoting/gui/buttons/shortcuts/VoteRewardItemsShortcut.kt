package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.appendWhenTrue
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.buttons.abstracts.ItemRewardsAbstractButton
import me.sd_master92.customvoting.gui.pages.editors.ItemRewardsEditor
import org.bukkit.entity.Player

class VoteRewardItemsShortcut(private val plugin: CV, gui: GUI, private val op: Boolean) : ItemRewardsAbstractButton(
    plugin,
    gui,
    Data.ITEM_REWARDS.path.appendWhenTrue(op, Data.OP_REWARDS),
    PMessage.ITEM_REWARDS_ITEM_NAME.toString()
)
{
    override fun onOpen(player: Player)
    {
        ItemRewardsEditor(plugin, op).open(player)
    }
}