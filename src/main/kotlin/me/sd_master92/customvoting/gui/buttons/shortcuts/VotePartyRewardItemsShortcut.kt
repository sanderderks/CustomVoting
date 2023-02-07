package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.buttons.abstracts.ItemRewardsAbstractButton
import me.sd_master92.customvoting.gui.pages.editors.VotePartyItemRewardsEditor
import org.bukkit.Material
import org.bukkit.entity.Player

class VotePartyRewardItemsShortcut(
    private val plugin: CV,
    gui: GUI,
    private val key: Int
) : ItemRewardsAbstractButton(
    plugin,
    gui,
    Data.VOTE_PARTY_CHESTS.path + ".$key",
    PMessage.VOTE_PARTY_ITEM_NAME_CHEST_X.with("$key"),
    Material.ENDER_CHEST
)
{
    override fun onOpen(player: Player)
    {
        VotePartyItemRewardsEditor(plugin, "$key").open(player)
    }
}