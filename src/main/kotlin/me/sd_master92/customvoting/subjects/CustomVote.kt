package me.sd_master92.customvoting.subjects

import com.vexsoftware.votifier.model.Vote
import me.sd_master92.core.appendWhenTrue
import me.sd_master92.core.errorLog
import me.sd_master92.core.infoLog
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.*
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.ItemRewardType
import me.sd_master92.customvoting.constants.enumerations.Logger
import me.sd_master92.customvoting.constants.enumerations.Messages
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.helpers.ParticleHelper
import me.sd_master92.customvoting.subjects.voteparty.VoteParty
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.text.DecimalFormat
import java.util.*

class CustomVote(
    private val plugin: CV,
    vote: Vote,
    private val queued: Boolean = false,
    private val logger: CommandSender? = null
) : Vote(vote)
{
    private fun forwardVote()
    {
        Logger.OK.log("Now examining incoming vote of $username...", logger)
        Logger.INFO.log("Website: $serviceName", logger)
        val player = Bukkit.getPlayer(username)
        if (player == null)
        {
            plugin.infoLog("The player is not online, adding vote to queue")
            queue()
            Logger.ERROR.log("End of log", logger)
        } else if (plugin.config.getStringList(Settings.DISABLED_WORLDS.path).contains(player.world.name))
        {
            Logger.INFO.log(
                "The player is in a disabled world called '${player.world.name}', adding vote to queue",
                logger
            )
            if (!plugin.config.getBoolean(Settings.DISABLED_MESSAGE_DISABLED_WORLD.path))
            {
                player.sendText(plugin, Messages.DISABLED_WORLD)
            }
            queue()
            Logger.ERROR.log("End of log", logger)
        } else
        {
            Logger.OK.log("Player online and in an enabled world", logger)
            broadcast(player)
            if (Voter.get(plugin, player).addVote())
            {
                Logger.INFO.log("Added vote to player", logger)
            } else
            {
                Logger.ERROR.log("Could not add vote to player", logger)
            }
            ParticleHelper.shootFirework(plugin, player.location)
            giveRewards(player, player.hasPermissionRewards(plugin))
            if (plugin.config.getBoolean(Settings.VOTE_PARTY.path))
            {
                subtractVotesUntilVoteParty()
            }
        }
    }

    private fun queue()
    {
        val voter = Voter.getByName(plugin, username)
        if (voter != null)
        {
            if (voter.addQueue(serviceName))
            {
                Logger.INFO.log("Added vote to queue", logger)
            } else
            {
                Logger.ERROR.log("Could not add vote to queue", logger)
            }
        } else
        {
            plugin.errorLog("A player with name $username does not exist")
        }
    }

    private fun broadcast(player: Player)
    {
        if (!plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE.path))
        {
            if (plugin.config.getBoolean(Settings.FIRST_VOTE_BROADCAST_ONLY.path))
            {
                val last = Voter.get(plugin, player).last
                val date1 = Calendar.getInstance()
                date1.time = Date(last)
                val date2 = Calendar.getInstance()
                date2.time = Date()
                if (date1.get(Calendar.DAY_OF_YEAR) == date2.get(Calendar.DAY_OF_YEAR) &&
                    date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
                )
                {
                    return
                }
            }

            if (plugin.config.getBoolean(Settings.DISABLED_BROADCAST_OFFLINE.path) && queued)
            {
                return
            }

            val placeholders = HashMap<String, String>()
            placeholders["%PLAYER%"] = username
            placeholders["%SERVICE%"] = serviceName
            plugin.broadcastText(Messages.VOTE_BROADCAST, placeholders)
        }
    }

    private fun subtractVotesUntilVoteParty()
    {
        if (plugin.data.getLocations(Data.VOTE_PARTY).isNotEmpty())
        {
            val votesRequired = plugin.config.getNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY.path)
            val votesUntil = votesRequired - plugin.data.getNumber(Data.CURRENT_VOTES)
            if (votesUntil <= 1)
            {
                plugin.data.setNumber(Data.CURRENT_VOTES, 0)
                TaskTimer.delay(plugin, 40) { VoteParty(plugin).start() }.run()
            } else
            {
                plugin.data.addNumber(Data.CURRENT_VOTES)
                if (!isAwaitingBroadcast)
                {
                    isAwaitingBroadcast = true
                    TaskTimer.delay(plugin, 40)
                    {
                        val updatedVotesUntil = votesRequired - plugin.data.getNumber(Data.CURRENT_VOTES)
                        if (updatedVotesUntil != votesRequired && !plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE_PARTY_UNTIL.path))
                        {
                            val placeholders = HashMap<String, String>()
                            placeholders["%VOTES%"] = "" + updatedVotesUntil
                            placeholders["%s%"] = if (updatedVotesUntil == 1) "" else "s"
                            plugin.broadcastText(Messages.VOTE_PARTY_UNTIL, placeholders)
                        }
                        isAwaitingBroadcast = false
                    }.run()
                }
            }
        }
    }

    private fun giveRewards(player: Player, op: Boolean)
    {
        if (op)
        {
            Logger.WARNING.log("Giving PERMISSION BASED rewards to player", logger)
            Logger.INFO.log("^ This happens because the player has the permission 'customvoting.extra'", logger)
        } else
        {
            Logger.INFO.log("Giving regular rewards to player", logger)
        }
        giveItems(player, op)
        executeCommands(player, op)
        var rewardMessage = ""
        val money = giveMoney(player, op)
        if (CV.ECONOMY != null && money > 0)
        {
            val placeholders = HashMap<String, String>()
            placeholders["%MONEY%"] = DecimalFormat("#.##").format(money)
            rewardMessage += Messages.VOTE_REWARD_MONEY.getMessage(plugin, placeholders)
        }
        val xp = giveExperience(player, op)
        if (xp > 0)
        {
            val placeholders = HashMap<String, String>()
            placeholders["%XP%"] = "" + xp
            placeholders["%s%"] = if (xp == 1) "" else "s"
            rewardMessage += if (rewardMessage.isEmpty()) "" else Messages.VOTE_REWARD_DIVIDER.getMessage(plugin)
            rewardMessage += Messages.VOTE_REWARD_XP.getMessage(plugin, placeholders)
        }
        giveLuckyReward(player)
        giveStreakRewards(player)

        val message = rewardMessage
        if (message.isNotEmpty())
        {
            TaskTimer.repeat(plugin, 1, 0, 40)
            {
                player.sendActionBar(Messages.VOTE_REWARD_PREFIX.getMessage(plugin) + message)
            }.run()
        }
    }

    private fun giveItems(player: Player, op: Boolean)
    {
        val path = Data.ITEM_REWARDS.appendWhenTrue(op, Data.OP_REWARDS)
        val typePath = Settings.ITEM_REWARD_TYPE.path.appendWhenTrue(op, Data.OP_REWARDS)
        val random = plugin.config.getNumber(typePath) != ItemRewardType.ALL_ITEMS.value
        val items = plugin.data.getItems(path)
        Logger.INFO.log("Giving ${items.size} items to player", logger)
        player.addToInventoryOrDrop(items, random)
    }

    private fun giveMoney(player: Player, op: Boolean): Double
    {
        val economy = CV.ECONOMY
        if (economy != null && economy.hasAccount(player))
        {
            val path = Settings.VOTE_REWARD_MONEY.path.appendWhenTrue(op, Data.OP_REWARDS)
            val amount = plugin.config.getDouble(path)
            Logger.INFO.log("Giving $$amount to player", logger)
            economy.depositPlayer(player, amount)
            return amount
        } else
        {
            Logger.WARNING.log("Money reward disabled", logger)
        }
        return 0.0
    }

    private fun giveExperience(player: Player, op: Boolean): Int
    {
        val path = Settings.VOTE_REWARD_EXPERIENCE.path.appendWhenTrue(op, Data.OP_REWARDS)
        val amount = plugin.config.getNumber(path)
        Logger.INFO.log("Giving ${amount}xp to player", logger)
        player.level = player.level + amount
        return amount
    }

    private fun giveLuckyReward(player: Player)
    {
        if (Random().nextInt(100) < plugin.config.getNumber(Settings.LUCKY_VOTE_CHANCE.path))
        {
            val luckyRewards = plugin.data.getItems(Data.LUCKY_REWARDS)
            if (luckyRewards.isNotEmpty())
            {
                Logger.OK.log("Giving 1 random lucky reward to player", logger)
                player.addToInventoryOrDrop(luckyRewards, true)
                player.sendMessage(Messages.VOTE_LUCKY.getMessage(plugin))
            }
        }
    }

    private fun executeCommands(player: Player, op: Boolean)
    {
        val path = Data.VOTE_COMMANDS.appendWhenTrue(op, Data.OP_REWARDS)
        val commands = plugin.data.getStringList(path)
        Logger.INFO.log("Executing ${commands.size} commands", logger)
        for (command in commands)
        {
            plugin.runCommand(command.replace("%PLAYER%", player.name).withPlaceholders(player))
        }
    }

    private fun giveStreakRewards(player: Player)
    {
        val votes = VoteFile.get(plugin, player).votes
        if (plugin.data.contains(Data.VOTE_STREAKS + "." + votes))
        {
            Logger.OK.log("Player reached vote streak #$votes, giving streak rewards", logger)
            if (!plugin.config.getBoolean(Settings.DISABLED_BROADCAST_STREAK.path))
            {
                val placeholders = HashMap<String, String>()
                placeholders["%PLAYER%"] = player.name
                placeholders["%STREAK%"] = "" + votes
                player.sendText(plugin, Messages.VOTE_STREAK_REACHED, placeholders)
            }

            val permissions = plugin.data.getStringList(Data.VOTE_STREAKS + "." + votes + ".permissions")
            if (CV.PERMISSION != null)
            {
                for (permission in permissions)
                {
                    CV.PERMISSION!!.playerAdd(null, player, permission)
                }
            }
            val commands = plugin.data.getStringList(Data.VOTE_STREAKS + "." + votes + ".commands")
            for (command in commands)
            {
                plugin.runCommand(command.replace("%PLAYER%", player.name).withPlaceholders(player))
            }
            player.addToInventoryOrDrop(plugin.data.getItems("${Data.VOTE_STREAKS}.$votes.${Data.ITEM_REWARDS}"))
        }
    }

    companion object
    {
        private var isAwaitingBroadcast = false

        fun create(
            plugin: CV,
            name: String,
            service: String,
            queued: Boolean = false,
            logger: CommandSender? = null
        )
        {
            val vote = Vote(service, name, "0.0.0.0", Date().time.toString())
            CustomVote(plugin, vote, queued, logger)
        }
    }

    init
    {
        forwardVote()
    }
}