package me.sd_master92.customvoting.gui

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.items.*
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class Support(private val plugin: CV) : GUI(plugin, "Support", 9, false, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER        ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(VoteSettings(plugin).inventory)
            }
            Material.CLOCK          -> if (!plugin.isUpToDate)
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.closeInventory()
                plugin.sendDownloadUrl(player)
            }
            Material.FILLED_MAP     ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.INGAME_UPDATES,
                    !plugin.config.getBoolean(Settings.INGAME_UPDATES)
                )
                plugin.config.saveConfig()
                event.currentItem = IngameUpdateItem(plugin)
            }
            Material.ENCHANTED_BOOK ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.closeInventory()
                player.sendMessage(ChatColor.AQUA.toString() + "Join the Discord server:")
                player.sendMessage(ChatColor.GREEN.toString() + "https://discord.gg/v3qmJu7jWD")
            }
            Material.CREEPER_HEAD   ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(Donators(plugin).inventory)
            }
            else                    ->
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
        inventory.setItem(0, UpdateItem(plugin))
        inventory.setItem(1, IngameUpdateItem(plugin))
        inventory.setItem(2, DiscordItem())
        inventory.setItem(3, DatabaseItem(plugin))
        inventory.setItem(4, DonatorsItem())
        inventory.setItem(8, BACK_ITEM)
    }
}