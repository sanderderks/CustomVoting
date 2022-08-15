package me.sd_master92.customvoting

import me.clip.placeholderapi.PlaceholderAPI
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.database.PlayerTable
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
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

fun Player?.sendActionBar(plugin: CV, message: Messages, placeholders: Map<String, String> = HashMap())
{
    this?.sendActionBar(message.getMessage(plugin, placeholders))
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
        for (group in CV.PERMISSION!!.getPlayerGroups(this))
        {
            if (plugin.config.getStringList(Settings.ENABLED_OP_GROUPS.path).contains(group.lowercase()))
            {
                return true
            }
        }
    }
    return false
}

fun Player.hasPermissionRewardsByUser(plugin: CV): Boolean
{
    val voter = if (plugin.hasDatabaseConnection()) PlayerTable(plugin, this) else VoteFile(this, plugin)
    return voter.isOpUser
}

fun OfflinePlayer.getSkull(): ItemStack
{
    val skull = ItemStack(Material.PLAYER_HEAD)
    val skullMeta = skull.itemMeta as SkullMeta
    skullMeta.owningPlayer = this
    skullMeta.setDisplayName(ChatColor.AQUA.toString() + name)
    skull.itemMeta = skullMeta
    return skull
}