package me.sd_master92.customvoting.extensions

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.database.PlayerRow
import me.sd_master92.customvoting.database.PlayerTable
import me.sd_master92.plugin.CustomPlugin
import org.bukkit.entity.Player

class CustomPlaceholders(private val plugin: CV) : PlaceholderExpansion()
{
    override fun getIdentifier(): String
    {
        return IDENTIFIER
    }

    override fun getAuthor(): String
    {
        return CustomPlugin.AUTHOR
    }

    override fun getVersion(): String
    {
        return CustomPlugin.VERSION
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
                SERVER_VOTES     ->
                {
                    var total = 0
                    for (voter in if (plugin.hasDatabaseConnection()) PlayerTable.getTopVoters(plugin) else VoteFile.getTopVoters(
                        plugin
                    ))
                    {
                        total += voter.votes
                    }
                    return "$total"
                }
                PLAYER_VOTES     ->
                {
                    if (player != null)
                    {
                        val voter =
                            if (plugin.hasDatabaseConnection()) PlayerRow(plugin, player) else VoteFile(player, plugin)
                        return "" + voter.votes
                    }
                    return "0"
                }
                PLAYER_PERIOD    ->
                {
                    if (player != null)
                    {
                        val voter =
                            if (plugin.hasDatabaseConnection()) PlayerRow(plugin, player) else VoteFile(player, plugin)
                        return "" + voter.period
                    }
                    return "0"
                }
                VOTE_PARTY_TOTAL ->
                {
                    val total = plugin.config.getInt(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY)
                    return "$total"
                }
                VOTE_PARTY_UNTIL ->
                {
                    val total = plugin.config.getInt(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY)
                    val current = plugin.data.getInt(Data.CURRENT_VOTES)
                    val until = total - current
                    return "$until"
                }
                else             ->
                {
                    if (params.contains(PLAYER_VOTES))
                    {
                        val key = params.split("_".toRegex()).toTypedArray()[2].toInt()
                        val topVoter = if (plugin.hasDatabaseConnection()) PlayerTable.getTopVoter(
                            plugin, key
                        ) else VoteFile.getTopVoter(plugin, key)
                        return if (params.endsWith("NAME"))
                        {
                            topVoter?.name ?: "Unknown"
                        } else
                        {
                            if (topVoter == null) "0" else "" + topVoter.votes
                        }
                    } else if (params.contains(PLAYER_PERIOD))
                    {
                        val key = params.split("_".toRegex()).toTypedArray()[2].toInt()
                        val topVoter = if (plugin.hasDatabaseConnection()) PlayerTable.getTopVoter(
                            plugin, key
                        ) else VoteFile.getTopVoter(plugin, key)
                        return if (params.endsWith("NAME"))
                        {
                            topVoter?.name ?: "Unknown"
                        } else
                        {
                            if (topVoter == null) "0" else "" + topVoter.period
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
        const val VOTE_PARTY_TOTAL = "VOTE_PARTY_TOTAL"
        const val VOTE_PARTY_UNTIL = "VOTE_PARTY_UNTIL"
    }
}