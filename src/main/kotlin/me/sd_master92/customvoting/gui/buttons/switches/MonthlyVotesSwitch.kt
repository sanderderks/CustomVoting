package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class MonthlyVotesSwitch(private val plugin: CV) : StatusItem(
    Material.TNT, PMessage.MONTHLY_VOTES_ITEM_NAME.toString(),
    plugin.config, Setting.MONTHLY_VOTES.path
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config[Setting.MONTHLY_VOTES.path] = !plugin.config.getBoolean(Setting.MONTHLY_VOTES.path)
        plugin.config.saveConfig()
        event.currentItem = MonthlyVotesSwitch(plugin)
    }
}