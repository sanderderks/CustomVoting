package me.sd_master92.customvoting.gui.support

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.VoteSettings
import me.sd_master92.customvoting.gui.items.SimpleItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class Support(private val plugin: CV) : GUI(plugin, PMessage.SUPPORT_INVENTORY_NAME.toString(), 9)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        VoteSettings(plugin).open(player)
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
        addItem(UpdateItem(plugin, this))
        addItem(IngameUpdateItem(plugin))
        addItem(
            object : BaseItem(
                Material.ENCHANTED_BOOK,
                PMessage.SUPPORT_ITEM_NAME_DISCORD.toString(),
                PMessage.SUPPORT_ITEM_LORE_DISCORD.toString()
            )
            {
                override fun onClick(event: InventoryClickEvent, player: Player)
                {
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent = true
                    player.closeInventory()
                    player.sendMessage(PMessage.SUPPORT_MESSAGE_DISCORD.toString())
                    player.sendMessage(PMessage.SUPPORT_MESSAGE_DISCORD_URL.toString())
                }
            }
        )
        addItem(
            SimpleItem(
                Material.ENCHANTING_TABLE, PMessage.SUPPORT_ITEM_NAME_DATABASE.toString(),
                PMessage.GENERAL_ITEM_LORE_STATUS_X.with(if (plugin.hasDatabaseConnection()) PMessage.GENERAL_VALUE_CONNECTED.toString() else PMessage.GENERAL_VALUE_DISABLED.toString())
            )
        )
        addItem(
            object : BaseItem(
                Material.CREEPER_HEAD, PMessage.SUPPORT_ITEM_NAME_DONATORS.toString(),
                PMessage.SUPPORT_ITEM_LORE_DONATORS.toString()
            )
            {
                override fun onClick(event: InventoryClickEvent, player: Player)
                {
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent = true
                    Donators(plugin).open(player)
                }
            }
        )
        addItem(
            object : BaseItem(
                Material.PLAYER_HEAD, PMessage.SUPPORT_ITEM_NAME_PLAYER_INFO.toString(),
                PMessage.SUPPORT_ITEM_LORE_PLAYER_INFO.toString()
            )
            {
                override fun onClick(event: InventoryClickEvent, player: Player)
                {
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent = true
                    PlayerInfo(plugin).open(player)
                }
            }
        )
        addItem(MergeItem(plugin))
        addItem(
            object : BaseItem(
                Material.CARVED_PUMPKIN, PMessage.SUPPORT_ITEM_NAME_STATISTICS.toString(),
                PMessage.SUPPORT_ITEM_LORE_STATISTICS.toString()
            )
            {
                override fun onClick(event: InventoryClickEvent, player: Player)
                {
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent = true
                    Statistics(plugin).open(player)
                }
            }
        )
    }
}

class UpdateItem(private val plugin: CV, private val support: Support) : BaseItem(
    Material.CLOCK, PMessage.SUPPORT_ITEM_NAME_VERSION.toString(),
    if (plugin.isUpToDate()) PMessage.GENERAL_VALUE_YES.toString() + ";" + PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(
        PMessage.GREEN.toString() + plugin.version
    ) else PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(
        PMessage.RED.toString() + plugin.version
    ) + ";" + PMessage.SUPPORT_ITEM_LORE_VERSION_LATEST_X.with(plugin.latestVersion)
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        if (!plugin.isUpToDate())
        {
            SoundType.CLICK.play(plugin, player)
            support.cancelCloseEvent = true
            player.closeInventory()
            plugin.sendDownloadUrl(player)
        }
    }
}

class IngameUpdateItem(private val plugin: CV) : StatusItem(
    Material.FILLED_MAP, PMessage.SUPPORT_ITEM_NAME_INGAME_UPDATE.toString(),
    plugin.config, Setting.INGAME_UPDATES.path
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(
            Setting.INGAME_UPDATES.path,
            !plugin.config.getBoolean(Setting.INGAME_UPDATES.path)
        )
        plugin.config.saveConfig()
        event.currentItem = IngameUpdateItem(plugin)
    }
}

class MergeItem(private val plugin: CV) : BaseItem(
    Material.HOPPER, PMessage.MERGE_DUPLICATES_ITEM_NAME.toString(),
    PMessage.MERGE_DUPLICATES_ITEM_LORE.with("" + VoteFile.getAll(plugin).size)
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        val deleted = VoteFile.mergeDuplicates(plugin)
        player.sendMessage(PMessage.MERGE_DUPLICATES_MESSAGE_DELETED_X.with("$deleted"))
        if (deleted > 0)
        {
            SoundType.SUCCESS
            event.currentItem = MergeItem(plugin)
        }
    }
}