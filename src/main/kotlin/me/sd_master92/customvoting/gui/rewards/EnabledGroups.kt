package me.sd_master92.customvoting.gui.rewards

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.stripColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class EnabledGroups(private val plugin: CV) :
    GUI(plugin, PMessage.ENABLED_GROUP_OVERVIEW_INVENTORY_NAME.toString(), 36)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        RewardSettings(plugin, true).open(player)
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
        if (CV.PERMISSION != null)
        {
            for (group in CV.PERMISSION!!.groups)
            {
                addItem(EnabledGroup(plugin, group).update())
            }
        }
    }
}

class EnabledGroup(private val plugin: CV, private val name: String) : BaseItem(
    Material.GREEN_WOOL, PMessage.ENABLED_GROUP_ITEM_NAME_X.with(name.lowercase()),
    PMessage.GENERAL_ITEM_LORE_ENABLED_X.with(
        if (plugin.config.getStringList(Setting.ENABLED_OP_GROUPS.path)
                .contains(name.lowercase())
        )
            PMessage.GENERAL_VALUE_YES.toString() else PMessage.GENERAL_VALUE_NO.toString()
    )
)
{
    fun update(): EnabledGroup
    {
        if (!plugin.config.getStringList(Setting.ENABLED_OP_GROUPS.path).contains(name.lowercase()))
        {
            type = Material.RED_WOOL
        }
        return this
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        val group = itemMeta!!.displayName.stripColor()
        val groups = plugin.config.getStringList(Setting.ENABLED_OP_GROUPS.path)
        if (groups.contains(group.lowercase()))
        {
            groups.remove(group)
        } else
        {
            groups.add(group)
        }
        plugin.config[Setting.ENABLED_OP_GROUPS.path] = groups
        plugin.config.saveConfig()
        event.currentItem = EnabledGroup(plugin, group).update()
    }
}
