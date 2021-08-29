package me.sd_master92.customvoting.listeners

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.subjects.VoteTopSign
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent

class VoteTopListener(private val plugin: Main) : Listener
{
    @EventHandler
    fun onSignChange(event: SignChangeEvent)
    {
        if (event.block.state is Sign)
        {
            if (event.player.hasPermission("customvoting.votetop.signs"))
            {
                val line0 = event.getLine(0)
                var line1 = event.getLine(1)
                if (line0 != null && line0.equals("[votes]", ignoreCase = true))
                {
                    event.isCancelled = true
                    val loc = event.block.location
                    val player = event.player
                    if (line1 != null && line1.lowercase().contains("top"))
                    {
                        try
                        {
                            line1 = line1.trim { it <= ' ' }.replace("top", "")
                            val top = line1.toInt()
                            VoteTopSign(plugin, top, loc, player)
                        } catch (ignored: Exception)
                        {
                        }
                    } else
                    {
                        VoteTopSign(plugin, 0, loc, player)
                    }
                }
            }
        }
    }
}