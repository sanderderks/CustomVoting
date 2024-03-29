package me.sd_master92.customvoting.subjects.stands

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.ArmorType
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.npc.NPC
import net.citizensnpcs.api.trait.trait.Equipment
import org.bukkit.Location
import org.bukkit.entity.EntityType
import java.util.*

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
        npc = npc ?: plugin.data.getString(path)?.let { storedUUID ->
            try
            {
                CitizensAPI.getNPCRegistry().getByUniqueId(UUID.fromString(storedUUID))
            } catch (e: IllegalArgumentException)
            {
                null
            }
        } ?: plugin.data.getNumber(path).let { id ->
            if (id > 0) CitizensAPI.getNPCRegistry().getById(id) else create(loc)
        }

        npc?.takeIf { !it.isSpawned && loc != null }?.spawn(loc)
    }

    fun create(newLoc: Location?): NPC?
    {
        this.loc = newLoc
        if (loc == null) return null
        val newNpc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Steve")
        ArmorType.dress(newNpc.getOrAddTrait(Equipment::class.java), top)
        plugin.data.setNumber(path, newNpc.id)
        newNpc.spawn(loc)
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
        findOrCreate()
        refresh()
    }
}

