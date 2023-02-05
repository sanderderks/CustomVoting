package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class LuckyVoteEnabledSwitch(private val plugin: CV) : StatusItem(
    Material.TOTEM_OF_UNDYING, PMessage.LUCKY_VOTE_ITEM_NAME.toString(),
    plugin.config, Setting.LUCKY_VOTE.path
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config[Setting.LUCKY_VOTE.path] = !plugin.config.getBoolean(Setting.LUCKY_VOTE.path)
        plugin.config.saveConfig()
        event.currentItem = LuckyVoteEnabledSwitch(plugin)
    }
}