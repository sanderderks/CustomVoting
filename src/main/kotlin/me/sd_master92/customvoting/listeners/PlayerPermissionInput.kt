package me.sd_master92.customvoting.listeners

import me.sd_master92.core.input.PlayerStringInput
import me.sd_master92.customvoting.CV
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
            player.sendMessage(ChatColor.RED.toString() + "Removed /" + permission + " from permissions")
        } else
        {
            permissions.add(permission)
            player.sendMessage(ChatColor.GREEN.toString() + "Added /" + permission + " to permissions")
        }
        plugin.data[path] = permissions
        plugin.data.saveConfig()
        onPermissionReceived()
        cancel()
    }

    init
    {
        player.sendMessage(ChatColor.GREEN.toString() + "Please enter a permission to add or remove from the list")
        player.sendMessage(ChatColor.GRAY.toString() + "Type 'cancel' to go back")
        player.sendMessage("")
        if (permissions.isEmpty())
        {
            player.sendMessage(ChatColor.RED.toString() + "There are currently no permissions.")
        } else
        {
            player.sendMessage(ChatColor.GRAY.toString() + "Permissions:")
            for (permission in permissions)
            {
                player.sendMessage(ChatColor.GRAY.toString() + "/" + ChatColor.GREEN + permission)
            }
        }
    }
}