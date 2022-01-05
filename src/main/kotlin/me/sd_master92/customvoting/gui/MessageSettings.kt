package me.sd_master92.customvoting.gui

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.Materials
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.items.UseVoteLinkItem
import me.sd_master92.customvoting.gui.items.VoteLinksItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class MessageSettings(private val plugin: CV) : GUI(plugin, "Message Settings", 9, false, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Materials.SOUL_TORCH.get() ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(VoteLinks(plugin).inventory)
            }
            Material.CHEST             ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(Settings.VOTE_LINK_INVENTORY, !plugin.config.getBoolean(Settings.VOTE_LINK_INVENTORY))
                plugin.config.saveConfig()
                event.currentItem = UseVoteLinkItem(plugin)
            }
            Material.BARRIER           ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(VoteSettings(plugin).inventory)
            }
            else                       ->
            {
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    companion object
    {
        val VOTE_LINKS = VoteLinksItem()
    }

    init
    {
        inventory.setItem(0, VOTE_LINKS)
        inventory.setItem(8, BACK_ITEM)
        inventory.setItem(1, UseVoteLinkItem(plugin))
    }
}