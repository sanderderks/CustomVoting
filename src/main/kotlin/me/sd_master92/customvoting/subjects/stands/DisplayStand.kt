package me.sd_master92.customvoting.subjects.stands

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.ArmorType
import me.sd_master92.customvoting.getSkull
import me.sd_master92.customvoting.spawnArmorStand
import me.sd_master92.customvoting.withPlaceholders
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import java.util.*

class DisplayStand(
    private val plugin: CV,
    private val path: String,
    private val top: Int,
    private val main: Boolean = false
)
{
    var stand: ArmorStand? = null
    val location
        get() = stand?.location

    fun update(player: Player?, name: String, uuid: UUID? = null)
    {
        stand?.customName = name.withPlaceholders(player)
        stand?.isCustomNameVisible = true

        if (main)
        {
            setVisible(!CV.CITIZENS, uuid)
        }
    }

    fun create(loc: Location)
    {
        stand = loc.spawnArmorStand()
        plugin.data.set(path, stand!!.uniqueId.toString())
        plugin.data.saveConfig()
    }

    fun delete()
    {
        stand?.remove()
        plugin.data.delete(path)
    }

    private fun setVisible(visible: Boolean, uuid: UUID?)
    {
        stand?.let { stand ->
            stand.isVisible = visible
            if (!visible)
            {
                stand.equipment?.clear()
            } else
            {
                ArmorType.dress(stand.equipment!!, top)
                val player: OfflinePlayer? = try
                {
                    val id = uuid ?: UUID.randomUUID()
                    Bukkit.getPlayer(id) ?: Bukkit.getOfflinePlayer(id)
                } catch (_: Exception)
                {
                    null
                }
                stand.setHelmet(player.getSkull())
            }
        }
    }

    init
    {
        plugin.data.getString(path)?.let {
            val entity = Bukkit.getEntity(UUID.fromString(it))
            if (entity is ArmorStand)
            {
                stand = entity
            }
        }
    }
}