package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.capitalize
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.WorldExclusionType
import me.sd_master92.customvoting.gui.buttons.switches.WorldEnabledSwitch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class WorldOverviewPage(private val plugin: CV, backPage: GUI?, private val worldExclusionType: WorldExclusionType) :
    GUI(plugin, backPage, PMessage.DISABLED_WORLD_OVERVIEW_INVENTORY_NAME_X.with(worldExclusionType.label()), { 27 })
{
    override fun newInstance(): GUI
    {
        return WorldOverviewPage(plugin, backPage, worldExclusionType)
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
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
            val name = world.name
            val label: String
            val overworld = PMessage.DISABLED_WORLD_NAME_OVERWORLD.toString()
            val nether = PMessage.DISABLED_WORLD_NAME_NETHER.toString()
            val end = PMessage.DISABLED_WORLD_NAME_END.toString()

            label = when (name)
            {
                overworld -> PMessage.DISABLED_WORLD_ITEM_NAME_OVERWORLD.toString()
                nether    -> PMessage.DISABLED_WORLD_ITEM_NAME_NETHER.toString()
                end       -> PMessage.DISABLED_WORLD_ITEM_NAME_END.toString()
                else      -> name.capitalize()
            }
            addItem(WorldEnabledSwitch(plugin, name, label, worldExclusionType))
        }
    }
}