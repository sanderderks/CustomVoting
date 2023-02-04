package me.sd_master92.customvoting.listeners

import me.sd_master92.core.input.PlayerStringInput
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
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
                PMessage.GENERAL_MESSAGE_LIST_REMOVED_XY.with(
                    permission,
                    PMessage.PERMISSION_REWARDS_UNIT_MULTIPLE.toString()
                )
            )
        } else
        {
            permissions.add(permission)
            player.sendMessage(
                PMessage.GENERAL_MESSAGE_LIST_ADDED_XY.with(
                    permission,
                    PMessage.PERMISSION_REWARDS_UNIT_MULTIPLE.toString()
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
        player.sendMessage(PMessage.GENERAL_MESSAGE_LIST_ALTER_X.with(PMessage.PERMISSION_REWARDS_UNIT.toString()))
        player.sendMessage(PMessage.GENERAL_MESSAGE_CANCEL_BACK.toString())
        player.sendMessage("")
        if (permissions.isEmpty())
        {
            player.sendMessage(PMessage.GENERAL_MESSAGE_LIST_EMPTY_X.with(PMessage.PERMISSION_REWARDS_UNIT_MULTIPLE.toString()))
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