package me.sd_master92.customvoting.subjects

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.gui.items.SimpleItem
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class VoteSite private constructor(
    private val plugin: CV,
    private val uniqueId: String,
)
{
    private val path = Data.VOTE_SITES.path + "." + uniqueId.lowercase().replace(".", "_")
    var active: Boolean
        get() = plugin.data.getBoolean("$path.active", true)
        set(value)
        {
            plugin.data.set("$path.active", value)
            plugin.data.saveConfig()
        }
    var title: String
        get() = plugin.data.getString("$path.title", uniqueId)!!
        set(value)
        {
            plugin.data.set("$path.title", value)
            plugin.data.saveConfig()
        }
    var description: List<String>
        get() = plugin.data.getStringList("$path.description")
        set(value)
        {
            plugin.data.set("$path.description", value)
            plugin.data.saveConfig()
        }
    var url: String
        get() = plugin.data.getString("$path.url", PMessage.VOTE_LINKS_MESSAGE_URL_DEFAULT.toString())!!
        set(value)
        {
            plugin.data.set("$path.url", value)
            plugin.data.saveConfig()
        }
    var item: ItemStack
        get() = plugin.data.getItemStack(
            "$path.item",
            SimpleItem(Material.DIAMOND)
        )!!
        set(value)
        {
            plugin.data.set("$path.item", value)
            plugin.data.saveConfig()
        }
    var slot: Int
        get() = plugin.data.getInt("$path.slot", -1)
        set(value)
        {
            plugin.data.setNumber("$path.slot", value)
        }

    companion object
    {
        fun migrate(plugin: CV)
        {
            if (plugin.data.isList(Data.VOTE_SITES.path))
            {
                val voteSites = plugin.data.getStringList(Data.VOTE_SITES.path)
                for (uniqueId in voteSites)
                {
                    VoteSite(plugin, uniqueId)
                }
            }
        }

        fun getAll(plugin: CV): MutableList<VoteSite>
        {
            val list = mutableListOf<VoteSite>()
            for (key in plugin.data.getConfigurationSection(Data.VOTE_SITES.path)?.getKeys(false) ?: emptyList())
            {
                list.add(VoteSite(plugin, key))
            }
            return list
        }

        fun exists(plugin: CV, uniqueId: String): Boolean
        {
            return plugin.data.getConfigurationSection(
                Data.VOTE_SITES.path + "." + uniqueId.lowercase().replace(".", "_")
            ) != null
        }

        fun getBySlot(plugin: CV, slot: Int): VoteSite?
        {
            return getAll(plugin).firstOrNull { it.slot == slot }
        }

        fun getItems(plugin: CV): Map<Int, BaseItem>
        {
            val activeVoteSites = getAll(plugin)
                .filter { it.active }

            val items = activeVoteSites.associateBy { voteSite ->
                if (voteSite.slot < 0)
                {
                    voteSite.slot = nextSlot(activeVoteSites)
                }
                voteSite.slot
            }.mapValues { (_, value) ->
                SimpleItem(
                    value.item.type,
                    value.title,
                    if (value.description.isEmpty()) PMessage.VOTE_LINKS_MESSAGE_DESCRIPTION_DEFAULT.toString()
                    else value.description.joinToString(";"),
                    value.item.itemMeta?.hasEnchants() ?: false
                )
            }
            return items
        }

        private fun nextSlot(voteSites: List<VoteSite>): Int
        {
            var nextSlot = 0
            while (voteSites.any { it.slot == nextSlot })
            {
                nextSlot++
            }
            return nextSlot
        }
    }

    init
    {
        if (plugin.data.getConfigurationSection(path) == null)
        {
            title = ChatColor.AQUA.toString() + uniqueId
        }
    }
}