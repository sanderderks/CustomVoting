package me.sd_master92.customvoting.listeners

import me.sd_master92.customvoting.CV
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ItemMergeEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ItemListener(private val plugin: CV) : Listener
{

    @EventHandler
    fun onItemMerge(event: ItemMergeEvent)
    {
        if (event.entity.uniqueId in CANCEL_EVENT)
        {
            event.isCancelled = true
            object : BukkitRunnable()
            {
                override fun run()
                {
                    CANCEL_EVENT.remove(event.entity.uniqueId)
                }
            }.runTaskLater(plugin, 20 * 5)
        }
    }

    companion object
    {
        val CANCEL_EVENT = mutableListOf<UUID>()
    }
}