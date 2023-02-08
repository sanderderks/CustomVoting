package me.sd_master92.customvoting.gui.buttons.abstracts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

abstract class AbstractRewardItemsButton(
    private val plugin: CV,
    private val backPage: GUI,
    path: String,
    name: String,
    mat: Material = Material.CHEST
) : BaseItem(mat, name)
{
    abstract fun onOpen(player: Player)

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        backPage.cancelCloseEvent = true
        onOpen(player)
    }

    init
    {
        val size = plugin.data.getItems(path).size
        setLore(
            PMessage.GENERAL_ITEM_LORE_CURRENT_XY.with(
                "$size",
                PMessage.ITEM_REWARDS_UNIT_STACKS_MULTIPLE.toString()
            )
        )
    }
}