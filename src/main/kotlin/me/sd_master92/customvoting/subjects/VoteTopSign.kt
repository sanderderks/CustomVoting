package me.sd_master92.customvoting.subjects

import com.github.shynixn.mccoroutine.bukkit.launch
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.getVotesPlaceholders
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.Sign
import org.bukkit.block.Skull
import org.bukkit.block.data.type.WallSign
import org.bukkit.entity.Player
import java.util.*

class VoteTopSign(
    private val plugin: CV,
    private val top: Int,
    val location: Location?,
    player: Player? = null
)
{
    private suspend fun update()
    {
        if (top == 0)
        {
            updateTitle()
        } else
        {
            updateSign()
        }
    }

    private fun updateTitle()
    {
        if (location!!.block.state is Sign)
        {
            val sign = location.block.state as Sign
            val messages = Message.VOTE_TOP_SIGNS_TITLE_SIGN.getMessages(plugin)
            for (i in messages.indices)
            {
                sign.setLine(i, messages[i])
            }
            sign.update(true)
        }
    }

    private suspend fun updateSign()
    {
        if (location!!.block.state is Sign)
        {
            val sign = location.block.state as Sign
            val topVoter = Voter.getTopVoter(plugin, top)
            if (topVoter != null)
            {
                val oldLoc = plugin.data.getLocation(Data.VOTE_TOP_SIGNS.path + "." + top)
                if (oldLoc != null && oldLoc != location)
                {
                    if (oldLoc.block.state is Sign)
                    {
                        val oldSign = oldLoc.block.state as Sign
                        for (i in 0..3)
                        {
                            if (i == 1)
                            {
                                oldSign.setLine(
                                    i,
                                    Message.VOTE_TOP_SIGNS_PLAYER_SIGNS_OUTDATED.getMessage(plugin)
                                )
                            } else
                            {
                                oldSign.setLine(i, "")
                            }
                        }
                        oldSign.update(true)
                    }
                }
                plugin.data.setLocation(Data.VOTE_TOP_SIGNS.path + "." + top, location)
                val placeholders = topVoter.getVotesPlaceholders(plugin)
                placeholders["%NUMBER%"] = "$top"
                for ((i, message) in Message.VOTE_TOP_SIGNS_PLAYER_SIGNS_FORMAT.getMessages(plugin, placeholders)
                    .withIndex())
                {
                    sign.setLine(i, message)
                }
                updateSkull(location, topVoter.getUuid())
            } else
            {
                for (i in 0..3)
                {
                    if (i == 1)
                    {
                        sign.setLine(
                            i,
                            Message.VOTE_TOP_SIGNS_PLAYER_SIGNS_NOT_FOUND.getMessage(plugin)
                        )
                    } else
                    {
                        sign.setLine(i, "")
                    }
                }
            }
            sign.update(true)
        }
    }

    private fun updateSkull(loc: Location, uuid: UUID)
    {
        val sign = loc.block.blockData
        if (sign is WallSign)
        {
            val block1 = loc.block.getRelative(BlockFace.UP)
            val block2 = loc.block.getRelative(sign.facing.oppositeFace)
                .getRelative(BlockFace.UP)
            for (block in arrayOf(block1, block2))
            {
                if (block.state is Skull)
                {
                    val skull = block.state as Skull
                    try
                    {
                        skull.setOwningPlayer(Bukkit.getPlayer(uuid) ?: Bukkit.getOfflinePlayer(uuid))
                        skull.update(true)
                    } catch (e: Exception)
                    {
                        if (skull.hasOwner())
                        {
                            val material = skull.type

                            @Suppress("DEPRECATION")
                            val blockFace = skull.rotation
                            block.type = Material.AIR
                            TaskTimer.delay(plugin)
                            {
                                block.type = material
                                if (material == Material.PLAYER_WALL_HEAD)
                                {
                                    @Suppress("DEPRECATION")
                                    skull.rotation = sign.facing
                                } else
                                {
                                    @Suppress("DEPRECATION")
                                    skull.rotation = blockFace
                                }
                                skull.update(true)
                            }.run()
                        }
                    }
                }
            }
        }
    }

    fun delete(player: Player)
    {
        if (plugin.data.deleteLocation(Data.VOTE_TOP_SIGNS.path + "." + if (top == 0) "title" else top))
        {
            voteTops.remove(top)
            player.sendMessage(PMessage.VOTE_TOP_MESSAGE_SIGN_DELETED_X.with(if (top == 0) "title" else "$top"))
        }
    }

    companion object
    {
        private val voteTops: MutableMap<Int, VoteTopSign> = HashMap()
        operator fun get(loc: Location): VoteTopSign?
        {
            for (key in voteTops.keys)
            {
                val voteTop = voteTops[key]
                if (voteTop!!.location == loc)
                {
                    return voteTop
                }
            }
            return null
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
                    plugin.launch {
                        voteTop.update()
                    }
                }
            }.run()
        }

        private fun initialize(plugin: CV)
        {
            val locations = plugin.data.getLocations(Data.VOTE_TOP_SIGNS.path)
            for (key in locations.keys)
            {
                val loc = locations[key]
                try
                {
                    VoteTopSign(plugin, key.toInt(), loc)
                } catch (ignored: Exception)
                {
                    VoteTopSign(plugin, 0, loc)
                }
            }
        }
    }

    init
    {
        if (player != null && (top == 0 || !voteTops.containsKey(top)))
        {
            player.sendMessage(PMessage.VOTE_TOP_MESSAGE_SIGN_CREATED_X.with(if (top == 0) "title" else "$top"))
        }
        voteTops[top] = this
        plugin.launch {
            update()
        }
    }
}