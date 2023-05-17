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
            if (npc != null)
            {
                npc!!.name = name
                ArmorType.dress(npc!!.getOrAddTrait(Equipment::class.java), top)
                refresh()
            } else if (loc != null)
            {
                findOrCreate()
            }
        }
    }

    fun create(loc: Location)
    {
        if (CV.CITIZENS)
        {
            npc =
                CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, PMessage.PLAYER_NAME_UNKNOWN.toString(), loc)
            refresh()
            plugin.data.set(path, npc!!.uniqueId.toString())
            plugin.data.saveConfig()
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
        if (uuid != null)
        {
            npc = CitizensAPI.getNPCRegistry().getByUniqueId(UUID.fromString(uuid))
            refresh()
        } else if (loc != null)
        {
            create(loc)
        }
    }

    private fun refresh()
    {
        if (npc != null)
        {
            if (npc!!.entity != null)
            {
                val loc = npc!!.entity.location
                npc!!.despawn()
                npc!!.data().setPersistent(NPC.Metadata.NAMEPLATE_VISIBLE, false)
                npc!!.spawn(loc)
            } else if (loc != null)
            {
                npc!!.spawn(loc)
            }
        } else if (loc != null)
        {
            create(loc)
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