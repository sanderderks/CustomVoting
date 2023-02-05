package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.settings.RewardSettingsPage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VoteRewardSettingsShortcut(private val plugin: CV, private val gui: GUI) : BaseItem(
    Material.DIAMOND, PMessage.SETTINGS_ITEM_NAME_REWARDS.toString(),
    null, true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        gui.cancelCloseEvent = true
        RewardSettingsPage(plugin).open(player)
    }
}