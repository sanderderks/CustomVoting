package me.sd_master92.customvoting.gui.rewards

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.Strings
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class EnabledGroups(private val plugin: CV) :
    GUI(plugin, Strings.GUI_TITLE_ENABLED_GROUPS.toString(), 36)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER                       ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(RewardSettings(plugin, true).inventory)
            }

            Material.GREEN_WOOL, Material.RED_WOOL ->
            {
                SoundType.CHANGE.play(plugin, player)
                var group = ChatColor.stripColor(item.itemMeta?.displayName)
                if (group == null)
                {
                    group = ""
                }
                val groups = plugin.config.getStringList(Settings.ENABLED_OP_GROUPS.path)
                if (groups.contains(group.lowercase()))
                {
                    groups.remove(group)
                } else
                {
                    groups.add(group)
                }
                plugin.config[Settings.ENABLED_OP_GROUPS.path] = groups
                plugin.config.saveConfig()
                event.currentItem = EnabledGroup(plugin, group).update()
            }

            else                                   ->
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
        if (CV.PERMISSION != null)
        {
            for (group in CV.PERMISSION!!.groups)
            {
                inventory.addItem(EnabledGroup(plugin, group).update())
            }
        }
        inventory.setItem(35, BACK_ITEM)
    }
}

class EnabledGroup(private val plugin: CV, private val name: String) : BaseItem(
    Material.GREEN_WOOL, Strings.GUI_ENABLED_GROUP_X.with(name.lowercase()),
    Strings.GUI_ENABLED_X.with(
        if (plugin.config.getStringList(Settings.ENABLED_OP_GROUPS.path)
                .contains(name.lowercase())
        )
            Strings.YES.toString() else Strings.NO.toString()
    )
)
{
    fun update(): EnabledGroup
    {
        if (!plugin.config.getStringList(Settings.ENABLED_OP_GROUPS.path).contains(name.lowercase()))
        {
            type = Material.RED_WOOL
        }
        return this
    }
}
