package me.sd_master92.customvoting.gui.general

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.stripColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class DisabledWorlds(private val plugin: CV) :
    GUI(plugin, PMessage.DISABLED_WORLD_OVERVIEW_INVENTORY_NAME.toString(), 27)
{

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        GeneralSettings(plugin).open(player)
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    init
    {
        for (world in Bukkit.getWorlds())
        {
            addItem(DisabledWorld(plugin, world.name))
        }
    }
}

class DisabledWorld(private val plugin: CV, world: String) : BaseItem(
    Material.GRASS_BLOCK, PMessage.DISABLED_WORLD_ITEM_NAME_X.with(world),
    PMessage.GENERAL_ITEM_LORE_DISABLED_X.with(
        if (plugin.config.getStringList(Setting.DISABLED_WORLDS.path)
                .contains(world)
        )
            PMessage.GENERAL_VALUE_YES.toString() else PMessage.GENERAL_VALUE_NO.toString()
    )
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        val world = itemMeta!!.displayName.stripColor()
        val worlds = plugin.config.getStringList(Setting.DISABLED_WORLDS.path)
        if (worlds.contains(world))
        {
            worlds.remove(world)
        } else
        {
            worlds.add(world)
        }
        plugin.config[Setting.DISABLED_WORLDS.path] = worlds
        plugin.config.saveConfig()
        event.currentItem = DisabledWorld(plugin, world)
    }
}