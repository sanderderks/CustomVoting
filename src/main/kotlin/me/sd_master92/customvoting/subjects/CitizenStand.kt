package me.sd_master92.customvoting.subjects

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.database.PlayerTable
import me.sd_master92.customvoting.helpers.EntityHelper
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class CitizenStand @JvmOverloads constructor(private val plugin: CV, private val top: Int, player: Player? = null)
{
    private var topStand: ArmorStand? = null
    private var nameStand: ArmorStand? = null
    private var votesStand: ArmorStand? = null
    private var citizen: NPC? = null

    private fun registerArmorStands()
    {
        val section = plugin.data.getConfigurationSection(Data.CITIZENS + "." + top)
        if (section != null)
        {
            topStand = getArmorStand(section.getString("top"))
            nameStand = getArmorStand(section.getString("name"))
            votesStand = getArmorStand(section.getString("votes"))
            citizen = CitizensAPI.getNPCRegistry().getByUniqueId(UUID.fromString(section.getString("citizen")))
            if (citizen != null && !citizen!!.isSpawned)
            {
                citizen!!.spawn(plugin.data.getLocation(Data.CITIZENS + "." + top))
            }
        }
    }

    private fun getArmorStand(uuid: String?): ArmorStand?
    {
        if (uuid != null)
        {
            val entity = plugin.server.getEntity(UUID.fromString(uuid))
            if (entity is ArmorStand)
            {
                return entity
            }
        }
        return null
    }

    private fun create(player: Player)
    {
        val world = player.location.world
        if (world != null)
        {
            val topStand = spawnArmorStand(player.location.add(0.0, 1.0, 0.0))
            plugin.data[Data.CITIZENS + "." + top + ".top"] = topStand.uniqueId.toString()
            topStand.isVisible = false
            this.topStand = topStand

            val nameStand = spawnArmorStand(player.location.add(0.0, 0.5, 0.0))
            plugin.data[Data.CITIZENS + "." + top + ".name"] = nameStand.uniqueId.toString()
            nameStand.isVisible = false
            this.nameStand = nameStand

            val votesStand = spawnArmorStand(player.location)
            plugin.data[Data.CITIZENS + "." + top + ".votes"] = votesStand.uniqueId.toString()
            plugin.data.saveConfig()
            this.votesStand = votesStand

            val citizen = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Unknown")
            plugin.data[Data.CITIZENS + "." + top + ".citizen"] = citizen.uniqueId.toString()
            citizen.isProtected = true
            citizen.data().setPersistent(NPC.NAMEPLATE_VISIBLE_METADATA, false)
            citizen.spawn(votesStand.location)
            if (citizen.isSpawned)
            {
                val entityEquipment = (citizen.entity as LivingEntity).equipment
                if (entityEquipment != null)
                {
                    EntityHelper.setEntityEquipment(entityEquipment, top)
                }
            }
            this.citizen = citizen
        }
        plugin.data.setLocation(Data.CITIZENS + "." + top, player.location)
        player.sendMessage(ChatColor.GREEN.toString() + "Registered Citizen Stand #" + top)
    }

    private fun spawnArmorStand(loc: Location): ArmorStand
    {
        val stand = loc.world!!.spawnEntity(loc, EntityType.ARMOR_STAND) as ArmorStand
        stand.removeWhenFarAway = false
        stand.isSilent = true
        stand.setGravity(false)
        stand.isCustomNameVisible = true
        stand.isInvulnerable = true
        stand.isVisible = false
        return stand
    }

    private fun update()
    {
        val voteFile =
            if (plugin.hasDatabaseConnection()) PlayerTable.getTopVoter(plugin, top) else VoteFile.getTopVoter(
                plugin,
                top
            )
        val placeholders: MutableMap<String, String> = HashMap()
        placeholders["%TOP%"] = "" + top
        if (voteFile != null)
        {
            placeholders["%PLAYER%"] = voteFile.name
            placeholders["%VOTES%"] = "${voteFile.votes}"
            placeholders["%PERIOD%"] = "${voteFile.period}"
            if (citizen != null && citizen!!.name != voteFile.name)
            {
                citizen!!.name = voteFile.name
                citizen!!.despawn()
                citizen!!.data()?.setPersistent(NPC.NAMEPLATE_VISIBLE_METADATA, false)
                citizen!!.spawn(plugin.data.getLocation(Data.CITIZENS + "." + top))
            }
        } else
        {
            placeholders["%PLAYER%"] = ChatColor.RED.toString() + "Unknown"
            placeholders["%VOTES%"] = "0"
            placeholders["%PERIOD%"] = "0"
            if (citizen != null && citizen!!.name != "Unknown")
            {
                citizen!!.name = "Unknown"
                citizen!!.despawn()
                citizen!!.data()?.setPersistent(NPC.NAMEPLATE_VISIBLE_METADATA, false)
                citizen!!.spawn(plugin.data.getLocation(Data.CITIZENS + "." + top))
            }
        }
        if (topStand == null || nameStand == null || votesStand == null || citizen == null || !citizen!!.isSpawned)
        {
            registerArmorStands()
        }
        topStand?.customName = Messages.VOTE_TOP_STANDS_TOP.getMessage(plugin, placeholders)
        nameStand?.customName = Messages.VOTE_TOP_STANDS_CENTER.getMessage(plugin, placeholders)
        votesStand?.customName = Messages.VOTE_TOP_STANDS_BOTTOM.getMessage(plugin, placeholders)
    }

    fun delete(player: Player)
    {
        topStand?.remove()
        nameStand?.remove()
        votesStand?.remove()
        citizen?.let { CitizensAPI.getNPCRegistry().deregister(citizen) }
        plugin.data[Data.CITIZENS + "." + top] = null
        plugin.data.saveConfig()
        voteTops.remove(top)
        player.sendMessage(ChatColor.GREEN.toString() + "Successfully deleted Citizen Stand #" + top)
    }

    companion object
    {
        private val voteTops: MutableMap<Int, CitizenStand> = HashMap()
        operator fun get(top: Int): CitizenStand?
        {
            return voteTops[top]
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
            val section = plugin.data.getConfigurationSection(Data.CITIZENS)
            if (section != null)
            {
                for (n in section.getKeys(false))
                {
                    try
                    {
                        val top = n.toInt()
                        CitizenStand(plugin, top)
                    } catch (ignored: Exception)
                    {
                    }
                }
            }
        }
    }

    init
    {
        val section = plugin.data.getConfigurationSection(Data.CITIZENS + "." + top)
        if (section != null)
        {
            if (player != null)
            {
                player.sendMessage(ChatColor.RED.toString() + "That Citizen Stand already exists.")
            } else
            {
                registerArmorStands()
                voteTops[top] = this
                update()
            }
        } else player?.let {
            create(it)
            voteTops[top] = this
            update()
        }
    }
}