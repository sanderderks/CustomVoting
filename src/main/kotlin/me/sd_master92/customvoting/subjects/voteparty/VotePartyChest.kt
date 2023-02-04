package me.sd_master92.customvoting.subjects.voteparty

import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.helpers.ParticleHelper
import me.sd_master92.customvoting.listeners.EntityListener
import me.sd_master92.customvoting.listeners.ItemListener
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.EntityType
import org.bukkit.entity.Pig
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class VotePartyChest(private val plugin: CV, key: String)
{
    val items: MutableList<ItemStack> = plugin.data.getItems(Data.VOTE_PARTY.path + ".$key").toMutableList()
    val loc: Location = plugin.data.getLocation(Data.VOTE_PARTY.path + ".$key")!!.clone()
    var dropLoc: Location = Location(loc.world, loc.x + 0.5, loc.y - 1, loc.z + 0.5)
    private val fireworkLoc: Location = Location(loc.world, loc.x + 0.5, loc.y + 1, loc.z + 0.5)
    private val random: Random = Random()

    fun isEmpty(): Boolean
    {
        return items.isEmpty()
    }

    fun isNotEmpty(): Boolean
    {
        return !isEmpty()
    }

    fun show()
    {
        loc.block.type = Material.ENDER_CHEST
    }

    fun hide()
    {
        loc.block.type = Material.AIR
    }

    fun explode()
    {
        loc.world!!.strikeLightningEffect(loc)
        hide()
        while (isNotEmpty())
        {
            dropRandomItem()
        }
        SoundType.EXPLODE.play(plugin, loc)
        TaskTimer.delay(plugin, 20 * 3) { show() }.run()
    }

    fun scare()
    {
        hide()
        val vex = loc.world!!.spawnEntity(loc, EntityType.VEX)
        EntityListener.CANCEL_EVENT.add(vex.uniqueId)
        TaskTimer.delay(plugin, 20 * 8)
        {
            EntityListener.CANCEL_EVENT.remove(vex.uniqueId)
            SoundType.SCARY_4.play(plugin, vex.location)
            loc.block.type = if (random.nextInt(2) == 1) Material.JACK_O_LANTERN else Material.CARVED_PUMPKIN
            loc.block.getRelative(BlockFace.UP).type = Material.FIRE
        }.run().then(TaskTimer.delay(plugin, 20 * 2)
        {
            vex.remove()
            explode()
        })
    }

    fun convertToPig()
    {
        hide()
        val pig = loc.world!!.spawnEntity(loc, EntityType.PIG) as Pig
        pig.isInvulnerable = true
        pig.isCustomNameVisible = true
        pig.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 20 * 120, 2))
        EntityListener.PIG_HUNT[pig.uniqueId] = this
        TaskTimer.repeat(plugin, 20, 0, 5)
        {
            if (it.count > 0)
            {
                pig.customName = Strings.VOTE_PARTY_NAME_PIG_X.with("" + it.count)
                SoundType.CLICK.play(plugin, pig.location)
            } else
            {
                pig.customName = null
                pig.isCustomNameVisible = false
                pig.isInvulnerable = false
                SoundType.NOTIFY.play(plugin, pig.location)
            }
        }.run()
    }

    fun popRandomItem(): ItemStack
    {
        return items.removeAt(random.nextInt(items.size))
    }

    fun dropRandomItem()
    {
        if (dropLoc.world != null)
        {
            val uuid = dropLoc.world!!.dropItem(dropLoc, popRandomItem()).uniqueId
            ItemListener.CANCEL_EVENT.add(uuid)
            TaskTimer.delay(plugin, 20 * 10) { ItemListener.CANCEL_EVENT.remove(uuid) }.run()
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
            for (key in plugin.data.getLocations(Data.VOTE_PARTY.path).keys)
            {
                list.add(VotePartyChest(plugin, key))
            }
            return list
        }
    }
}