package me.sd_master92.customvoting.subjects.voteparty

import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.getEntityHealthString
import me.sd_master92.customvoting.helpers.ParticleHelper
import me.sd_master92.customvoting.listeners.EntityListener
import me.sd_master92.customvoting.listeners.ItemListener
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.block.BlockFace
import org.bukkit.entity.EntityType
import org.bukkit.entity.Pig
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*
import kotlin.collections.set


class VotePartyChest private constructor(private val plugin: CV, val key: String)
{
    private val path = Data.VOTE_PARTY_CHESTS.path + ".$key"
    private val random = Random()
    val items = plugin.data.getItems(path).toMutableList()
    var loc: Location?
        get() = plugin.data.getLocation(path)?.clone()
        set(value)
        {
            if (value != null)
            {
                plugin.data.setLocation(path, value)
            }
        }
    val dropLoc: Location?
        get() = if (loc != null) Location(loc!!.world, loc!!.x + 0.5, loc!!.y - 1, loc!!.z + 0.5) else null
    val fireworkLoc
        get() = if (loc != null) Location(loc!!.world, loc!!.x + 0.5, loc!!.y + 1, loc!!.z + 0.5) else null
    var lockedCrateLoc: Location? = null
    var runningPig: Pig? = null
    var isOpened
        get() = plugin.data.getBoolean("$path.is_opened")
        set(value)
        {
            plugin.data.set("$path.is_opened", value)
            plugin.data.saveConfig()
        }

    fun isEmpty(): Boolean
    {
        return items.isEmpty()
    }

    fun isNotEmpty(): Boolean
    {
        return !isEmpty()
    }

    fun delete(player: Player)
    {
        if (plugin.data.deleteItems(path))
        {
            loc?.let {
                if (it.block.type == Material.ENDER_CHEST)
                {
                    it.block.type = Material.AIR
                }
                plugin.data.deleteLocation(path)
            }
            player.sendMessage(PMessage.VOTE_PARTY_MESSAGE_CHEST_DELETED_X.with(key))
        }
    }

    fun deleteLocation(player: Player)
    {
        plugin.data.deleteLocation(path)
        player.sendMessage(PMessage.VOTE_PARTY_MESSAGE_CHEST_LOCATION_UNSET_X.with(key))
    }

    fun show(loc: Location)
    {
        loc.block.type = Material.ENDER_CHEST
    }

    fun hide(loc: Location)
    {
        loc.block.type = Material.AIR
    }

    fun stop()
    {
        runningPig?.remove()
        lockedCrateLoc?.let {
            it.block.type = Material.AIR
        }
    }

    fun explode(loc: Location, dropLoc: Location)
    {
        loc.world!!.strikeLightningEffect(loc)
        hide(loc)
        while (isNotEmpty())
        {
            dropRandomItem(dropLoc)
        }
        SoundType.EXPLODE.play(plugin, loc)
        TaskTimer.delay(plugin, 20 * 3) { show(loc) }.run()
    }

    fun scare(loc: Location, dropLoc: Location)
    {
        hide(loc)
        val vex = loc.world!!.spawnEntity(loc, EntityType.VEX)
        EntityListener.CANCEL_EVENT.add(vex.uniqueId)
        TaskTimer.delay(plugin, 20 * 8)
        {
            EntityListener.CANCEL_EVENT.remove(vex.uniqueId)
            SoundType.SCARY_4.play(plugin, vex.location)
            loc.block.type = if (random.nextInt(2) == 1) Material.JACK_O_LANTERN else Material.CARVED_PUMPKIN
            loc.block.getRelative(BlockFace.UP).type = Material.FIRE
        }.run().then(
            TaskTimer.delay(plugin, 20 * 2)
            {
                vex.remove()
                explode(loc, dropLoc)
            })
    }

