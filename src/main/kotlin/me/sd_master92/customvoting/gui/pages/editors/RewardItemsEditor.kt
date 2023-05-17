package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.core.appendWhenTrue
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.buttons.actions.PaginationNextAction
import me.sd_master92.customvoting.gui.buttons.actions.PaginationPreviousAction
import me.sd_master92.customvoting.gui.pages.abstracts.AbstractItemEditor
import org.bukkit.entity.Player

class RewardItemsEditor(
    private val plugin: CV,
    backPage: GUI?,
    private val power: Boolean = false,
    page: Int = 0
) :
    AbstractItemEditor(
        plugin,
        backPage,
        Data.ITEM_REWARDS.path.appendWhenTrue(power, Data.POWER_REWARDS),
        PMessage.ITEM_REWARDS_INVENTORY_NAME.toString(),
        27,
        page = page
    )
{
    override fun newInstance(): GUI
    {
        return RewardItemsEditor(plugin, backPage, power, page!!)
    }

    init
    {
        setItem(
            nonClickableSizeWithNull - 1,
            object : PaginationNextAction(plugin, this, page)
            {
                override fun onNext(player: Player, newPage: Int)
                {
                    save(player, notifyNoChanges = false)
                    RewardItemsEditor(plugin, backPage, power, newPage).open(player)
                }
            })
        setItem(nonClickableSizeWithNull - 1, object : PaginationPreviousAction(plugin, this, page)
        {
            override fun onPrevious(player: Player, newPage: Int)
            {
                save(player, notifyNoChanges = false)
                RewardItemsEditor(plugin, backPage, power, newPage).open(player)
            }
        })
    }
}