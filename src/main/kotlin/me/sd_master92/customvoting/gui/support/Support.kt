package me.sd_master92.customvoting.gui.support

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.gui.VoteSettings
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class Support(private val plugin: CV) : GUI(plugin, Strings.SUPPORT_INVENTORY_NAME.toString(), 9)
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
                    Setting.INGAME_UPDATES.path,
                    !plugin.config.getBoolean(Setting.INGAME_UPDATES.path)
                )
                plugin.config.saveConfig()
                event.currentItem = IngameUpdateItem(plugin)
            }

            Material.ENCHANTED_BOOK ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.closeInventory()
                player.sendMessage(Strings.SUPPORT_MESSAGE_DISCORD.toString())
                player.sendMessage(Strings.SUPPORT_MESSAGE_DISCORD_URL.toString())
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
                player.sendMessage(Strings.MERGE_DUPLICATES_MESSAGE_DELETED_X.with("$deleted"))
                if (deleted > 0)
                {
                    SoundType.SUCCESS
                    event.currentItem = MergeItem(plugin)
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
                Strings.SUPPORT_ITEM_NAME_DISCORD.toString(),
                Strings.SUPPORT_ITEM_LORE_DISCORD.toString()
            )
        )
        inventory.addItem(
            BaseItem(
                Material.ENCHANTING_TABLE, Strings.SUPPORT_ITEM_NAME_DATABASE.toString(),
                Strings.GENERAL_ITEM_LORE_STATUS_X.with(if (plugin.hasDatabaseConnection()) Strings.GENERAL_VALUE_CONNECTED.toString() else Strings.GENERAL_VALUE_DISABLED.toString())
            )
        )
        inventory.addItem(
            BaseItem(
                Material.CREEPER_HEAD, Strings.SUPPORT_ITEM_NAME_DONATORS.toString(),
                Strings.SUPPORT_ITEM_LORE_DONATORS.toString()
            )
        )
        inventory.addItem(
            BaseItem(
                Material.PLAYER_HEAD, Strings.SUPPORT_ITEM_NAME_PLAYER_INFO.toString(),
                Strings.SUPPORT_ITEM_LORE_PLAYER_INFO.toString()
            )
        )
        inventory.addItem(MergeItem(plugin))
        inventory.addItem(
            BaseItem(
                Material.CARVED_PUMPKIN, Strings.SUPPORT_ITEM_NAME_STATISTICS.toString(),
                Strings.SUPPORT_ITEM_LORE_STATISTICS.toString()
            )
        )
        inventory.setItem(8, BACK_ITEM)
    }
}

class UpdateItem(plugin: CV) : BaseItem(
    Material.CLOCK, Strings.SUPPORT_ITEM_NAME_VERSION.toString(),
    if (plugin.isUpToDate()) Strings.GENERAL_VALUE_YES.toString() + ";" + Strings.GENERAL_ITEM_LORE_CURRENT_X.with(
        ChatColor.GREEN.toString() + plugin.version
    ) else Strings.GENERAL_ITEM_LORE_CURRENT_X.with(
        ChatColor.RED.toString() + plugin.version
    ) + ";" + Strings.SUPPORT_ITEM_LORE_VERSION_LATEST_X.with(plugin.latestVersion)
)

class IngameUpdateItem(plugin: CV) : StatusItem(
    Material.FILLED_MAP, Strings.SUPPORT_ITEM_NAME_INGAME_UPDATE.toString(),
    plugin.config, Setting.INGAME_UPDATES.path
)

class MergeItem(plugin: CV) : BaseItem(
    Material.HOPPER, Strings.MERGE_DUPLICATES_ITEM_NAME.toString(),
    Strings.MERGE_DUPLICATES_ITEM_LORE.with("" + VoteFile.getAll(plugin).size)
)