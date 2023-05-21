package me.sd_master92.customvoting.extensions

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.interfaces.Voter
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
                SERVER_VOTES               ->
                {
                    var total = 0
                    for (voter in Voter.getTopVoters(plugin))
                    {
                        total += voter.votes
                    }
                    return "$total"
                }

                PLAYER_VOTES               ->
                {
                    if (player != null)
                    {
                        return "" + Voter.get(plugin, player).votes
                    }
                    return "0"
                }

                PLAYER_VOTES + SUF_MONTHLY ->
                {
                    if (player != null)
                    {
                        return "" + Voter.get(plugin, player).votesMonthly
                    }
                    return "0"
                }

                PLAYER_VOTES + SUF_WEEKLY  ->
                {
                    if (player != null)
                    {
                        return "" + Voter.get(plugin, player).votesWeekly
                    }
                    return "0"
                }

                PLAYER_VOTES + SUF_DAILY   ->
                {
                    if (player != null)
                    {
                        return "" + Voter.get(plugin, player).votesDaily
                    }
                    return "0"
                }

                PLAYER_STREAK + SUF_DAILY  ->
                {
                    if (player != null)
                    {
                        return "" + Voter.get(plugin, player).streakDaily
                    }
                    return "0"
                }

                VOTE_PARTY_TOTAL           ->
                {
                    val total = plugin.config.getInt(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path)
                    return "$total"
                }

                VOTE_PARTY_CURRENT         ->
                {
                    val current = plugin.data.getInt(Data.CURRENT_VOTES.path)
                    return "$current"
                }

                VOTE_PARTY_UNTIL           ->
                {
                    val total = plugin.config.getInt(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path)
                    val current = plugin.data.getInt(Data.CURRENT_VOTES.path)
                    val until = total - current
                    return "$until"
                }

                else                       ->
                {
                    when (true)
                    {
                        (params.contains(PLAYER_VOTES) && params.contains(SUF_NAME))    ->
                        {
                            return getTopVoter(params)?.name ?: PMessage.PLAYER_NAME_UNKNOWN.toString()
                        }

                        (params.contains(PLAYER_VOTES) && params.contains(SUF_MONTHLY)) ->
                        {
                            return "" + (getTopVoter(params)?.votesMonthly ?: 0)
                        }

                        (params.contains(PLAYER_VOTES) && params.contains(SUF_WEEKLY))  ->
                        {
                            return "" + (getTopVoter(params)?.votesWeekly ?: 0)
                        }

                        (params.contains(PLAYER_VOTES) && params.contains(SUF_DAILY))   ->
                        {
                            return "" + (getTopVoter(params)?.votesDaily ?: 0)
                        }

                        params.contains(PLAYER_VOTES)                                   ->
                        {
                            return "" + (getTopVoter(params)?.votes ?: 0)
                        }

                        else                                                            ->
                        {
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

    private fun getTopVoter(params: String): Voter?
    {
        val key = params.split("_").toTypedArray()[2].toInt()
        return Voter.getTopVoter(plugin, key)
    }

    companion object
    {
        const val IDENTIFIER = "CV"
        const val SERVER_VOTES = "SERVER_VOTES"

        const val PLAYER_VOTES = "PLAYER_VOTES"
        const val PLAYER_STREAK = "PLAYER_STREAK"
        const val SUF_MONTHLY = "_MONTHLY"
        const val SUF_WEEKLY = "_WEEKLY"
        const val SUF_DAILY = "_DAILY"
        const val SUF_NAME = "_NAME"

        const val VOTE_PARTY_TOTAL = "VOTE_PARTY_TOTAL"
        const val VOTE_PARTY_CURRENT = "VOTE_PARTY_CURRENT"
        const val VOTE_PARTY_UNTIL = "VOTE_PARTY_UNTIL"
    }
}