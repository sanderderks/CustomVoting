package me.sd_master92.customvoting.listeners

import me.sd_master92.core.input.PlayerStringInput
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Strings
import org.bukkit.ChatColor
import org.bukkit.entity.Player

abstract class PlayerPermissionInput(
    private val plugin: CV,
    private val player: Player,
    private val path: String
) : PlayerStringInput(plugin, player)
{
    private var permissions: MutableList<String> = plugin.data.getStringList(path)

    abstract fun onPermissionReceived()

    override fun onInputReceived(input: String)
    {
        var permission = input
        if (permission.startsWith("/"))
        {
            permission = permission.substring(1)
        }

        if (permissions.contains(permission))
        {
            permissions.remove(permission)
            player.sendMessage(
                Strings.INPUT_REMOVED_FROM_LIST_XY.with(
                    permission,
                    Strings.INPUT_PERMISSION_TYPE_MULTIPLE.toString()
                )
            )
        } else
        {
            permissions.add(permission)
            player.sendMessage(
                Strings.INPUT_ADDED_TO_LIST_XY.with(
                    permission,
                    Strings.INPUT_PERMISSION_TYPE_MULTIPLE.toString()
                )
            )
        }
        plugin.data[path] = permissions
        plugin.data.saveConfig()
        onPermissionReceived()
        cancel()
    }

    init
    {
        player.sendMessage(Strings.INPUT_ALTER_LIST_X.with(Strings.INPUT_PERMISSION_TYPE.toString()))
        player.sendMessage(Strings.INPUT_CANCEL_BACK.toString())
        player.sendMessage("")
        if (permissions.isEmpty())
        {
            player.sendMessage(Strings.INPUT_LIST_EMPTY_X.with(Strings.INPUT_PERMISSION_TYPE_MULTIPLE.toString()))
        } else
        {
            player.sendMessage(ChatColor.GRAY.toString() + "Permissions:")
            for (permission in permissions)
            {
                player.sendMessage(ChatColor.GREEN.toString() + permission)
            }
        }
    }
}