package me.sd_master92.customvoting.listeners

import com.vexsoftware.votifier.model.VotifierEvent
import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.subjects.CustomVote
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class VotifierListener(private val plugin: Main) : Listener
{
    @EventHandler(priority = EventPriority.NORMAL)
    fun onVotifierEvent(event: VotifierEvent)
    {
        CustomVote(plugin, event.vote)
    }
}