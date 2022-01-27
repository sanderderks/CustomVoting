package me.sd_master92.customvoting.tasks

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.commands.ReloadCommand
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class DailyTask(private val plugin: CV)
{
    companion object
    {
        var FIRST_OF_MONTH = false
    }
    
    init
    {
        object : BukkitRunnable()
        {
            override fun run()
            {
                ReloadCommand.reload(plugin)
                if (Calendar.getInstance()[Calendar.DAY_OF_MONTH] == 1)
                {
                    FIRST_OF_MONTH = true
                }
            }
        }.runTaskTimer(plugin, 60, (20 * 60 * 60).toLong())
    }
}