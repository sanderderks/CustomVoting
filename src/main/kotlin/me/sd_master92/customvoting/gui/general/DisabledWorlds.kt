package me.sd_master92.customvoting.gui.general

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.Strings
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class DisabledWorlds(private val plugin: CV) :
    GUI(plugin, Strings.DISABLED_WORLD_OVERVIEW_INVENTORY_NAME.toString(), 27)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER     ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(GeneralSettings(plugin).inventory)
            }

            Material.GRASS_BLOCK ->
            {
                SoundType.CHANGE.play(plugin, player)
                var world = ChatColor.stripColor(item.itemMeta?.displayName)
                if (world == null)
                {
                    world = ""
                }
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

            else                 ->
            {
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    init
    {
        for (world in Bukkit.getWorlds())
        {
            inventory.addItem(DisabledWorld(plugin, world.name))
        }
        inventory.setItem(26, BACK_ITEM)
    }
}

class DisabledWorld(plugin: CV, world: String) : BaseItem(
    Material.GRASS_BLOCK, Strings.DISABLED_WORLD_ITEM_NAME_X.with(world),
    Strings.GENERAL_ITEM_LORE_DISABLED_X.with(
        if (plugin.config.getStringList(Setting.DISABLED_WORLDS.path)
                .contains(world)
        )
            Strings.GENERAL_VALUE_YES.toString() else Strings.GENERAL_VALUE_NO.toString()
    )
)