package me.sd_master92.customvoting.gui.buttons.descriptions

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.items.SimpleItem
import org.bukkit.Material

class DatabaseDescription(plugin: CV) :
    SimpleItem(Material.ENCHANTING_TABLE, PMessage.SUPPORT_ITEM_NAME_DATABASE.toString())
{
    init
    {
        val isConnected =
            if (plugin.hasDatabaseConnection()) PMessage.GENERAL_VALUE_CONNECTED.toString() else PMessage.GENERAL_VALUE_DISABLED.toString()
        setLore(PMessage.GENERAL_ITEM_LORE_STATUS_X.with(isConnected))
    }
}