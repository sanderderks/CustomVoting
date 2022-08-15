package me.sd_master92.customvoting.extensions

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.database.PlayerTable
import org.bukkit.entity.Player

class CustomPlaceholders(private val plugin: CV) : PlaceholderExpansion()
{
    override fun getIdentifier(): String
    {
        return IDENTIFIER
    }

    override fun getAuthor(): String
    {
        return plugin.author
    }

    override fun getVersion(): String
    {
        return plugin.version
    }

    override fun canRegister(): Boolean
    {
        return true
    }

    override fun persist(): Boolean
    {
        return true
    }

    override fun onPlaceholderRequest(player: Player?, params: String): String?
    {
        try
        {
            when (params)
            {
                SERVER_VOTES ->
                {
                    var total = 0
                    for (voter in Voter.getTopVoters(plugin))
                    {
                        total += voter.votes
                    }
                    return "$total"
                }

                PLAYER_VOTES ->
                {
                    if (player != null)
                    {
                        val voter =
                            if (plugin.hasDatabaseConnection()) PlayerTable(plugin, player) else VoteFile(
                                player,
                                plugin
                            )
                        return "${voter.votes}"
                    }
                    return "0"
                }

                PLAYER_PERIOD, PLAYER_MONTHLY_VOTES ->
                {
                    if (player != null)
                    {
                        val voter =
                            if (plugin.hasDatabaseConnection()) PlayerTable(plugin, player) else VoteFile(
                                player,
                                plugin
                            )
                        return "${voter.monthlyVotes}"
                    }
                    return "0"
                }

                VOTE_PARTY_TOTAL ->
                {
                    val total = plugin.config.getInt(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY.path)
                    return "$total"
                }

                VOTE_PARTY_CURRENT ->
                {
                    val current = plugin.data.getInt(Data.CURRENT_VOTES)
                    return "$current"
                }

                VOTE_PARTY_UNTIL ->
                {
                    val total = plugin.config.getInt(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY.path)
                    val current = plugin.data.getInt(Data.CURRENT_VOTES)
                    val until = total - current
                    return "$until"
                }

                else ->
                {
                    if (params.contains(PLAYER_VOTES))
                    {
                        val top = params.split("_").toTypedArray()[2].toInt()
                        val topVoter = Voter.getTopVoter(plugin, top)
                        return if (params.endsWith("NAME"))
                        {
                            topVoter?.name ?: "Unknown"
                        } else
                        {
                            if (topVoter == null) "0" else "${topVoter.votes}"
                        }
                    } else if (params.contains(PLAYER_PERIOD) || params.contains(PLAYER_MONTHLY_VOTES))
                    {
                        val key = params.split("_").toTypedArray()[2].toInt()
                        val topVoter = Voter.getTopVoter(plugin, key)
                        return if (params.endsWith("NAME"))
                        {
                            topVoter?.name ?: "Unknown"
                        } else
                        {
                            if (topVoter == null) "0" else "${topVoter.monthlyVotes}"
                        }
                    }
                }
            }
        } catch (e: Exception)
        {
            return null
        }
        return null
    }

    companion object
    {
        const val IDENTIFIER = "CV"
        const val SERVER_VOTES = "SERVER_VOTES"
        const val PLAYER_VOTES = "PLAYER_VOTES"
        const val PLAYER_PERIOD = "PLAYER_PERIOD"
        const val PLAYER_MONTHLY_VOTES = "MONTHLY_VOTES"
        const val VOTE_PARTY_TOTAL = "VOTE_PARTY_TOTAL"
        const val VOTE_PARTY_CURRENT = "VOTE_PARTY_CURRENT"
        const val VOTE_PARTY_UNTIL = "VOTE_PARTY_UNTIL"
    }
}