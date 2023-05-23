package me.sd_master92.customvoting.subjects.stands

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.ArmorType
import me.sd_master92.customvoting.constants.enumerations.PMessage
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.api.trait.trait.Equipment
import org.bukkit.Location
import org.bukkit.entity.EntityType
import java.util.*

class Citizen(private val plugin: CV, private val path: String, private val top: Int, private val loc: Location?)
{
    private var npc: NPC? = null

    fun update(name: String)
    {
        if (CV.CITIZENS)
        {
            npc?.let {
                if (it.name != name)
                {
                    it.name = name
                    ArmorType.dress(it.getOrAddTrait(Equipment::class.java), top)
                    refresh()
                }
            } ?: run {
                findOrCreate()
            }
        }
    }

    fun create(loc: Location)
    {
        if (CV.CITIZENS)
        {
            npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, PMessage.PLAYER_NAME_UNKNOWN.toString())
            npc?.let {
                it.spawn(loc)
                ArmorType.dress(it.getOrAddTrait(Equipment::class.java), top)
                plugin.data.set(path, it.uniqueId.toString())
                plugin.data.saveConfig()
            }
        }
    }

    fun delete()
    {
        npc?.destroy()
        plugin.data.delete(path)
    }

    private fun findOrCreate()
    {
        val uuid = plugin.data.getString(path)
        npc = if (uuid != null)
        {
            CitizensAPI.getNPCRegistry().getByUniqueId(UUID.fromString(uuid))
        } else
        {
            create(loc!!)
            return
        }
        refresh()
    }

    private fun refresh()
    {
        npc?.let {
            if (it.isSpawned)
            {
                val loc = it.entity.location
                it.despawn()
                it.spawn(loc)
            } else if (loc != null)
            {
                it.spawn(loc)
            }
            it.data().setPersistent(NPC.Metadata.NAMEPLATE_VISIBLE, false)
        }
    }

    init
    {
        if (CV.CITIZENS)
        {
            findOrCreate()
        }
    }
}
