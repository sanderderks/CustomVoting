package me.sd_master92.customvoting.extensions

import kotlinx.coroutines.runBlocking
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
        return runBlocking {
            try
            {
                when (params)
                {
                    SERVER_VOTES               ->
                    {
                        var total = 0
                        for (voter in Voter.getTopVoters(plugin))
                        {
                            total += voter.getVotes()
                        }
                        return@runBlocking "$total"
                    }

                    PLAYER_VOTES               ->
                    {
                        if (player != null)
                        {
                            return@runBlocking "" + Voter.get(plugin, player).getVotes()
                        }
                        return@runBlocking "0"
                    }

                    PLAYER_VOTES + SUF_MONTHLY ->
                    {
                        if (player != null)
                        {
                            return@runBlocking "" + Voter.get(plugin, player).getVotesMonthly()
                        }
                        return@runBlocking "0"
                    }

                    PLAYER_VOTES + SUF_WEEKLY  ->
                    {
                        if (player != null)
                        {
                            return@runBlocking "" + Voter.get(plugin, player).getVotesWeekly()
                        }
                        return@runBlocking "0"
                    }

                    PLAYER_VOTES + SUF_DAILY   ->
                    {
                        if (player != null)
                        {
                            return@runBlocking "" + Voter.get(plugin, player).getVotesDaily()
                        }
                        return@runBlocking "0"
                    }

                    PLAYER_STREAK + SUF_DAILY  ->
                    {
                        if (player != null)
                        {
                            return@runBlocking "" + Voter.get(plugin, player).getStreakDaily()
                        }
                        return@runBlocking "0"
                    }

                    VOTE_PARTY_TOTAL           ->
                    {
                        val total = plugin.config.getInt(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path)
                        return@runBlocking "$total"
                    }

                    VOTE_PARTY_CURRENT         ->
                    {
                        val current = plugin.data.getInt(Data.CURRENT_VOTES.path)
                        return@runBlocking "$current"
                    }

                    VOTE_PARTY_UNTIL           ->
                    {
                        val total = plugin.config.getInt(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path)
                        val current = plugin.data.getInt(Data.CURRENT_VOTES.path)
                        val until = total - current
                        return@runBlocking "$until"
                    }

                    else                       ->
                    {
                        when (true)
                        {
                            (params.contains(PLAYER_VOTES) && params.contains(SUF_NAME))    ->
                            {
                                return@runBlocking getTopVoter(params)?.getName() ?: PMessage.PLAYER_NAME_UNKNOWN.toString()
                            }

                            (params.contains(PLAYER_VOTES) && params.contains(SUF_MONTHLY)) ->
                            {
                                return@runBlocking "" + (getTopVoter(params)?.getVotesMonthly() ?: 0)
                            }

                            (params.contains(PLAYER_VOTES) && params.contains(SUF_WEEKLY))  ->
                            {
                                return@runBlocking "" + (getTopVoter(params)?.getVotesWeekly() ?: 0)
                            }

                            (params.contains(PLAYER_VOTES) && params.contains(SUF_DAILY))   ->
                            {
                                return@runBlocking "" + (getTopVoter(params)?.getVotesDaily() ?: 0)
                            }

                            params.contains(PLAYER_VOTES)                                   ->
                            {
                                return@runBlocking "" + (getTopVoter(params)?.getVotes() ?: 0)
                            }

                            else                                                            ->
                            {
                                return@runBlocking null
                            }
                        }
                    }
                }
            } catch (e: Exception)
            {
                return@runBlocking null
            }
        }
    }

    private suspend fun getTopVoter(params: String): Voter?
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