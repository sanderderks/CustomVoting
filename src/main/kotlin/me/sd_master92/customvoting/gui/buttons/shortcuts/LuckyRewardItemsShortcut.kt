package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.buttons.abstracts.ItemRewardsAbstractButton
import me.sd_master92.customvoting.gui.pages.editors.LuckyItemRewardsEditor
import org.bukkit.Material
import org.bukkit.entity.Player

class LuckyRewardItemsShortcut(private val plugin: CV, gui: GUI) : ItemRewardsAbstractButton(
    plugin, gui, Data.LUCKY_REWARDS.path, PMessage.LUCKY_VOTE_ITEM_NAME_REWARDS.toString(), Material.ENDER_CHEST
)
{
    override fun onOpen(player: Player)
    {
        LuckyItemRewardsEditor(plugin).open(player)
    }
}