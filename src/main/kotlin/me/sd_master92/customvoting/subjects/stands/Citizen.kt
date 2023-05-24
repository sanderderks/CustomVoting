package me.sd_master92.customvoting.subjects.stands

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.ArmorType
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.api.trait.trait.Equipment
import org.bukkit.Location
import org.bukkit.entity.EntityType

class Citizen(private val plugin: CV, private val path: String, private val top: Int, private var loc: Location?)
{
    private var npc: NPC? = null

    private fun refresh()
    {
        npc?.let { npc ->
            npc.data().setPersistent(NPC.Metadata.NAMEPLATE_VISIBLE, false)
            if (npc.isSpawned) npc.despawn()
            loc?.let { npc.spawn(it) }
        }
    }

    private fun findOrCreate()
    {
        if (npc == null)
        {
            val id = plugin.data.getNumber(path)
            npc = if (id > 0)
            {
                CitizensAPI.getNPCRegistry().getById(id)
            } else
            {
                create(loc)
            }
        }
        npc?.let { npc ->
            if (!npc.isSpawned && loc != null)
            {
                npc.spawn(loc)
            }
        }
    }

    fun create(newLoc: Location?): NPC?
    {
        this.loc = newLoc
        if (loc == null) return null
        val newNpc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Steve")
        newNpc.spawn(loc)
        ArmorType.dress(newNpc.getOrAddTrait(Equipment::class.java), top)
        plugin.data.setNumber(path, newNpc.id)
        return newNpc
    }

    fun update(name: String)
    {
        findOrCreate()
        npc?.let { npc ->
            if (npc.name != name)
            {
                npc.name = name
                ArmorType.dress(npc.getOrAddTrait(Equipment::class.java), top)
            }
        }
        refresh()
    }

    fun delete()
    {
        npc?.destroy()
        plugin.data.delete(path)
    }

    init
    {
        if (CV.CITIZENS)
        {
            findOrCreate()
            refresh()
        }
    }
}

