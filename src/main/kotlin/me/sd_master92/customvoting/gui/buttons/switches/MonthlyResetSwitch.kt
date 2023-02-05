package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class MonthlyResetSwitch(private val plugin: CV) : StatusItem(
    Material.CLOCK, PMessage.RESET_VOTES_ITEM_NAME_MONTHLY.toString(),
    plugin.config, Setting.MONTHLY_RESET.path
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config[Setting.MONTHLY_RESET.path] = !plugin.config.getBoolean(Setting.MONTHLY_RESET.path)
        plugin.config.saveConfig()
        event.currentItem = MonthlyResetSwitch(plugin)
    }
}