package me.sd_master92.customvoting.commands.migrate

import com.github.shynixn.mccoroutine.bukkit.launch
import me.sd_master92.core.command.SimpleSubCommand
import me.sd_master92.core.inventory.ConfirmGUI
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.database.PlayerTable
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class MigrateToPlayerFilesCommand(private val plugin: CV) : SimpleSubCommand("database")
{
    override suspend fun onCommand(sender: CommandSender, args: Array<out String>)
    {
    }

    override suspend fun onCommand(player: Player, args: Array<out String>)
    {
        if (args.size > 1)
        {
            val to = args[1].replace("to=", "").replace("[", "").replace("]", "")

            if (plugin.hasDatabaseConnection())
            {
                if (to == "playerfiles")
                {
                    if (plugin.hasDatabaseConnection())
                    {
                        object : ConfirmGUI(
                            plugin,
                            PMessage.MIGRATE_VOTES_COMMAND_INVENTORY_NAME_TO_PLAYERFILES_CONFIRM.toString()
                        )
                        {
                            override fun onClose(event: InventoryCloseEvent, player: Player)
                            {
                                SoundType.FAILURE.play(plugin, player)
                                player.closeInventory()
                            }

                            override fun onCancel(event: InventoryClickEvent, player: Player)
                            {
                                SoundType.FAILURE.play(plugin, player)
                                player.closeInventory()
                                player.sendMessage(PMessage.MIGRATE_VOTES_MESSAGE_FAILURE.toString())
                            }

                            override fun onConfirm(event: InventoryClickEvent, player: Player)
                            {
                                SoundType.SUCCESS.play(plugin, player)
                                player.closeInventory()

                                var total = 0
                                var migrated = 0
                                val progress = TaskTimer.repeat(plugin, 20)
                                {
                                    if (total > 0)
                                    {
                                        player.sendMessage(PMessage.GRAY.getColor() + "$migrated of $total migrated")
                                    }
                                }.run()
                                plugin.launch {
                                    try
                                    {
                                        val players = PlayerTable.getAll()
                                        total = players.size
                                        for (existing in players)
                                        {
                                            val voter = VoteFile.getByUuid(plugin, existing.getUuid())
                                            voter.setName(existing.getName())
                                            voter.setVotes(existing.getVotes(), false)
                                            voter.setPower(existing.getPower())
                                            voter.setStreakDaily(existing.getStreakDaily())
                                            migrated++
                                        }
                                        Voter.getTopVoters(plugin, true)
                                        player.sendMessage(
                                            PMessage.MIGRATE_VOTES_MESSAGE_TO_PLAYERFILES_SUCCESS_X.with(
                                                "" + players.size
                                            )
                                        )
                                    } catch (_: Exception)
                                    {
                                        player.sendMessage(PMessage.MIGRATE_VOTES_MESSAGE_FAILURE.toString())
                                    } finally
                                    {
                                        progress.cancel()
                                    }
                                }
                            }
                        }.open(player)
                        SoundType.OPEN.play(plugin, player)
                    } else
                    {
                        player.sendMessage(PMessage.MIGRATE_VOTES_COMMAND_MESSAGE_DATABASE_REQUIRED.toString())
                    }
                } else
                {
                    player.sendMessage(PMessage.MIGRATE_VOTES_COMMAND_USAGE.toString())
                }
            } else
            {
                player.sendMessage(PMessage.DATABASE_ERROR_NOT_FUNCTIONING.toString())
            }
        } else
        {
            player.sendMessage(PMessage.MIGRATE_VOTES_COMMAND_USAGE.toString())
        }
    }

    init
    {
        withPlayer()
    }
}