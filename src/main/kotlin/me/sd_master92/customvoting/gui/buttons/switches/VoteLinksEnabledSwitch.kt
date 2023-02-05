package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VoteLinksEnabledSwitch(private val plugin: CV) : StatusItem(
    Material.CHEST, PMessage.VOTE_LINKS_ITEM_NAME_GUI.toString(),
    plugin.config, Setting.VOTE_LINK_INVENTORY.path
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(
            Setting.VOTE_LINK_INVENTORY.path,
            !plugin.config.getBoolean(Setting.VOTE_LINK_INVENTORY.path)
        )
        plugin.config.saveConfig()
        event.currentItem = VoteLinksEnabledSwitch(plugin)
    }
}