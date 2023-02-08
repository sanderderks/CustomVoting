package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractRewardItemsButton
import me.sd_master92.customvoting.gui.pages.editors.CrateRewardItemsEditor
import org.bukkit.entity.Player

class CrateRewardItemsShortcut(
    private val plugin: CV,
    private val backPage: GUI,
    private val number: Int,
    chance: Int
) : AbstractRewardItemsButton(
    plugin,
    backPage,
    "${Data.VOTE_CRATES}.$number.${Data.ITEM_REWARDS}.$chance",
    PMessage.CRATE_ITEM_NAME_REWARDS_PERCENTAGE_X.with("$chance")
)
{
    override fun onOpen(player: Player)
    {
        try
        {
            val chance = itemMeta?.displayName?.filter { it.isDigit() }?.toInt()
            CrateRewardItemsEditor(
                plugin,
                backPage,
                number,
                chance ?: Data.CRATE_REWARD_CHANCES[0]
            ).open(player)
        } catch (_: Exception)
        {
        }
    }
}