package me.sd_master92.customvoting.subjects

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Messages
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.EntityEquipment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


class VoteTopStand @JvmOverloads constructor(private val plugin: CV, private val top: Int, player: Player? = null)
{
    private var topStand: ArmorStand? = null
    private var nameStand: ArmorStand? = null
    private var votesStand: ArmorStand? = null
    private var citizen: NPC? = null
    private val path = Data.VOTE_TOP_STANDS + "." + top

    private fun registerArmorStands()
    {
        val section = plugin.data.getConfigurationSection(path)
        if (section != null)
        {
            topStand = getArmorStand(section.getString("top"))
            nameStand = getArmorStand(section.getString("name"))
            votesStand = getArmorStand(section.getString("votes"))
            if (CV.CITIZENS)
            {
                if (votesStand?.isVisible == true)
                {
                    votesStand?.isVisible = false
                    votesStand?.equipment?.clear()
                }
                if (citizen == null)
                {
                    citizen = CitizensAPI.getNPCRegistry().getByUniqueId(UUID.fromString(section.getString("citizen")))
                    if (citizen != null && !citizen!!.isSpawned)
                    {
                        citizen!!.spawn(plugin.data.getLocation(path))
                    }
                }
            } else
            {
                votesStand?.isVisible = true
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
            plugin.data["$path.top"] = topStand.uniqueId.toString()
            this.topStand = topStand

            val nameStand = spawnArmorStand(player.location.add(0.0, 0.5, 0.0))
            plugin.data["$path.name"] = nameStand.uniqueId.toString()
            this.nameStand = nameStand

            val votesStand = spawnArmorStand(player.location)
            plugin.data["$path.votes"] = votesStand.uniqueId.toString()
            plugin.data.saveConfig()
            this.votesStand = votesStand

            if (CV.CITIZENS)
            {
                val citizen = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Unknown")
                plugin.data["$path.citizen"] = citizen.uniqueId.toString()
                citizen.isProtected = true
                citizen.data().setPersistent(NPC.NAMEPLATE_VISIBLE_METADATA, false)
                citizen.spawn(player.location)
                if (citizen.isSpawned)
                {
                    val entityEquipment = (citizen.entity as LivingEntity).equipment
                    if (entityEquipment != null)
                    {
                        setEntityEquipment(entityEquipment)
                    }
                }
                this.citizen = citizen
            } else
            {
                votesStand.isVisible = true
                val entityEquipment = votesStand.equipment
                if (entityEquipment != null)
                {
                    setEntityEquipment(entityEquipment)
                }
            }
        }
        plugin.data.setLocation(path, player.location)
        player.sendMessage(ChatColor.GREEN.toString() + "Registered Vote Stand #" + top)
    }

    private fun setEntityEquipment(equipment: EntityEquipment)
    {
        when (top)
        {
            1    ->
            {
                equipment.chestplate = BaseItem(Material.DIAMOND_CHESTPLATE, true)
                equipment.leggings = BaseItem(Material.DIAMOND_LEGGINGS, true)
                equipment.boots = BaseItem(Material.DIAMOND_BOOTS, true)
                equipment.setItemInMainHand(BaseItem(Material.DIAMOND_SWORD, true))
            }

            2    ->
            {
                equipment.chestplate = BaseItem(Material.GOLDEN_CHESTPLATE, true)
                equipment.leggings = BaseItem(Material.GOLDEN_LEGGINGS, true)
                equipment.boots = BaseItem(Material.GOLDEN_BOOTS, true)
                equipment.setItemInMainHand(BaseItem(Material.GOLDEN_SWORD, true))
            }

            3    ->
            {
                equipment.chestplate = BaseItem(Material.IRON_CHESTPLATE, true)
                equipment.leggings = BaseItem(Material.IRON_LEGGINGS, true)
                equipment.boots = BaseItem(Material.IRON_BOOTS, true)
                equipment.setItemInMainHand(BaseItem(Material.IRON_SWORD, true))
            }

            else ->
            {
                equipment.chestplate = BaseItem(Material.CHAINMAIL_CHESTPLATE, true)
                equipment.leggings = BaseItem(Material.CHAINMAIL_LEGGINGS, true)
                equipment.boots = BaseItem(Material.CHAINMAIL_BOOTS, true)
                equipment.setItemInMainHand(BaseItem(Material.STONE_SWORD, true))
            }
        }
    }

    private fun update()
    {
        val topVoter = Voter.getTopVoter(plugin, top)
        val placeholders: MutableMap<String, String> = HashMap()
        placeholders["%TOP%"] = "" + top
        if (topVoter != null)
        {
            placeholders["%PLAYER%"] = topVoter.name
            placeholders["%VOTES%"] = "${topVoter.votes}"
            placeholders["%MONTHLY_VOTES%"] = "${topVoter.monthlyVotes}"
            if (CV.CITIZENS && citizen != null && citizen!!.name != topVoter.name)
            {
                citizen!!.name = topVoter.name
                citizen!!.despawn()
                citizen!!.data()?.setPersistent(NPC.NAMEPLATE_VISIBLE_METADATA, false)
                citizen!!.spawn(plugin.data.getLocation(path))
            }
        } else
        {
            placeholders["%PLAYER%"] = ChatColor.RED.toString() + "Unknown"
            placeholders["%VOTES%"] = "0"
            placeholders["%MONTHLY_VOTES%"] = "0"
            if (CV.CITIZENS && citizen != null && citizen!!.name != "Unknown")
            {
                citizen!!.name = "Unknown"
                citizen!!.despawn()
                citizen!!.data()?.setPersistent(NPC.NAMEPLATE_VISIBLE_METADATA, false)
                citizen!!.spawn(plugin.data.getLocation(path))
            }
        }
        if (topStand == null
            || nameStand == null
            || votesStand == null
            || CV.CITIZENS && (citizen == null || !citizen!!.isSpawned)
        )
        {
            registerArmorStands()
        }
        if (!CV.CITIZENS)
        {
            val skull = ItemStack(Material.PLAYER_HEAD)
            val skullMeta = skull.itemMeta as SkullMeta?
            if (skullMeta != null && topVoter != null)
            {
                try
                {
                    val uuid = UUID.fromString(topVoter.uuid)
                    skullMeta.owningPlayer = Bukkit.getPlayer(uuid) ?: Bukkit.getOfflinePlayer(uuid)
                    skull.itemMeta = skullMeta
                } catch (ignored: Exception)
                {
                }
            }
            votesStand?.equipment?.helmet = skull
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
        plugin.data[path] = null
        plugin.data.saveConfig()
        voteTops.remove(top)
        player.sendMessage(ChatColor.GREEN.toString() + "Successfully deleted Vote Stand #" + top)
    }

    companion object
    {
        private val voteTops: MutableMap<Int, VoteTopStand> = HashMap()

        operator fun get(top: Int): VoteTopStand?
        {
            return voteTops[top]
        }

        fun spawnArmorStand(loc: Location): ArmorStand
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
            val section = plugin.data.getConfigurationSection(Data.VOTE_TOP_STANDS)
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
        val section = plugin.data.getConfigurationSection(path)
        if (section != null)
        {
            if (player != null)
            {
                player.sendMessage(ChatColor.RED.toString() + "That Vote Stand already exists.")
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