    fun convertToPig(loc: Location)
    {
        hide(loc)
        val pig = loc.world!!.spawnEntity(loc, EntityType.PIG) as Pig
        val health = plugin.config.getDouble(Setting.VOTE_PARTY_PIG_HUNT_HEALTH.path)
        pig.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = health
        pig.health = health
        pig.isInvulnerable = true
        pig.isCustomNameVisible = true
        pig.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 20 * 120, 2))
        runningPig = pig
        EntityListener.PIG_HUNT[pig.uniqueId] = this
        TaskTimer.repeat(plugin, 20, 0, 5)
        {
            if (it.count > 0)
            {
                pig.customName = PMessage.VOTE_PARTY_NAME_PIG_X.with("" + it.count)
                SoundType.CLICK.play(plugin, pig.location)
            } else
            {
                pig.customName = pig.getEntityHealthString()
                pig.isInvulnerable = false
                SoundType.NOTIFY.play(plugin, pig.location)
            }
        }.run()
    }

    fun convertToCrate(loc: Location)
    {
        hide(loc)
        EntityListener.FALLING_LOCKED_CRATE = this
        val fallingBlock = loc.world!!.spawnFallingBlock(
            loc,
            Material.OBSIDIAN.createBlockData()
        )
        fallingBlock.velocity = Vector(0.0, -0.1, 0.0)
    }

    fun popRandomItem(): ItemStack
    {
        return items.removeAt(random.nextInt(items.size))
    }

    fun dropRandomItem(dropLoc: Location)
    {
        if (dropLoc.world != null)
        {
            val uuid = dropLoc.world!!.dropItem(dropLoc, popRandomItem()).uniqueId
            ItemListener.CANCEL_EVENT.add(uuid)
            TaskTimer.delay(plugin, 20 * 10) { ItemListener.CANCEL_EVENT.remove(uuid) }.run()
        }
    }

    fun shootFirework(fireworkLoc: Location)
    {
        if (random.nextInt(3) == 0)
        {
            ParticleHelper.shootFirework(plugin, fireworkLoc)
        }
    }

    companion object
    {
        fun create(plugin: CV, player: Player, key: String? = null): VotePartyChest?
        {
            val number = if (key != null)
            {
                key
            } else
            {
                val numbers = getAll(plugin).map { chest -> chest.key }.mapNotNull { it.toIntOrNull() }
                val maxNumber = numbers.maxOrNull() ?: 0
                var newNumber = maxNumber + 1

                for (i in 1..maxNumber)
                {
                    if (i !in numbers)
                    {
                        newNumber = i
                        break
                    }
                }
                newNumber.toString()
            }

            plugin.data.setItems(Data.VOTE_PARTY_CHESTS.path + ".$number", emptyArray())
            SoundType.SUCCESS.play(plugin, player)
            player.sendMessage(PMessage.VOTE_PARTY_MESSAGE_CHEST_CREATED_X.with(number))
            return getByKey(plugin, number)
        }

        fun getAll(plugin: CV): MutableList<VotePartyChest>
        {
            val list = mutableListOf<VotePartyChest>()
            for (key in plugin.data.getConfigurationSection("items.${Data.VOTE_PARTY_CHESTS.path}")?.getKeys(false)
                ?: emptyList())
            {
                list.add(VotePartyChest(plugin, key))
            }
            return list
        }

        fun getAllWithLocation(plugin: CV): MutableList<VotePartyChest>
        {
            return getAll(plugin).filter { it.loc != null }.toMutableList()
        }

        fun getByKey(plugin: CV, key: String): VotePartyChest?
        {
            return getAll(plugin).firstOrNull { chest -> chest.key == key }
        }

        fun getByLocation(plugin: CV, loc: Location): VotePartyChest?
        {
            return getAll(plugin).firstOrNull { chest -> chest.loc == loc }
        }

        fun resetAll(plugin: CV)
        {
            for (chest in getAll(plugin))
            {
                chest.isOpened = false
                chest.loc?.let {
                    chest.show(it)
                }
            }
        }
    }
}