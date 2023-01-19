package me.sd_master92.customvoting.subjects.voteparty

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.helpers.ParticleHelper
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import java.util.*

class VotePartyChest(private val plugin: CV, key: String)
{
    private val loc: Location = plugin.data.getLocation(Data.VOTE_PARTY + ".$key")!!.clone().add(0.5, 0.0, 0.5)
    private val dropLoc: Location = Location(loc.world, loc.x, loc.y - 1, loc.z)
    private val fireworkLoc: Location = Location(loc.world, loc.x, loc.y + 1, loc.z)
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

    fun popRandomItem(): ItemStack
    {
        return items.removeAt(random.nextInt(items.size))
    }

    fun dropRandomItem()
    {
        if (dropLoc.world != null)
        {
            dropLoc.world!!.dropItem(dropLoc, popRandomItem())
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