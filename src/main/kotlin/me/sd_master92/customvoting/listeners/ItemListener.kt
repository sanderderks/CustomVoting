package me.sd_master92.customvoting.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ItemMergeEvent
import java.util.*

class ItemListener : Listener
{

    @EventHandler
    fun onItemMerge(event: ItemMergeEvent)
    {
        if (event.entity.uniqueId in CANCEL_EVENT)
        {
            event.isCancelled = true
        }
    }

    companion object
    {
        val CANCEL_EVENT = mutableListOf<UUID>()
    }
}