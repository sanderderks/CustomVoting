package me.sd_master92.customvoting.listeners

import com.vexsoftware.votifier.model.VotifierEvent
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.subjects.CustomVote
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

        val voteSites = plugin.data.getStringList(Data.VOTE_SITES.path)
        if (!voteSites.contains(vote.serviceName))
        {
            voteSites.add(vote.serviceName)
        }
        plugin.data.set(Data.VOTE_SITES.path, voteSites)
        plugin.data.saveConfig()
    }
}