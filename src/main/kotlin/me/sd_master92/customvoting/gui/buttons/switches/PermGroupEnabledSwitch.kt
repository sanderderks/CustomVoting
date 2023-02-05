package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.stripColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class PermGroupEnabledSwitch(private val plugin: CV, private val name: String) : BaseItem(
    Material.GREEN_WOOL, PMessage.PERM_GROUP_ITEM_NAME_X.with(name.lowercase())
)
{
    fun update(): PermGroupEnabledSwitch
    {
        if (!plugin.config.getStringList(Setting.ENABLED_PERM_GROUPS.path).contains(name.lowercase()))
        {
            type = Material.RED_WOOL
        }
        return this
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        val group = itemMeta!!.displayName.stripColor()
        val groups = plugin.config.getStringList(Setting.ENABLED_PERM_GROUPS.path)
        if (groups.contains(group.lowercase()))
        {
            groups.remove(group)
        } else
        {
            groups.add(group)
        }
        plugin.config[Setting.ENABLED_PERM_GROUPS.path] = groups
        plugin.config.saveConfig()
        event.currentItem = PermGroupEnabledSwitch(plugin, group).update()
    }

    init
    {
        val value = if (plugin.config.getStringList(Setting.ENABLED_PERM_GROUPS.path).contains(name.lowercase()))
        {
            PMessage.GENERAL_VALUE_YES.toString()
        } else
        {
            PMessage.GENERAL_VALUE_NO.toString()
        }
        setLore(PMessage.GENERAL_ITEM_LORE_ENABLED_X.with(value))
    }
}