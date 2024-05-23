package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.carousel.VoteRewardExperienceCarousel
import me.sd_master92.customvoting.gui.buttons.carousel.VoteRewardItemsTypeCarousel
import me.sd_master92.customvoting.gui.buttons.inputfields.VoteRewardCommandsInput
import me.sd_master92.customvoting.gui.buttons.inputfields.VoteRewardMoneyInput
import me.sd_master92.customvoting.gui.buttons.shortcuts.DoubleRewardShortcut
import me.sd_master92.customvoting.gui.buttons.shortcuts.PermGroupOverviewShortcut
import me.sd_master92.customvoting.gui.buttons.shortcuts.PermUserOverviewShortcut
import me.sd_master92.customvoting.gui.buttons.shortcuts.VoteRewardItemsShortcut
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class RewardSettingsPage(private val plugin: CV, backPage: GUI?, private val power: Boolean = false) :
    GUI(
        plugin,
        backPage,
        if (!power) PMessage.VOTE_REWARDS_INVENTORY_NAME.toString() else PMessage.POWER_REWARDS_INVENTORY_NAME.toString()
    )
{
    override fun newInstance(): GUI
    {
        return RewardSettingsPage(plugin, backPage, power)
    }

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
        addItem(
            DoubleRewardShortcut(
                plugin,
                this,
                if (power) Setting.DOUBLE_REWARDS_POWER else Setting.DOUBLE_REWARDS_REGULAR
            )
        )
        addItem(VoteRewardMoneyInput(plugin, this, power))
        addItem(VoteRewardExperienceCarousel(plugin, power))
        addItem(VoteRewardCommandsInput(plugin, this, power))
        if (power)
        {
            if (CV.PERMISSION != null)
            {
                setItem(6, PermGroupOverviewShortcut(plugin, this, CV.PERMISSION!!.name))
            }
            setItem(7, PermUserOverviewShortcut(plugin, this))
        }
    }
}