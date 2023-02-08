package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.carousel.LuckyChanceCarousel
import me.sd_master92.customvoting.gui.buttons.carousel.VoteRewardItemsTypeCarousel
import me.sd_master92.customvoting.gui.buttons.inputfields.VoteRewardCommandsInput
import me.sd_master92.customvoting.gui.buttons.inputfields.VoteRewardExperienceInput
import me.sd_master92.customvoting.gui.buttons.inputfields.VoteRewardMoneyInput
import me.sd_master92.customvoting.gui.buttons.shortcuts.*
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class RewardSettingsPage(private val plugin: CV, backPage: GUI?, power: Boolean = false) :
    GUI(
        plugin,
        backPage,
        if (!power) PMessage.VOTE_REWARDS_INVENTORY_NAME.toString() else PMessage.POWER_REWARDS_INVENTORY_NAME.toString(),
        if (power) 9 else 18
    )
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
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
        addItem(VoteRewardItemsShortcut(plugin, this, power))
        addItem(VoteRewardItemsTypeCarousel(plugin, power))
        addItem(VoteRewardMoneyInput(plugin, this, power))
        addItem(VoteRewardExperienceInput(plugin, power))
        addItem(VoteRewardCommandsInput(plugin, this, power))
        if (!power)
        {
            addItem(LuckyRewardItemsShortcut(plugin, this))
            addItem(LuckyChanceCarousel(plugin))
            addItem(MilestoneOverviewShortcut(plugin, this))
            addItem(PermissionBasedRewardSettingsShortcut(plugin, this))
            addItem(VotePartyRewardCommandsShortcut(plugin, this))
        } else
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