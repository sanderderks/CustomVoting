package me.sd_master92.customvoting

import me.clip.placeholderapi.PlaceholderAPI
import me.sd_master92.core.file.PlayerFile
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.database.PlayerTable
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

fun String.withPlaceholders(player: Player): String
{
    return if (CV.PAPI)
    {
        PlaceholderAPI.setPlaceholders(player, this)
    } else
    {
        this
    }
}

fun String.replaceIfNotNull(oldValue: String, newValue: String?): String
{
    if (newValue != null)
    {
        return this.replace(oldValue, newValue)
    }
    return this
}

fun CommandSender?.sendText(message: String)
{
    if (this is Player)
    {
        this.sendMessage(message.withPlaceholders(this))
    } else
    {
        this?.sendMessage(message)
    }
}

fun CommandSender?.sendText(plugin: CV, message: Messages, placeholders: Map<String, String> = HashMap())
{
    this?.sendText(message.getMessage(plugin, placeholders))
}

fun CommandSender?.sendTexts(messages: List<String>)
{
    if (this != null)
    {
        for (line in messages)
        {
            this.sendText(line)
        }
    }
}

fun CommandSender?.sendTexts(plugin: CV, message: Messages, placeholders: Map<String, String> = HashMap())
{
    if (this != null)
    {
        for (line in message.getMessages(plugin, placeholders))
        {
            this.sendText(line)
        }
    }
}

fun Player?.sendActionBar(message: String)
{
    this?.spigot()?.sendMessage(
        ChatMessageType.ACTION_BAR,
        TextComponent(message.withPlaceholders(this))
    )
}

fun Player.addToInventoryOrDrop(items: Array<ItemStack>, random: Boolean = false)
{
    if (items.isNotEmpty())
    {
        if (random)
        {
            this.addToInventoryOrDrop(items[Random().nextInt(items.size)])
        } else
        {
            items.forEach { this.addToInventoryOrDrop(it) }
        }
    }
}

fun Player.addToInventoryOrDrop(item: ItemStack)
{
    this.inventory.addItem(item).values.forEach { this.world.dropItemNaturally(this.location, it) }
}

fun Player.hasPermissionRewards(plugin: CV): Boolean
{
    return this.hasPermissionRewardsByGroup(plugin) || this.hasPermissionRewardsByUser(plugin)
}

fun Player.hasPermissionRewardsByGroup(plugin: CV): Boolean
{
    if (CV.PERMISSION != null)
    {
        try
        {
            for (group in CV.PERMISSION!!.getPlayerGroups(this))
            {
                if (plugin.config.getStringList(Settings.ENABLED_OP_GROUPS.path).contains(group.lowercase()))
                {
                    return true
                }
            }
        } catch (e: Exception)
        {
            return false
        }
    }
    return false
}

fun Player.hasPermissionRewardsByUser(plugin: CV): Boolean
{
    val voter = if (plugin.hasDatabaseConnection()) PlayerTable.get(plugin, this) else VoteFile.get(plugin, this)
    return voter.isOpUser
}

fun Player.getPlayerFile(plugin: CV): PlayerFile
{
    val player = if (plugin.config.getBoolean(Settings.UUID_STORAGE.path)) PlayerFile.getByUuid(
        plugin,
        this
    ) else PlayerFile.getByName(this.name)
    return player ?: PlayerFile.getByUuid(plugin, this)
}

fun OfflinePlayer?.getSkull(): ItemStack
{
    val skull = ItemStack(Material.PLAYER_HEAD)
    val skullMeta = skull.itemMeta as SkullMeta
    skullMeta.owningPlayer = this
    if (this != null)
    {
        skullMeta.setDisplayName(
            Strings.PLAYER_ITEM_NAME_SKULL_X.with(
                this.name ?: Strings.PLAYER_NAME_UNKNOWN.toString()
            )
        )
    }
    skull.itemMeta = skullMeta
    return skull
}

fun String.getOfflinePlayer(): OfflinePlayer?
{
    return Bukkit.getOfflinePlayers().toList().firstOrNull { player -> player.name == this }
}