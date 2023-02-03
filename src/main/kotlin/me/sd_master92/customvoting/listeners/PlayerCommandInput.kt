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
                player.sendMessage(Strings.INPUT_COMMAND_FORBIDDEN.toString())
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
                Strings.INPUT_REMOVED_FROM_LIST_XY.with(
                    "/$command",
                    Strings.INPUT_COMMAND_TYPE_MULTIPLE.toString()
                )
            )
        } else
        {
            commands.add(command)
            player.sendMessage(
                Strings.INPUT_ADDED_TO_LIST_XY.with(
                    "/$command",
                    Strings.INPUT_COMMAND_TYPE_MULTIPLE.toString()
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
        player.sendMessage(Strings.INPUT_ALTER_LIST_X.with(Strings.INPUT_COMMAND_TYPE.toString()))
        player.sendMessage(Strings.INPUT_COMMANDS_ADD_LORE.toString())
        player.sendMessage(Strings.INPUT_CANCEL_BACK.toString())
        player.sendMessage("")
        if (commands.isEmpty())
        {
            player.sendMessage(Strings.INPUT_LIST_EMPTY_X.with(Strings.INPUT_COMMAND_TYPE_MULTIPLE.toString()))
        } else
        {
            player.sendMessage(Strings.INPUT_COMMANDS.toString())
            for (command in commands)
            {
                player.sendMessage(ChatColor.GRAY.toString() + "/" + ChatColor.GREEN + command)
            }
        }
    }
}
