package me.sd_master92.customvoting.constants.enumerations

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

enum class Logger(private val color: ChatColor)
{
    OK(ChatColor.GREEN),
    INFO(ChatColor.WHITE),
    WARNING(ChatColor.YELLOW),
    ERROR(ChatColor.RED);

    fun log(message: String, player: CommandSender?)
    {
        player?.sendMessage(color.toString() + "CustomVoting: $message")
    }
}