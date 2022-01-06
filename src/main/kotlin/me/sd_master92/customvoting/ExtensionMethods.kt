package me.sd_master92.customvoting

import me.clip.placeholderapi.PlaceholderAPI
import me.sd_master92.customvoting.constants.Messages
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun String.appendWhenTrue(value: Boolean, append: String): String
{
    if (value)
    {
        return this + append
    }
    return this
}

fun Boolean.reverseWhenTrue(value: Boolean): Boolean
{
    if (value)
    {
        return !this
    }
    return this
}

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

fun broadcastText(plugin: CV, message: Messages, placeholders: Map<String, String> = HashMap())
{
    plugin.server.broadcastMessage(message.getMessage(plugin, placeholders))
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