package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.carousel.LuckyVoteChanceCarousel
import me.sd_master92.customvoting.gui.buttons.carousel.VoteRewardItemsTypeCarousel
import me.sd_master92.customvoting.gui.buttons.editors.VoteRewardCommandsEditor
import me.sd_master92.customvoting.gui.buttons.editors.VoteRewardExperienceEditor
import me.sd_master92.customvoting.gui.buttons.editors.VoteRewardMoneyEditor
import me.sd_master92.customvoting.gui.buttons.shortcuts.*
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class RewardSettingsPage(private val plugin: CV, private val op: Boolean = false) :
    GUI(
        plugin,
        if (!op) PMessage.VOTE_REWARDS_INVENTORY_NAME.toString() else PMessage.PERMISSION_BASED_REWARDS_INVENTORY_NAME.toString(),
        if (op) 9 else 18
    )
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        if (op)
        {
            RewardSettingsPage(plugin).open(player)
        } else
        {
            VoteSettingsPage(plugin).open(player)
        }
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    init
    {
        addItem(VoteRewardItemsShortcut(plugin, this, op))
        addItem(VoteRewardItemsTypeCarousel(plugin, op))
        addItem(VoteRewardMoneyEditor(plugin, this, op))
        addItem(VoteRewardExperienceEditor(plugin, op))
        addItem(VoteRewardCommandsEditor(plugin, this, op))
        if (!op)
        {
            addItem(LuckyRewardItemsShortcut(plugin, this))
            addItem(LuckyVoteChanceCarousel(plugin))
            addItem(MilestoneOverviewShortcut(plugin, this))
            addItem(PermissionBasedRewardSettingsShortcut(plugin, this))
            addItem(VotePartyRewardCommandsShortcut(plugin, this))
        }
        if (op)
        {
            if (CV.PERMISSION != null)
            {
                addItem(PermGroupOverviewShortcut(plugin, this))
            }
            addItem(PermUserOverviewShortcut(plugin, this))
        }
        addItem(CrateOverviewShortcut(plugin, this))
    }
}