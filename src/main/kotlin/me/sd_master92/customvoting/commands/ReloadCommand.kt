package me.sd_master92.customvoting.commands

import me.sd_master92.core.appendWhenTrue
import me.sd_master92.core.command.SimpleCommand
import me.sd_master92.core.file.PlayerFile
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.subjects.VoteTopSign
import me.sd_master92.customvoting.subjects.VoteTopStand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReloadCommand(private val plugin: CV) : SimpleCommand(plugin, "votereload")
{
    override fun onCommand(sender: CommandSender, args: Array<String>)
    {
        val cache = args.isNotEmpty() && args[0] == "cache"
        sender.sendMessage(Strings.RELOAD_START_X.with("".appendWhenTrue(cache, " and cache")))
        if (reload(plugin, cache))
        {
            sender.sendMessage(Strings.RELOAD_FINISH_X.with("".appendWhenTrue(cache, " and cache")))
        } else
        {
            sender.sendMessage(Strings.RELOAD_FAIL.toString())
        }
    }

    override fun onCommand(player: Player, args: Array<String>)
    {
    }

    companion object
    {
        fun reload(plugin: CV, init: Boolean? = false): Boolean
        {
            if (plugin.config.reloadConfig() && plugin.data.reloadConfig() && plugin.messages.reloadConfig())
            {
                if (init == true)
                {
                    Voter.init(plugin)
                }
                if (PlayerFile.getAll().values.stream().allMatch { obj: PlayerFile -> obj.reloadConfig() })
                {
                    VoteTopSign.updateAll(plugin)
                    VoteTopStand.updateAll(plugin)
                    return true
                }
            }
            return false
        }
    }

    init
    {
        withNoPermMessage(Messages.NO_PERMISSION.getMessage(plugin))
    }
}