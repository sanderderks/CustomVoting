package me.sd_master92.customvoting.subjects

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.capitalize
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.constants.models.VoteSiteUUID
import me.sd_master92.customvoting.gui.items.SimpleItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class VoteSite private constructor(
    private val plugin: CV,
    val uniqueId: VoteSiteUUID,
)
{
    private val path = Data.VOTE_SITES.path + ".$uniqueId"
    var active: Boolean
        get() = plugin.data.getBoolean("$path.active", true)
        set(value)
        {
            plugin.data.set("$path.active", value)
            plugin.data.saveConfig()
        }
    var title: String
        get() = plugin.data.getString("$path.title", uniqueId.toString())!!
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
    var url: String?
        get() = plugin.data.getString("$path.url")
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
    var interval: Int
        get() = plugin.data.getInt("$path.interval", 24)
        set(value)
        {
            plugin.data.setNumber("$path.interval", value)
        }

    fun getGUIItem(editor: Boolean): SimpleItem
    {
        return object : SimpleItem(
            item.type,
            title,
            (if (description.isEmpty()) PMessage.VOTE_SITES_MESSAGE_DESCRIPTION_DEFAULT.toString()
            else description.joinToString(";")) +
                    if (editor) ";;" +
                            PMessage.GRAY + PMessage.GENERAL_UNIT_URL + ": " + PMessage.GREEN + (url
                        ?: PMessage.GENERAL_VALUE_NONE.toString().lowercase()) + ";" +
                            PMessage.GRAY + PMessage.VOTE_SITES_UNIT_INTERVAL.toString()
                        .capitalize() + ": " + PMessage.GREEN + interval + "h"
                    else "",
            item.itemMeta?.hasEnchants() ?: false
        )
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
            {
                if(!editor)
                {
                    TaskTimer.delay(plugin)
                    {
                        if (event.inventory is GUI)
                        {
                            (event.inventory as GUI).cancelCloseEvent = true
                        }
                        if (url != null)
                        {
                            SoundType.SUCCESS.play(plugin, player)
                            player.sendMessage(
                                Message.VOTE_COMMAND_PREFIX.getMessage(
                                    plugin,
                                    mapOf(Pair("%SERVICE%", url!!))
                                )
                            )
                        } else
                        {
                            SoundType.FAILURE.play(plugin, player)
                            player.sendMessage(PMessage.VOTE_SITES_MESSAGE_URL_DEFAULT.toString().capitalize())
                        }
                        player.closeInventory()
                    }.run()
                }
            }
        }
    }

    suspend fun getLastByDate(voter: Voter): Long?
    {
        return (voter.getHistory().filter {
            it.site == uniqueId
        }.maxByOrNull { it.time }?.time)
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
                    VoteSite(plugin, VoteSiteUUID(uniqueId))
                }
            }
        }

        fun register(plugin: CV, uniqueId: VoteSiteUUID)
        {
            if (!exists(plugin, uniqueId))
            {
                VoteSite(plugin, uniqueId)
            }
        }

        fun getAll(plugin: CV): MutableList<VoteSite>
        {
            val list = mutableListOf<VoteSite>()
            for (key in plugin.data.getConfigurationSection(Data.VOTE_SITES.path)?.getKeys(false) ?: emptyList())
            {
                list.add(VoteSite(plugin, VoteSiteUUID(key)))
            }
            return list
        }

        fun getAllActive(plugin: CV): MutableList<VoteSite>
        {
            return getAll(plugin).filter { it.active }.toMutableList()
        }

        fun exists(plugin: CV, uniqueId: VoteSiteUUID): Boolean
        {
            return plugin.data.getConfigurationSection(
                Data.VOTE_SITES.path + ".$uniqueId"
            ) != null
        }

        fun get(plugin: CV, uniqueId: VoteSiteUUID): VoteSite?
        {
            return getAll(plugin).firstOrNull { it.uniqueId == uniqueId }
        }

        fun getBySlot(plugin: CV, slot: Int): VoteSite?
        {
            return getAll(plugin).firstOrNull { it.slot == slot }
        }

        fun getItems(plugin: CV, editor: Boolean = false): Map<Int, BaseItem>
        {
            val activeVoteSites = getAllActive(plugin)

            val items = activeVoteSites.associateBy { voteSite ->
                if (voteSite.slot < 0)
                {
                    voteSite.slot = nextSlot(activeVoteSites)
                }
                voteSite.slot
            }.mapValues { (_, site) ->
                site.getGUIItem(editor)
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
            title = PMessage.AQUA.getColor() + uniqueId.serviceName
        }
    }
}