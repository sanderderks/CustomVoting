package me.sd_master92.customvoting.subjects

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.database.PlayerTable
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.Sign
import org.bukkit.block.Skull
import org.bukkit.block.data.type.WallSign
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class VoteTopSign @JvmOverloads constructor(
    private val plugin: CV,
    private val top: Int,
    val location: Location?,
    player: Player? = null
)
{
    private fun update()
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
            val messages = Messages.VOTE_TOP_SIGNS_TITLE_SIGN.getMessages(plugin)
            for (i in messages.indices)
            {
                sign.setLine(i, messages[i])
            }
            sign.update(true)
        }
    }

    private fun updateSign()
    {
        if (location!!.block.state is Sign)
        {
            val sign = location.block.state as Sign
            val topVoter =
                if (plugin.hasDatabaseConnection()) PlayerTable.getTopVoter(plugin, top) else VoteFile.getTopVoter(
                    plugin,
                    top
                )
            if (topVoter != null)
            {
                val oldLoc = plugin.data.getLocation(Data.VOTE_TOP_SIGNS + "." + top)
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
                                    Messages.VOTE_TOP_SIGNS_PLAYER_SIGNS_OUTDATED.getMessage(plugin)
                                )
                            } else
                            {
                                oldSign.setLine(i, "")
                            }
                        }
                        oldSign.update(true)
                    }
                }
                plugin.data.setLocation(Data.VOTE_TOP_SIGNS + "." + top, location)
                val placeholders = HashMap<String, String>()
                placeholders["%NUMBER%"] = "" + top
                placeholders["%PLAYER%"] = topVoter.name
                placeholders["%VOTES%"] = "${topVoter.votes}"
                placeholders["%PERIOD%"] = "${topVoter.period}"
                if (plugin.config.getBoolean(Settings.MONTHLY_PERIOD))
                {
                    placeholders["%s%"] = if (topVoter.period == 1) "" else "s"
                } else
                {
                    placeholders["%s%"] = if (topVoter.votes == 1) "" else "s"
                }
                for ((i, message) in Messages.VOTE_TOP_SIGNS_PLAYER_SIGNS_FORMAT.getMessages(plugin, placeholders)
                    .withIndex())
                {
                    sign.setLine(i, message)
                }
                updateSkull(location, topVoter.uuid)
            } else
            {
                for (i in 0..3)
                {
                    if (i == 1)
                    {
                        sign.setLine(
                            i,
                            Messages.VOTE_TOP_SIGNS_PLAYER_SIGNS_NOT_FOUND.getMessage(plugin)
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

    private fun updateSkull(loc: Location, uuid: String)
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
                        val pUuid = UUID.fromString(uuid)
                        skull.setOwningPlayer(Bukkit.getPlayer(pUuid) ?: Bukkit.getOfflinePlayer(pUuid))
                        skull.update(true)
                    } catch (e: Exception)
                    {
                        if (skull.hasOwner())
                        {
                            val material = skull.type
                            val blockFace = skull.rotation
                            block.type = Material.AIR
                            object : BukkitRunnable()
                            {
                                override fun run()
                                {
                                    block.type = material
                                    if (material == Material.PLAYER_WALL_HEAD)
                                    {
                                        skull.rotation = sign.facing
                                    } else
                                    {
                                        skull.rotation = blockFace
                                    }
                                    skull.update(true)
                                }
                            }.runTaskLater(plugin, 1L)
                        }
                    }
                }
            }
        }
    }

    fun delete(player: Player)
    {
        if (plugin.data.deleteLocation(Data.VOTE_TOP_SIGNS + "." + if (top == 0) "title" else top))
        {
            voteTops.remove(top)
            player.sendMessage(ChatColor.RED.toString() + "Unregistered Vote Sign #" + if (top == 0) "title" else top)
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
            object : BukkitRunnable()
            {
                override fun run()
                {
                    for (voteTop in voteTops.values)
                    {
                        voteTop.update()
                    }
                }
            }.runTaskLater(plugin, 40L)
        }

        private fun initialize(plugin: CV)
        {
            val locations = plugin.data.getLocations(Data.VOTE_TOP_SIGNS)
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
            player.sendMessage(ChatColor.GREEN.toString() + "Registered Vote Sign #" + if (top == 0) "title" else top)
        }
        voteTops[top] = this
        update()
    }
}