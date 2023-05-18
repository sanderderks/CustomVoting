package me.sd_master92.customvoting.subjects

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.menus.CrateMenu
import me.sd_master92.customvoting.spawnArmorStand
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.Directional
import org.bukkit.entity.Player
import java.util.*

class VoteCrate private constructor(private val plugin: CV, val key: String)
{
    private val path = Data.VOTE_CRATES.path + ".$key"
    val name = plugin.data.getString("$path.name") ?: PMessage.CRATE_NAME_DEFAULT_X.with(key)
    val items = plugin.data.getItems(path).toMutableList()
    var loc = plugin.data.getLocation(path)?.clone()

    fun isEmpty(): Boolean
    {
        return items.isEmpty()
    }

    fun isNotEmpty(): Boolean
    {
        return !isEmpty()
    }

    fun open(player: Player)
    {
        val crate = CrateMenu(plugin, player, key)
        SoundType.OPEN.play(plugin, player)
        crate.open(player)
        crate.run()
    }

    fun create(loc: Location, player: Player)
    {
        val location = Location(loc.world, loc.x, loc.y + 1, loc.z)
        location.block.type = Material.ENDER_CHEST
        val directional = location.block.blockData as Directional
        directional.facing = player.facing.oppositeFace
        location.block.blockData = directional
        plugin.data.setLocation(path, location)

        val stand = Location(
            loc.world,
            loc.x + 0.5,
            loc.y + 1,
            loc.z + 0.5
        ).spawnArmorStand()
        stand.customName = PMessage.CRATE_NAME_STAND_X.with(name)
        plugin.data.set("$path.stand", stand.uniqueId.toString())
        plugin.data.saveConfig()

        SoundType.SUCCESS.play(plugin, player)
        player.closeInventory()
    }

    fun delete(player: Player)
    {
        if (plugin.data.deleteLocation(path))
        {
            plugin.data.getString("$path.stand")?.let {
                val stand = Bukkit.getEntity(UUID.fromString(it))
                stand?.remove()
            }
            player.sendMessage(PMessage.GENERAL_MESSAGE_DELETE_SUCCESS_X.with(name))
        }

    }

    companion object
    {
        fun getAll(plugin: CV): MutableList<VoteCrate>
        {
            val list = mutableListOf<VoteCrate>()
            for (key in plugin.data.getConfigurationSection(Data.VOTE_CRATES.path)?.getKeys(false) ?: emptyList())
            {
                list.add(VoteCrate(plugin, key))
            }
            return list
        }

        fun getByLocation(plugin: CV, loc: Location): VoteCrate?
        {
            return getAll(plugin).firstOrNull { crate -> crate.loc == loc }
        }

        fun getByKey(plugin: CV, key: String): VoteCrate?
        {
            return getAll(plugin).firstOrNull { crate -> crate.key == key }
        }
    }
}