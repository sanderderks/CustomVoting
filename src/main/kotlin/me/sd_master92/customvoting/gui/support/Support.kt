package me.sd_master92.customvoting.gui.support

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.gui.VoteSettings
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class Support(private val plugin: CV) : GUI(plugin, Strings.GUI_TITLE_SUPPORT.toString(), 9)
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
                player.sendMessage(Strings.SUPPORT_DISCORD_CHAT.toString())
                player.sendMessage(Strings.SUPPORT_DISCORD_CHAT_URL.toString())
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
                player.sendMessage(Strings.VOTE_FILES_DELETED_X.with("$deleted"))
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
                Strings.SUPPORT_DISCORD.toString(),
                Strings.SUPPORT_DISCORD_LORE.toString()
            )
        )
        inventory.addItem(
            BaseItem(
                Material.ENCHANTING_TABLE, Strings.SUPPORT_DATABASE.toString(),
                Strings.GUI_STATUS_X.with(if (plugin.hasDatabaseConnection()) Strings.CONNECTED.toString() else Strings.DISABLED.toString())
            )
        )
        inventory.addItem(
            BaseItem(
                Material.CREEPER_HEAD, Strings.SUPPORT_DONATORS.toString(),
                Strings.SUPPORT_DONATORS_LORE.toString()
            )
        )
        inventory.addItem(
            BaseItem(
                Material.PLAYER_HEAD, Strings.SUPPORT_PLAYER_INFO.toString(),
                Strings.SUPPORT_PLAYER_INFO_LORE.toString()
            )
        )
        inventory.addItem(MergeItem(plugin))
        inventory.addItem(
            BaseItem(
                Material.CARVED_PUMPKIN, Strings.SUPPORT_STATISTICS.toString(),
                Strings.SUPPORT_STATISTICS_LORE.toString()
            )
        )
        inventory.setItem(8, BACK_ITEM)
    }
}

class UpdateItem(plugin: CV) : BaseItem(
    Material.CLOCK, Strings.SUPPORT_UP_TO_DATE.toString(),
    if (plugin.isUpToDate()) Strings.YES.toString() + ";" + Strings.GUI_CURRENT_X.with(ChatColor.GREEN.toString() + plugin.version) else Strings.GUI_CURRENT_X.with(
        ChatColor.RED.toString() + plugin.version
    ) + ";" + Strings.SUPPORT_LATEST_X.with(plugin.latestVersion)
)

class IngameUpdateItem(plugin: CV) : StatusItem(
    Material.FILLED_MAP, Strings.SUPPORT_INGAME_UPDATE.toString(),
    plugin.config, Settings.INGAME_UPDATES.path
)

class MergeItem(plugin: CV) : BaseItem(
    Material.HOPPER, Strings.SUPPORT_MERGE_DUPLICATES.toString(),
    Strings.SUPPORT_MERGE_DUPLICATES_L0RE_X.with("" + VoteFile.getAll(plugin).size)
)