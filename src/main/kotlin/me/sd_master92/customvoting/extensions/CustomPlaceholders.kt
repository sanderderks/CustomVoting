package me.sd_master92.customvoting.extensions

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.database.PlayerRow
import me.sd_master92.customvoting.database.PlayerTable
import org.bukkit.entity.Player

class CustomPlaceholders(private val plugin: Main) : PlaceholderExpansion()
{
    override fun getIdentifier(): String
    {
        return IDENTIFIER
    }

    override fun getAuthor(): String
    {
        return plugin.AUTHOR
    }

    override fun getVersion(): String
    {
        return plugin.VERSION
    }

    override fun canRegister(): Boolean
    {
        return true
    }

    override fun persist(): Boolean
    {
        return true
    }

    override fun onPlaceholderRequest(player: Player, params: String): String?
    {
        try
        {
            if (params == SERVER_VOTES)
            {
                var total = 0
                for (voter in if (plugin.hasDatabaseConnection()) PlayerTable.getTopVoters(plugin) else VoteFile.getTopVoters(plugin))
                {
                    total += voter.votes
                }
                return "" + total
            } else if (params == PLAYER_VOTES)
            {
                val voter = if (plugin.hasDatabaseConnection()) PlayerRow(plugin, player) else VoteFile(player, plugin)
                return "" + voter.votes
            } else if (params.contains(PLAYER_VOTES))
            {
                val key = params.split("_".toRegex()).toTypedArray()[2].toInt()
                val topVoter = if (plugin.hasDatabaseConnection()) PlayerTable.getTopVoter(plugin, key
                ) else VoteFile.getTopVoter(plugin, key)
                return if (params.endsWith("NAME"))
                {
                    if (topVoter == null) "Unknown" else topVoter.userName!!
                } else
                {
                    if (topVoter == null) "0" else "" + topVoter.votes
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
    }
}