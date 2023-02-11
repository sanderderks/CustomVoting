package me.sd_master92.customvoting

import me.clip.placeholderapi.PlaceholderAPI
import me.sd_master92.core.file.PlayerFile
import me.sd_master92.core.plugin.CustomPlugin
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.interfaces.Voter
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
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

fun String.replaceIfNotNull(oldValue: String, newValue: String?): String
{
    if (newValue != null)
    {
        return this.replace(oldValue, newValue)
    }
    return this
}

fun String.stripColor(): String
{
    return ChatColor.stripColor(this)!!
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

fun CommandSender?.sendText(plugin: CV, message: Message, placeholders: Map<String, String> = HashMap())
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

fun CommandSender?.sendTexts(plugin: CV, message: Message, placeholders: Map<String, String> = HashMap())
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

fun OfflinePlayer.hasPowerRewards(plugin: CV): Boolean
{
    return this.hasPowerRewardsByGroup(plugin) || this.hasPowerRewardsByUser(plugin)
}

fun OfflinePlayer.hasPowerRewardsByGroup(plugin: CV): Boolean
{
    if (CV.PERMISSION != null)
    {
        try
        {
            for (group in CV.PERMISSION!!.getPlayerGroups(null, this))
            {
                if (plugin.config.getStringList(Setting.ENABLED_PERM_GROUPS.path).contains(group.lowercase()))
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

fun OfflinePlayer.hasPowerRewardsByUser(plugin: CV): Boolean
{
    if (this is Player)
    {
        return Voter.get(plugin, this).power
    }
    return false
}

fun Player.getPlayerFile(plugin: CV): PlayerFile
{
    return if (plugin.config.getBoolean(Setting.UUID_STORAGE.path)) PlayerFile.getByUuid(
        plugin,
        this
    ) else PlayerFile.getByName(plugin, this.name) ?: PlayerFile.getByUuid(plugin, this)
}

fun OfflinePlayer?.getSkull(): ItemStack
{
    val skull = ItemStack(Material.PLAYER_HEAD)
    val skullMeta = skull.itemMeta as SkullMeta
    skullMeta.owningPlayer = this
    if (this != null)
    {
        skullMeta.setDisplayName(
            PMessage.PLAYER_ITEM_NAME_SKULL_X.with(
                this.name ?: PMessage.PLAYER_NAME_UNKNOWN.toString()
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

fun Long.dayDifferenceToday(): Int
{
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)
    return Calendar.getInstance()[Calendar.DAY_OF_YEAR] - calendar[Calendar.DAY_OF_YEAR]
}

fun Locale.setLanguage()
{
    CV.RESOURCE_BUNDLE = ResourceBundle.getBundle("language/messages", this)

    CustomPlugin.SAVE_TEXT = PMessage.GENERAL_VALUE_SAVE.toString()
    CustomPlugin.BACK_TEXT = PMessage.GENERAL_VALUE_BACK.toString()
    CustomPlugin.STATUS_TEXT = PMessage.GENERAL_ITEM_LORE_STATUS.toString()
    CustomPlugin.ON_TEXT = PMessage.GENERAL_VALUE_ON.toString()
    CustomPlugin.OFF_TEXT = PMessage.GENERAL_VALUE_OFF.toString()
}