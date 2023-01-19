package me.sd_master92.customvoting.subjects.voteparty

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.helpers.ParticleHelper
import me.sd_master92.customvoting.listeners.ItemListener
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class VotePartyChest(private val plugin: CV, key: String)
{
    private val loc: Location = plugin.data.getLocation(Data.VOTE_PARTY + ".$key")!!.clone()
    private val dropLoc: Location = Location(loc.world, loc.x + 0.5, loc.y - 1, loc.z + 0.5)
    private val fireworkLoc: Location = Location(loc.world, loc.x + 0.5, loc.y + 1, loc.z + 0.5)
    private val items: MutableList<ItemStack> = plugin.data.getItems(Data.VOTE_PARTY + ".$key").toMutableList()
    private val random: Random = Random()

    fun isEmpty(): Boolean
    {
        return items.isEmpty()
    }

    fun isNotEmpty(): Boolean
    {
        return !isEmpty()
    }

    fun explode()
    {
        val chest = loc.block
        chest.world.strikeLightningEffect(loc)
        chest.type = Material.AIR
        while (isNotEmpty())
        {
            dropRandomItem()
        }
        SoundType.EXPLODE.play(plugin, loc)
        object : BukkitRunnable()
        {
            override fun run()
            {
                chest.type = Material.ENDER_CHEST
            }
        }.runTaskLater(plugin, 60)
    }

    fun popRandomItem(): ItemStack
    {
        return items.removeAt(random.nextInt(items.size))
    }

    fun dropRandomItem()
    {
        if (dropLoc.world != null)
        {
            ItemListener.CANCEL_EVENT.add(dropLoc.world!!.dropItem(dropLoc, popRandomItem()).uniqueId)
        }
    }

    fun shootFirework()
    {
        if (random.nextInt(3) == 0)
        {
            ParticleHelper.shootFirework(plugin, fireworkLoc)
        }
    }

    companion object
    {
        fun getAll(plugin: CV): MutableList<VotePartyChest>
        {
            val list = mutableListOf<VotePartyChest>()
            for (key in plugin.data.getLocations(Data.VOTE_PARTY).keys)
            {
                list.add(VotePartyChest(plugin, key))
            }
            return list
        }
    }
}