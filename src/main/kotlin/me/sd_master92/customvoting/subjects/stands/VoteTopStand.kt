package me.sd_master92.customvoting.subjects.stands

import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.getVotesPlaceholders
import org.bukkit.entity.Player


class VoteTopStand private constructor(private val plugin: CV, private val top: Int)
{
    private val path = Data.VOTE_TOP_STANDS.path + ".$top"
    var topStand = DisplayStand(plugin, "$path.top", top)
    var nameStand = DisplayStand(plugin, "$path.name", top)
    var votesStand = DisplayStand(plugin, "$path.votes", top, true)
    private var citizen = if (CV.CITIZENS) Citizen(plugin, "$path.citizen", top, votesStand.location) else null

    fun exists(): Boolean
    {
        return plugin.data.contains(path)
    }

    fun create(player: Player)
    {
        if (!exists())
        {
            topStand.create(player.location.add(0.0, 1.0, 0.0))
            nameStand.create(player.location.add(0.0, 0.5, 0.0))
            votesStand.create(player.location)
            citizen?.create(player.location)
            player.sendMessage(PMessage.VOTE_TOP_MESSAGE_STAND_CREATED_X.with("$top"))
            updateAll(plugin)
        } else
        {
            player.sendMessage(PMessage.GENERAL_ERROR_ALREADY_EXIST_X.with(PMessage.VOTE_TOP_UNIT_STAND.toString()))
        }
    }

    private fun update()
    {
        val topVoter = Voter.getTopVoter(plugin, top)
        val placeholders = topVoter?.getVotesPlaceholders(plugin) ?: mutableMapOf()
        placeholders["%TOP%"] = "$top"
        if (topVoter == null)
        {
            placeholders["%PLAYER%"] = PMessage.PLAYER_NAME_UNKNOWN_COLORED.toString()
            placeholders["%VOTES%"] = "0"
            placeholders["%VOTES_MONTHLY%"] = "0"
            placeholders["%VOTES_WEEKLY%"] = "0"
            placeholders["%VOTES_DAILY%"] = "0"
            placeholders["%VOTES_AUTO%"] = "0"
        }
        topStand.update(Message.VOTE_TOP_STANDS_TOP.getMessage(plugin, placeholders))
        nameStand.update(Message.VOTE_TOP_STANDS_CENTER.getMessage(plugin, placeholders))
        votesStand.update(Message.VOTE_TOP_STANDS_BOTTOM.getMessage(plugin, placeholders), topVoter?.uuid)
        citizen?.update(topVoter?.name ?: PMessage.PLAYER_NAME_UNKNOWN_COLORED.toString())
    }

    fun delete(player: Player)
    {
        topStand.delete()
        nameStand.delete()
        votesStand.delete()
        citizen?.delete()
        plugin.data.delete(path)
        voteTops.remove(top)
        player.sendMessage(PMessage.VOTE_TOP_MESSAGE_STAND_DELETED_X.with("$top"))
    }

    companion object
    {
        val voteTops: MutableMap<Int, VoteTopStand> = HashMap()

        operator fun get(plugin: CV, top: Int): VoteTopStand
        {
            return voteTops[top] ?: VoteTopStand(plugin, top)
        }

        fun updateAll(plugin: CV)
        {
            if (voteTops.isEmpty())
            {
                initialize(plugin)
            }
            TaskTimer.delay(plugin, 40)
            {
                for (voteTop in voteTops.values)
                {
                    voteTop.update()
                }
            }.run()
        }

        private fun initialize(plugin: CV)
        {
            val section = plugin.data.getConfigurationSection(Data.VOTE_TOP_STANDS.path)
            if (section != null)
            {
                for (n in section.getKeys(false))
                {
                    try
                    {
                        val top = n.toInt()
                        VoteTopStand(plugin, top)
                    } catch (ignored: Exception)
                    {
                    }
                }
            }
        }
    }

    init
    {
        voteTops[top] = this
    }
}