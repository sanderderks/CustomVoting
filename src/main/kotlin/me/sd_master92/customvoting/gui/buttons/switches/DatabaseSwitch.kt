package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractStatusSwitch
import org.bukkit.Material

class DatabaseSwitch(plugin: CV) :
    AbstractStatusSwitch(plugin, Material.ENCHANTING_TABLE, Setting.USE_DATABASE, PMessage.DATABASE_ITEM_NAME)
{
    override fun newInstance(plugin: CV): DatabaseSwitch
    {
        return DatabaseSwitch(plugin)
    }

    init
    {
        if (value)
        {
            val isConnected =
                if (plugin.hasDatabaseConnection())
                {
                    PMessage.DATABASE_VALUE_CONNECTED.toString()
                } else
                {
                    PMessage.DATABASE_VALUE_NOT_FUNCTIONING.toString()
                }
            addLore(isConnected)
        }
        addLore(";" + PMessage.DATABASE_ITEM_LORE)
    }
}