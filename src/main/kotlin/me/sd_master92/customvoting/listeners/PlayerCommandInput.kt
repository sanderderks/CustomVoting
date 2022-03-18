package me.sd_master92.customvoting.listeners

import me.sd_master92.core.input.PlayerStringInput
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.ChatColor
import org.bukkit.entity.Player

abstract class PlayerCommandInput(
    private val plugin: CV,
    private val player: Player,
    private val path: String
) : PlayerStringInput(plugin, player)
{
    private var commands: MutableList<String> = plugin.data.getStringList(path)

    abstract fun onCommandReceived()

    override fun onInputReceived(input: String)
    {
        var command = input
        for (forbidden in plugin.config.getStringList(Settings.FORBIDDEN_COMMANDS))
        {
            if (command.lowercase().contains(forbidden))
            {
                player.sendMessage(ChatColor.RED.toString() + "This command is forbidden.")
                return
            }
        }
        if (command.startsWith("/"))
        {
            command = command.substring(1)
        }

        if (commands.contains(command))
        {
            commands.remove(command)
            player.sendMessage(ChatColor.RED.toString() + "Removed /" + command + " from commands")
        } else
        {
            commands.add(command)
            player.sendMessage(ChatColor.GREEN.toString() + "Added /" + command + " to commands")
        }
        plugin.data[path] = commands
        plugin.data.saveConfig()
        onCommandReceived()
        cancel()
    }

    init
    {
        player.sendMessage(ChatColor.GREEN.toString() + "Please enter a command to add or remove from the list")
        player.sendMessage(ChatColor.GREEN.toString() + "(with %PLAYER% as placeholder)")
        player.sendMessage(ChatColor.GRAY.toString() + "Type 'cancel' to go back")
        player.sendMessage("")
        if (commands.isEmpty())
        {
            player.sendMessage(ChatColor.RED.toString() + "There are currently no commands.")
        } else
        {
            player.sendMessage(ChatColor.GRAY.toString() + "Commands:")
            for (command in commands)
            {
                player.sendMessage(ChatColor.GRAY.toString() + "/" + ChatColor.GREEN + command)
            }
        }
    }
}
