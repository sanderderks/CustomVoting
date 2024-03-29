package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.getSkull
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class SupporterOverviewPage(private val plugin: CV, backPage: GUI?) :
    GUI(plugin, backPage, PMessage.SUPPORTER_INVENTORY_NAME.toString())
{
    override fun newInstance(): GUI
    {
        return SupporterOverviewPage(plugin, backPage)
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
        for (supporter in listOf("sd_master92", "Dutchbeard", "Smirren", "King_Tom_94"))
        {
            @Suppress("DEPRECATION")
            addItem(Bukkit.getOfflinePlayer(supporter).getSkull())
        }
    }
}