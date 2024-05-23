package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.settings.DoubleRewardSettingsPage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class DoubleRewardShortcut(private val plugin: CV, private val currentPage: GUI, private val setting: Setting) :
    BaseItem(
        Material.CLOCK,
        PMessage.DOUBLE_REWARDS_ITEM_NAME.toString(),
        PMessage.DOUBLE_REWARDS_ITEM_LORE.toString()
    )
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        currentPage.cancelCloseEvent = true
        DoubleRewardSettingsPage(plugin, currentPage, setting).open(player)
    }
}