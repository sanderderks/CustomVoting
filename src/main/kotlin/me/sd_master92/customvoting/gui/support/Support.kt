package me.sd_master92.customvoting.gui.support

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.VoteSettings
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class Support(private val plugin: CV) : GUI(plugin, "Support", 9)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER        ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(VoteSettings(plugin).inventory)
            }

            Material.CLOCK          -> if (!plugin.isUpToDate())
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.closeInventory()
                plugin.sendDownloadUrl(player)
            }

            Material.FILLED_MAP     ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.INGAME_UPDATES.path,
                    !plugin.config.getBoolean(Settings.INGAME_UPDATES.path)
                )
                plugin.config.saveConfig()
                event.currentItem = IngameUpdateItem(plugin)
            }

            Material.ENCHANTED_BOOK ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.closeInventory()
                player.sendMessage(ChatColor.AQUA.toString() + "Join the Discord server:")
                player.sendMessage(ChatColor.GREEN.toString() + "https://discord.gg/v3qmJu7jWD")
            }

            Material.CREEPER_HEAD   ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(Donators(plugin).inventory)
            }

            Material.PLAYER_HEAD    ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(PlayerInfo(plugin).inventory)
            }

            Material.HOPPER         ->
            {
                SoundType.CHANGE.play(plugin, player)
                val deleted = VoteFile.mergeDuplicates(plugin)
                if (deleted > 0)
                {
                    SoundType.SUCCESS
                    player.sendMessage(ChatColor.GREEN.toString() + "Deleted $deleted votefiles!")
                    event.currentItem = MergeItem(plugin)
                } else
                {
                    player.sendMessage(ChatColor.RED.toString() + "Deleted 0 votefiles!")
                }
            }

            Material.CARVED_PUMPKIN ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(Statistics(plugin).inventory)
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
        inventory.addItem(UpdateItem(plugin))
        inventory.addItem(IngameUpdateItem(plugin))
        inventory.addItem(
            BaseItem(
                Material.ENCHANTED_BOOK,
                ChatColor.LIGHT_PURPLE.toString() + "Discord",
                ChatColor.GRAY.toString() + "Join the discord server"
            )
        )
        inventory.addItem(
            BaseItem(
                Material.ENCHANTING_TABLE, ChatColor.LIGHT_PURPLE.toString() + "Database",
                ChatColor.GRAY.toString() + "Status: " + if (plugin.hasDatabaseConnection()) ChatColor.GREEN.toString() + "Connected" else ChatColor.RED.toString() + "Disabled"
            )
        )
        inventory.addItem(
            BaseItem(
                Material.CREEPER_HEAD, ChatColor.LIGHT_PURPLE.toString() + "Donators",
                ChatColor.GRAY.toString() + "CustomVoting supporters!"
            )
        )
        inventory.addItem(
            BaseItem(
                Material.PLAYER_HEAD, ChatColor.LIGHT_PURPLE.toString() + "Player Info",
                ChatColor.GRAY.toString() + "Player vote information"
            )
        )
        inventory.addItem(MergeItem(plugin))
        inventory.addItem(
            BaseItem(
                Material.CARVED_PUMPKIN, ChatColor.LIGHT_PURPLE.toString() + "Statistics",
                ChatColor.GRAY.toString() + "CustomVoting BStats"
            )
        )
        inventory.setItem(8, BACK_ITEM)
    }
}

class UpdateItem(plugin: CV) : BaseItem(
    Material.CLOCK, ChatColor.LIGHT_PURPLE.toString() + "Up to date?",
    if (plugin.isUpToDate()) ChatColor.GREEN.toString() + "Yes;" + ChatColor.GRAY.toString() + "Currently: " +
            ChatColor.GREEN + plugin.version else ChatColor.GRAY.toString() +
            "Currently: " + ChatColor.RED + plugin.version + ";" + ChatColor.GRAY +
            "Latest: " + ChatColor.GREEN + plugin.latestVersion + ";;" + ChatColor.GRAY + "Click to " +
            "download"
)

class IngameUpdateItem(plugin: CV) : StatusItem(
    Material.FILLED_MAP, "Ingame Updates",
    plugin.config, Settings.INGAME_UPDATES.path
)

class MergeItem(plugin: CV) : BaseItem(
    Material.HOPPER, ChatColor.RED.toString() + "Merge Duplicates",
    ChatColor.GRAY.toString() + "Find and merge duplicate;" + ChatColor.GRAY + "playerfiles;" + ChatColor.GRAY + "Currently: " +
            ChatColor.GREEN + VoteFile.getAll(plugin).size + " files"
)