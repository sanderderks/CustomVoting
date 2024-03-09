package me.sd_master92.customvoting.commands

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.core.errorLog
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.addToInventoryOrDrop
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.items.CrateKeyItem
import me.sd_master92.customvoting.sendText
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GiveCrateKeyCommand(private val plugin: CV) : SimpleCommand(plugin, "givecratekey", false)
{
    override suspend fun onCommand(sender: CommandSender, args: Array<out String>)
    {
        if (args.size > 1)
        {
            try
            {
                val name = args[0]
                val crateId = args[1].toInt()
                val player = Bukkit.getPlayer(name)

                if (player != null)
                {
                    if (CrateKeyItem.exists(plugin, crateId))
                    {
                        SoundType.SUCCESS.play(plugin, player)
                        player.addToInventoryOrDrop(CrateKeyItem(plugin, crateId))
                        player.sendText(plugin, Message.CRATE_KEY_RECEIVED)
                        sender.sendMessage(PMessage.CRATE_MESSAGE_GIVE_KEY.toString())
                    } else
                    {
                        sender.sendMessage(PMessage.CRATE_ERROR_NOT_FOUND.toString())
                    }
                } else
                {
                    sender.sendMessage(PMessage.PLAYER_NAME_UNKNOWN.toString())
                }
            } catch (e: NumberFormatException)
            {
                sender.sendMessage(PMessage.GENERAL_ERROR_INVALID_ARGUMENT_NOT_NUMBER_X.with("crateID"))
            } catch (e: Exception)
            {
                sender.sendMessage(PMessage.CRATE_ERROR_GIVE_KEY.toString())
                plugin.errorLog(PMessage.CRATE_ERROR_GIVE_KEY.toString(), e)
            }
        } else
        {
            sender.sendMessage(PMessage.CRATE_MESSAGE_GIVE_KEY_COMMAND_USAGE.toString())
        }
    }

    override suspend fun onCommand(player: Player, args: Array<out String>)
    {
    }

    init
    {
        withUsage(PMessage.CRATE_MESSAGE_GIVE_KEY_COMMAND_USAGE.toString())
        withNoPermMessage(Message.NO_PERMISSION.getMessage(plugin))
    }
}