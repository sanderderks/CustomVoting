package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.plugin.CustomPlugin
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class PluginUpdateAction(private val plugin: CV, private val currentPage: GUI) : BaseItem(
    Material.CLOCK, PMessage.PLUGIN_VERSION_ITEM_NAME.toString()
)
{
    private val version = plugin.versionStatus()

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        if (!version.isValid())
        {
            SoundType.CLICK.play(plugin, player)
            currentPage.cancelCloseEvent = true
            player.closeInventory()
            plugin.sendDownloadUrl(player)
        }
    }

    init
    {
        val lore = when (version)
        {
            CustomPlugin.VersionStatus.LATEST   ->
                PMessage.GENERAL_VALUE_YES.toString() + ";" + PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(PMessage.GREEN.getColor() + plugin.version)

            CustomPlugin.VersionStatus.BETA     ->
                PMessage.GENERAL_VALUE_YES.toString() + ";" + PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(PMessage.GREEN.getColor() + plugin.version + PMessage.PLUGIN_VERSION_ITEM_LORE_BETA)

            CustomPlugin.VersionStatus.OUTDATED ->
                PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(PMessage.RED.getColor() + plugin.version) + ";" + PMessage.PLUGIN_VERSION_ITEM_LORE_LATEST_X.with(
                    plugin.latestVersion
                )
        }
        setLore(lore)
    }
}