package me.sd_master92.customvoting.listeners

import me.sd_master92.core.input.PlayerStringInput
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.constants.enumerations.Strings
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
        for (forbidden in plugin.config.getStringList(Settings.FORBIDDEN_COMMANDS.path))
        {
            if (command.lowercase().contains(forbidden))
            {
                player.sendMessage(Strings.COMMAND_REWARDS_ERROR_FORBIDDEN.toString())
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
            player.sendMessage(
                Strings.GENERAL_MESSAGE_LIST_REMOVED_XY.with(
                    "/$command",
                    Strings.COMMAND_REWARDS_UNIT_MULTIPLE.toString()
                )
            )
        } else
        {
            commands.add(command)
            player.sendMessage(
                Strings.GENERAL_MESSAGE_LIST_ADDED_XY.with(
                    "/$command",
                    Strings.COMMAND_REWARDS_UNIT_MULTIPLE.toString()
                )
            )
        }
        plugin.data[path] = commands
        plugin.data.saveConfig()
        onCommandReceived()
        cancel()
    }

    init
    {
        player.sendMessage(Strings.GENERAL_MESSAGE_LIST_ALTER_X.with(Strings.COMMAND_REWARDS_UNIT.toString()))
        player.sendMessage(Strings.COMMAND_REWARDS_MESSAGE_PLACEHOLDER.toString())
        player.sendMessage(Strings.GENERAL_MESSAGE_CANCEL_BACK.toString())
        player.sendMessage("")
        if (commands.isEmpty())
        {
            player.sendMessage(Strings.GENERAL_MESSAGE_LIST_EMPTY_X.with(Strings.COMMAND_REWARDS_UNIT_MULTIPLE.toString()))
        } else
        {
            player.sendMessage(Strings.COMMAND_REWARDS_MESSAGE_TITLE.toString())
            for (command in commands)
            {
                player.sendMessage(ChatColor.GRAY.toString() + "/" + ChatColor.GREEN + command)
            }
        }
    }
}
