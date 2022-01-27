package me.sd_master92.customvoting.gui

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.items.BaseItem
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import kotlin.jvm.internal.Intrinsics

abstract class ConfirmGUI(private val plugin: CV, name: String) : GUI(plugin, name, 9, false, true)
{
    abstract fun onConfirm(event: InventoryClickEvent, player: Player)
    abstract fun onCancel(event: InventoryClickEvent, player: Player)
    
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.GREEN_WOOL ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                onConfirm(event, player)
            }
            Material.RED_WOOL   ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                onCancel(event, player)
            }
            else                ->
            {
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        Intrinsics.checkNotNullParameter(event, "event")
        Intrinsics.checkNotNullParameter(player, "player")
        SoundType.CLOSE.play(plugin, player)
    }

    init
    {
        inventory.setItem(2, BaseItem(Material.GREEN_WOOL, ChatColor.GREEN.toString() + "Confirm"))
        inventory.setItem(6, BaseItem(Material.RED_WOOL, ChatColor.RED.toString() + "Decline"))
    }
}