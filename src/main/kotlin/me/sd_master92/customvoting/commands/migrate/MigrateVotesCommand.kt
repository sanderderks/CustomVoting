package me.sd_master92.customvoting.commands.migrate

import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.PMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MigrateVotesCommand(plugin: CV) :
    SimpleCommand(
        plugin,
        "migratevotes",
        false,
        MigrateToDatabaseCommand(plugin),
        MigrateToPlayerFilesCommand(plugin)
    )
{
    override suspend fun onCommand(sender: CommandSender, args: Array<out String>)
    {
    }

    override suspend fun onCommand(player: Player, args: Array<out String>)
    {
    }

    init
    {
        withPlayer(Message.MUST_BE_PLAYER.getMessage(plugin))
        withUsage(PMessage.MIGRATE_VOTES_COMMAND_USAGE.toString())
        withNoPermMessage(Message.NO_PERMISSION.getMessage(plugin))
    }
}