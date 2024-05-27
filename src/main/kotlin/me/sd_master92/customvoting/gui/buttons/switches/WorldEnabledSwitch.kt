package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.WorldExclusionType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class WorldEnabledSwitch(
    private val plugin: CV,
    private val world: String,
    private val label: String,
    private val exclusionType: WorldExclusionType
) : BaseItem(
    Material.GRASS_BLOCK, PMessage.DISABLED_WORLD_ITEM_NAME_X.with(world),
    PMessage.GRAY.getColor() + exclusionType.label() + "ed: " +
            if (plugin.config.getStringList(Setting.WORLD_EXCLUSION_LIST.path).contains(world))
                PMessage.GENERAL_VALUE_YES.toString() else PMessage.GENERAL_VALUE_NO.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        val worlds = plugin.config.getStringList(Setting.WORLD_EXCLUSION_LIST.path)
        if (worlds.contains(world))
        {
            worlds.remove(world)
        } else
        {
            worlds.add(world)
        }
        plugin.config[Setting.WORLD_EXCLUSION_LIST.path] = worlds
        plugin.config.saveConfig()
        event.currentItem = WorldEnabledSwitch(plugin, world, label, exclusionType)
    }
}