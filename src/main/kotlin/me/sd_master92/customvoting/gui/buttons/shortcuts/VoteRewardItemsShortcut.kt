package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.appendWhenTrue
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractRewardItemsButton
import me.sd_master92.customvoting.gui.pages.editors.RewardItemsEditor
import org.bukkit.entity.Player

class VoteRewardItemsShortcut(
    private val plugin: CV,
    private val currentPage: GUI,
    private val power: Boolean
) : AbstractRewardItemsButton(
    plugin,
    currentPage,
    Data.ITEM_REWARDS.path.appendWhenTrue(power, Data.POWER_REWARDS),
    PMessage.ITEM_REWARDS_ITEM_NAME.toString()
)
{
    override fun onOpen(player: Player)
    {
        RewardItemsEditor(plugin, currentPage, power).open(player)
    }
}