package me.sd_master92.customvoting.listeners

import com.vexsoftware.votifier.model.VotifierEvent
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.models.VoteSiteUUID
import me.sd_master92.customvoting.subjects.CustomVote
import me.sd_master92.customvoting.subjects.VoteSite
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class VotifierListener(private val plugin: CV) : Listener
{
    @EventHandler(priority = EventPriority.NORMAL)
    fun onVotifierEvent(event: VotifierEvent)
    {
        val vote = event.vote
        CustomVote(plugin, vote)

        VoteSite.register(plugin, VoteSiteUUID(vote.serviceName))
    }
}