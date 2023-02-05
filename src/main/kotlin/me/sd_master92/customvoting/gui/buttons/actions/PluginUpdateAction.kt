package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class PluginUpdateAction(private val plugin: CV, private val gui: GUI) : BaseItem(
    Material.CLOCK, PMessage.SUPPORT_ITEM_NAME_VERSION.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        if (!plugin.isUpToDate())
        {
            SoundType.CLICK.play(plugin, player)
            gui.cancelCloseEvent = true
            player.closeInventory()
            plugin.sendDownloadUrl(player)
        }
    }

    init
    {
        val lore = if (plugin.isUpToDate())
        {
            PMessage.GENERAL_VALUE_YES.toString() + ";" + PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(PMessage.GREEN.toString() + plugin.version)
        } else
        {
            PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(PMessage.RED.toString() + plugin.version) + ";" + PMessage.SUPPORT_ITEM_LORE_VERSION_LATEST_X.with(
                plugin.latestVersion
            )
        }
        setLore(lore)
    }
}