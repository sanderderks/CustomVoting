package me.sd_master92.customvoting.subjects

import com.github.shynixn.mccoroutine.bukkit.launch
import com.vexsoftware.votifier.model.Vote
import me.sd_master92.core.appendWhenTrue
import me.sd_master92.core.errorLog
import me.sd_master92.core.infoLog
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.*
import me.sd_master92.customvoting.constants.enumerations.*
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.constants.models.VoteSiteUUID
import me.sd_master92.customvoting.helpers.ParticleHelper
import me.sd_master92.customvoting.subjects.voteparty.VoteParty
import me.sd_master92.customvoting.subjects.voteparty.VotePartyChest
import org.bukkit.entity.Player
import java.text.DecimalFormat
import java.time.LocalDate
import java.util.*

class CustomVote(
    private val plugin: CV,
    vote: Vote,
    private var queued: Boolean = false,
    private val silent: Boolean = false
) : Vote(vote)
{
    private var previousLast: Long = 0
    private var votes = 0

    private suspend fun forwardVote()
    {
        val player = username.getPlayer(plugin)
        if (player == null)
        {
            plugin.infoLog(PMessage.QUEUE_MESSAGE_ADD.toString())
            register(true)
        } else if (plugin.config.getStringList(Setting.DISABLED_WORLDS.path).contains(player.world.name))
        {
            if (!plugin.config.getBoolean(Setting.DISABLED_MESSAGE_DISABLED_WORLD.path))
            {
                player.sendText(plugin, Message.DISABLED_WORLD)
            }
            register(true)
        } else
        {
            if (!silent)
            {
                broadcast(player)
            }
            val voter = Voter.get(plugin, player)
            previousLast = voter.getLast()
            votes = voter.getVotes()
            voter.addVote()
            ParticleHelper.shootFirework(plugin, player.location)
            giveRewards(player, player.hasPowerRewards(plugin))
            if (plugin.config.getBoolean(Setting.VOTE_PARTY_ENABLED.path))
            {
                subtractVotesUntilVoteParty()
            }
            register()
        }
    }

    private suspend fun register(queue: Boolean = false)
    {
        if (!queued)
        {
            val voter = Voter.getByName(plugin, username)
            if (voter != null)
            {
                voter.addHistory(VoteSiteUUID(serviceName), queue)
            } else
            {
                plugin.errorLog(PMessage.PLAYER_ERROR_NOT_EXIST_X.with(username))
            }
        }
    }

    private suspend fun broadcast(player: Player)
    {
        if (!plugin.config.getBoolean(Setting.DISABLED_BROADCAST_VOTE.path))
        {
            if (plugin.config.getBoolean(Setting.FIRST_VOTE_BROADCAST_ONLY.path))
            {
                val last = Voter.get(plugin, player).getLast()
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

            val placeholders = HashMap<String, String>()
            placeholders["%PLAYER%"] = username
            placeholders["%SERVICE%"] = serviceName
            plugin.broadcastText(Message.VOTE_BROADCAST_SINGLE, placeholders)
        }
    }

    private fun subtractVotesUntilVoteParty()
    {
        if (VotePartyChest.getAll(plugin).isNotEmpty())
        {
            val votesRequired = plugin.config.getNumber(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path)
            val votesUntil = votesRequired - plugin.data.getNumber(Data.CURRENT_VOTES.path)
            if (votesUntil <= 1)
            {
                plugin.data.setNumber(Data.CURRENT_VOTES.path, 0)
                TaskTimer.delay(plugin, 40) { VoteParty(plugin).start() }.run()
            } else
            {
                plugin.data.addNumber(Data.CURRENT_VOTES.path)
                if (!isAwaitingBroadcast)
                {
                    isAwaitingBroadcast = true
                    TaskTimer.delay(plugin, 40)
                    {
                        val updatedVotesUntil = votesRequired - plugin.data.getNumber(Data.CURRENT_VOTES.path)
                        if (updatedVotesUntil != votesRequired && !plugin.config.getBoolean(Setting.DISABLED_BROADCAST_VOTE_PARTY_UNTIL.path))
                        {
                            val placeholders = HashMap<String, String>()
                            placeholders["%VOTES%"] = "$updatedVotesUntil"
                            placeholders["%s%"] = if (updatedVotesUntil == 1) "" else "s"
                            plugin.broadcastText(Message.VOTE_PARTY_UNTIL, placeholders)
                        }
                        isAwaitingBroadcast = false
                    }.run()
                }
            }
        }
    }

    private suspend fun giveRewards(player: Player, power: Boolean)
    {
        val dayOfWeek = LocalDate.now().getDayOfWeek()
        val doubleRewards = dayOfWeek.isDoubleRewards(plugin, power)

        giveItems(player, power, doubleRewards)
        executeCommands(player, Data.VOTE_COMMANDS.path.appendWhenTrue(power, Data.POWER_REWARDS))
        var rewardMessage = ""
        val money = giveMoney(player, power, doubleRewards)
        if (CV.ECONOMY != null && money > 0)
        {
            val placeholders = HashMap<String, String>()
            placeholders["%MONEY%"] = DecimalFormat("#.##").format(money)
            rewardMessage += Message.VOTE_REWARD_MONEY.getMessage(plugin, placeholders)
        }
        val xp = giveExperience(player, power, doubleRewards)
        if (xp > 0)
        {
            val placeholders = HashMap<String, String>()
            placeholders["%XP%"] = "$xp"
            placeholders["%s%"] = if (xp == 1) "" else "s"
            rewardMessage += if (rewardMessage.isEmpty()) "" else Message.VOTE_REWARD_DIVIDER.getMessage(plugin)
            rewardMessage += Message.VOTE_REWARD_XP.getMessage(plugin, placeholders)
        }
        giveLuckyReward(player)
        giveMilestoneRewards(player)
        giveStreakRewards(player)

        val message = rewardMessage
        if (message.isNotEmpty())
        {
            TaskTimer.repeat(plugin, 1, 0, 40)
            {
                player.sendActionBar(Message.VOTE_REWARD_PREFIX.getMessage(plugin) + message)
            }.run()
        }

        if (doubleRewards)
        {
            val placeholders = HashMap<String, String>()
            placeholders["%PLAYER%"] = username
            placeholders["%DAY%"] = dayOfWeek.getDisplayName(plugin).lowercase()
            plugin.broadcastText(Message.DOUBLE_REWARDS_RECEIVED, placeholders)
        }
    }

    private fun giveItems(player: Player, power: Boolean, doubleRewards: Boolean)
    {
        val path = Data.ITEM_REWARDS.path.appendWhenTrue(power, Data.POWER_REWARDS)
        val typePath = Setting.ITEM_REWARD_TYPE.path.appendWhenTrue(power, Setting.POWER_REWARDS)
        val random = plugin.config.getNumber(typePath) != ItemRewardType.ALL_ITEMS.ordinal
        val items = plugin.data.getItems(path)
        player.addToInventoryOrDrop(items, random)
        if (doubleRewards)
        {
            player.addToInventoryOrDrop(items, random)
        }
    }

    private fun giveMoney(player: Player, power: Boolean, doubleRewards: Boolean): Double
    {
        val economy = CV.ECONOMY
        if (economy != null && economy.hasAccount(player))
        {
            val path = Setting.VOTE_REWARD_MONEY.path.appendWhenTrue(power, Setting.POWER_REWARDS)
            var amount = plugin.config.getDouble(path)

            if (doubleRewards)
            {
                amount *= 2
            }

            economy.depositPlayer(player, amount)
            return amount
        }
        return 0.0
    }

    private fun giveExperience(player: Player, power: Boolean, doubleRewards: Boolean): Int
    {
        val path = Setting.VOTE_REWARD_EXPERIENCE.path.appendWhenTrue(power, Setting.POWER_REWARDS)
        var amount = plugin.config.getNumber(path)

        if (doubleRewards)
        {
            amount *= 2
        }

        player.level += amount
        return amount
    }

    private fun giveLuckyReward(player: Player)
    {
        if (plugin.config.getBoolean(Setting.LUCKY_VOTE.path) && Random().nextInt(100) < plugin.config.getNumber(Setting.LUCKY_VOTE_CHANCE.path))
        {
            val luckyRewards = plugin.data.getItems(Data.LUCKY_REWARDS.path)
            if (luckyRewards.isNotEmpty())
            {
                player.addToInventoryOrDrop(luckyRewards, true)
                player.sendMessage(Message.VOTE_LUCKY.getMessage(plugin))
            }
            executeCommands(player, Data.LUCKY_COMMANDS.path)
        }
    }

    private fun executeCommands(player: Player, dataPath: String)
    {
        val commands = plugin.data.getStringList(dataPath)
        for (command in commands)
        {
            plugin.runCommand(command.replace("%PLAYER%", player.name).withPlaceholders(player))
        }
    }

    private suspend fun giveMilestoneRewards(player: Player)
    {
        val votes = Voter.get(plugin, player).getVotes()
        if (plugin.data.contains(Data.MILESTONES.path + ".$votes"))
        {
            if (!plugin.config.getBoolean(Setting.DISABLED_BROADCAST_MILESTONE.path))
            {
                val placeholders = HashMap<String, String>()
                placeholders["%PLAYER%"] = player.name
                placeholders["%MILESTONE%"] = "$votes"
                player.sendText(plugin, Message.MILESTONE_REACHED, placeholders)
            }

            val permissions = plugin.data.getStringList(Data.MILESTONES.path + ".$votes.permissions")
            if (CV.PERMISSION != null)
            {
                for (permission in permissions)
                {
                    CV.PERMISSION!!.playerAdd(null, player, permission)
                }
            }
            val commands = plugin.data.getStringList(Data.MILESTONES.path + ".$votes.commands")
            for (command in commands)
            {
                plugin.runCommand(command.replace("%PLAYER%", player.name).withPlaceholders(player))
            }
            player.addToInventoryOrDrop(plugin.data.getItems("${Data.MILESTONES}.$votes.${Data.ITEM_REWARDS}"))
        }
    }

    private suspend fun giveStreakRewards(player: Player)
    {
        val voter = Voter.get(plugin, player)
        val streak = voter.getStreakDaily()
        if (plugin.data.contains(Data.STREAKS.path + ".$streak"))
        {
            if (previousLast.dayDifferenceToday() == 1 || votes == 0)
            {
                if (!plugin.config.getBoolean(Setting.DISABLED_BROADCAST_STREAK.path))
                {
                    val placeholders = HashMap<String, String>()
                    placeholders["%PLAYER%"] = player.name
                    placeholders["%STREAK%"] = "$streak"
                    player.sendText(plugin, Message.STREAK_REACHED, placeholders)
                }

                val permissions = plugin.data.getStringList(Data.STREAKS.path + ".$streak.permissions")
                if (CV.PERMISSION != null)
                {
                    for (permission in permissions)
                    {
                        CV.PERMISSION!!.playerAdd(null, player, permission)
                    }
                }
                val commands = plugin.data.getStringList(Data.STREAKS.path + ".$streak.commands")
                for (command in commands)
                {
                    plugin.runCommand(command.replace("%PLAYER%", player.name).withPlaceholders(player))
                }
                player.addToInventoryOrDrop(plugin.data.getItems("${Data.STREAKS}.$streak.${Data.ITEM_REWARDS}"))
            }
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
            silent: Boolean = false
        )
        {
            val vote = Vote(service, name, "0.0.0.0", Date().time.toString())
            CustomVote(plugin, vote, queued, silent)
        }
    }

    init
    {
        val delay = plugin.config.getNumber(Setting.VOTE_DELAY.path)
        TaskTimer.delay(plugin, delay * 20L) {
            plugin.launch {
                forwardVote()
            }
        }.run()
    }
}