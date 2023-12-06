package me.sd_master92.customvoting.extensions

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.models.VoterData
import me.sd_master92.customvoting.tasks.PlaceholderChecker
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
                        return "${PlaceholderChecker.SERVER_VOTES}"
                    }

                    PLAYER_VOTES               ->
                    {
                        if (player != null)
                        {
                            return "${PlaceholderChecker.VOTERS[player.uniqueId]?.votes ?: 0}"
                        }
                        return "0"
                    }

                    PLAYER_VOTES + SUF_MONTHLY ->
                    {
                        if (player != null)
                        {
                            return "${PlaceholderChecker.VOTERS[player.uniqueId]?.votesMonthly ?: 0}"
                        }
                        return "0"
                    }

                    PLAYER_VOTES + SUF_WEEKLY  ->
                    {
                        if (player != null)
                        {
                            return "${PlaceholderChecker.VOTERS[player.uniqueId]?.votesWeekly ?: 0}"
                        }
                        return "0"
                    }

                    PLAYER_VOTES + SUF_DAILY   ->
                    {
                        if (player != null)
                        {
                            return "${PlaceholderChecker.VOTERS[player.uniqueId]?.votesDaily ?: 0}"
                        }
                        return "0"
                    }

                    PLAYER_STREAK + SUF_DAILY  ->
                    {
                        if (player != null)
                        {
                            return "${PlaceholderChecker.VOTERS[player.uniqueId]?.streakDaily ?: 0}"
                        }
                        return "0"
                    }

                    VOTE_PARTY_TOTAL           ->
                    {
                        return "${PlaceholderChecker.VOTE_PARTY_TOTAL}"
                    }

                    VOTE_PARTY_CURRENT         ->
                    {
                        return "${PlaceholderChecker.VOTE_PARTY_CURRENT}"
                    }

                    VOTE_PARTY_UNTIL           ->
                    {
                        return "${PlaceholderChecker.VOTE_PARTY_UNTIL}"
                    }

                    else                       ->
                    {
                        when (true)
                        {
                            (params.contains(PLAYER_VOTES) && params.contains(SUF_NAME))    ->
                            {
                                return "" + (getTopVoterData(params)?.name ?: PMessage.PLAYER_NAME_UNKNOWN)
                            }

                            (params.contains(PLAYER_VOTES) && params.contains(SUF_MONTHLY)) ->
                            {
                                return "" + (getTopVoterData(params)?.votesMonthly ?: 0)
                            }

                            (params.contains(PLAYER_VOTES) && params.contains(SUF_WEEKLY))  ->
                            {
                                return "" + (getTopVoterData(params)?.votesWeekly ?: 0)
                            }

                            (params.contains(PLAYER_VOTES) && params.contains(SUF_DAILY))   ->
                            {
                                return "" + (getTopVoterData(params)?.votesDaily ?: 0)
                            }

                            params.contains(PLAYER_VOTES)                                   ->
                            {
                                return "" + (getTopVoterData(params)?.votes ?: 0)
                            }

                            else                                                            ->
                            {
                                return null
                            }
                        }
                    }
                }
            } catch (e: Exception)
            {
                return null
            }
    }

    private fun getTopVoterData(params: String): VoterData?
    {
        val key = params.split("_").toTypedArray()[2].toInt()
        return PlaceholderChecker.VOTERS.values.withIndex().find { it.index == key }?.value
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