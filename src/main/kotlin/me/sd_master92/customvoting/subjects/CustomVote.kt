package me.sd_master92.customvoting.subjects

import com.vexsoftware.votifier.model.Vote
import me.sd_master92.core.appendWhenTrue
import me.sd_master92.core.file.PlayerFile
import me.sd_master92.customvoting.*
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.ItemRewardType
import me.sd_master92.customvoting.database.PlayerRow
import me.sd_master92.customvoting.helpers.ParticleHelper
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.text.DecimalFormat
import java.util.*

class CustomVote(private val plugin: CV, vote: Vote, private val queued: Boolean = false) : Vote(vote)
{
    private fun forwardVote()
    {
        val player = Bukkit.getPlayer(username)
        if (player == null)
        {
            queue()
        } else if (plugin.config.getStringList(Settings.DISABLED_WORLDS).contains(player.world.name))
        {
            if (!plugin.config.getBoolean(Settings.DISABLED_MESSAGE_DISABLED_WORLD))
            {
                player.sendText(plugin, Messages.DISABLED_WORLD)
            }
            queue()
        } else
        {
            broadcast(player)
            if (plugin.hasDatabaseConnection())
            {
                PlayerRow(plugin, player.uniqueId.toString()).addVote(true)
            } else
            {
                VoteFile(player.uniqueId.toString(), plugin).addVote(true)
            }
            ParticleHelper.shootFirework(plugin, player.location)
            giveRewards(player, player.hasPermission("customvoting.extra"))
            if (plugin.config.getBoolean(Settings.VOTE_PARTY))
            {
                subtractVotesUntilVoteParty()
            }
        }
    }

    private fun queue()
    {
        if (plugin.hasDatabaseConnection() && plugin.playerTable != null)
        {
            PlayerRow(plugin, plugin.playerTable!!.getUuid(username)).addQueue()
        } else
        {
            val playerFile = PlayerFile.getByName(username)
            if (playerFile != null)
            {
                VoteFile(playerFile.uuid, plugin).addQueue(serviceName)
            }
        }
    }

    private fun broadcast(player: Player)
    {
        if (!plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE))
        {
            if (plugin.config.getBoolean(Settings.FIRST_VOTE_BROADCAST_ONLY))
            {
                val last: Long = if (plugin.hasDatabaseConnection())
                {
                    PlayerRow(plugin, player.uniqueId.toString()).last
                } else
                {
                    VoteFile(player.uniqueId.toString(), plugin).last
                }
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

            if (plugin.config.getBoolean(Settings.DISABLED_BROADCAST_OFFLINE) && queued)
            {
                return
            }

            val placeholders = HashMap<String, String>()
            placeholders["%PLAYER%"] = username
            placeholders["%SERVICE%"] = serviceName
            broadcastText(plugin, Messages.VOTE_BROADCAST, placeholders)
        }
    }

    private fun subtractVotesUntilVoteParty()
    {
        if (plugin.data.getLocations(Data.VOTE_PARTY).isNotEmpty())
        {
            val votesRequired = plugin.config.getNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY)
            val votesUntil = votesRequired - plugin.data.getNumber(Data.CURRENT_VOTES)
            if (votesUntil <= 1)
            {
                plugin.data.setNumber(Data.CURRENT_VOTES, 0)
                object : BukkitRunnable()
                {
                    override fun run()
                    {
                        VoteParty(plugin).start()
                    }
                }.runTaskLater(plugin, 40)
            } else
            {
                plugin.data.addNumber(Data.CURRENT_VOTES)
                if (!isAwaitingBroadcast)
                {
                    isAwaitingBroadcast = true
                    object : BukkitRunnable()
                    {
                        override fun run()
                        {
                            val updatedVotesUntil = votesRequired - plugin.data.getNumber(Data.CURRENT_VOTES)
                            if (updatedVotesUntil != votesRequired && !plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE_PARTY_UNTIL))
                            {
                                val placeholders = HashMap<String, String>()
                                placeholders["%VOTES%"] = "" + updatedVotesUntil
                                placeholders["%s%"] = if (updatedVotesUntil == 1) "" else "s"
                                broadcastText(plugin, Messages.VOTE_PARTY_UNTIL, placeholders)
                            }
                            isAwaitingBroadcast = false
                        }
                    }.runTaskLater(plugin, 40)
                }
            }
        }
    }

    private fun giveRewards(player: Player, op: Boolean)
    {
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
            object : BukkitRunnable()
            {
                var i = 40
                override fun run()
                {
                    player.sendActionBar(Messages.VOTE_REWARD_PREFIX.getMessage(plugin) + message)
                    if (i == 0)
                    {
                        cancel()
                    }
                    i--
                }
            }.runTaskTimer(plugin, 0, 1)
        }
    }

    private fun giveItems(player: Player, op: Boolean)
    {
        val path = Data.ITEM_REWARDS.appendWhenTrue(op, Data.OP_REWARDS)
        val typePath = Settings.ITEM_REWARD_TYPE.appendWhenTrue(op, Data.OP_REWARDS)
        val random = plugin.config.getNumber(typePath) != ItemRewardType.ALL_ITEMS.value
        player.addToInventoryOrDrop(plugin.data.getItems(path), random)
    }

    private fun giveMoney(player: Player, op: Boolean): Double
    {
        val economy = CV.ECONOMY
        if (economy != null && economy.hasAccount(player))
        {
            val path = Settings.VOTE_REWARD_MONEY.appendWhenTrue(op, Data.OP_REWARDS)
            val amount = plugin.config.getDouble(path)
            economy.depositPlayer(player, amount)
            return amount
        }
        return 0.0
    }

    private fun giveExperience(player: Player, op: Boolean): Int
    {
        val path = Settings.VOTE_REWARD_EXPERIENCE.appendWhenTrue(op, Data.OP_REWARDS)
        val amount = plugin.config.getNumber(path)
        player.level = player.level + amount
        return amount
    }

    private fun giveLuckyReward(player: Player)
    {
        if (Random().nextInt(100) < plugin.config.getNumber(Settings.LUCKY_VOTE_CHANCE))
        {
            val luckyRewards = plugin.data.getItems(Data.LUCKY_REWARDS)
            if (luckyRewards.isNotEmpty())
            {
                player.addToInventoryOrDrop(luckyRewards, true)
                player.sendMessage(Messages.VOTE_LUCKY.getMessage(plugin))
            }
        }
    }

    private fun executeCommands(player: Player, op: Boolean)
    {
        val path = Data.VOTE_COMMANDS.appendWhenTrue(op, Data.OP_REWARDS)
        for (command in plugin.data.getStringList(path))
        {
            runCommand(plugin, command.replace("%PLAYER%", player.name).withPlaceholders(player))
        }
    }

    private fun giveStreakRewards(player: Player)
    {
        val votes = VoteFile(player, plugin).votes
        if (plugin.data.contains(Data.VOTE_STREAKS + "." + votes))
        {
            if (!plugin.config.getBoolean(Settings.DISABLED_BROADCAST_STREAK))
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
                runCommand(plugin, command.replace("%PLAYER%", player.name).withPlaceholders(player))
            }
            player.addToInventoryOrDrop(plugin.data.getItems("${Data.VOTE_STREAKS}.$votes.${Data.ITEM_REWARDS}"))
        }
    }

    companion object
    {
        private var isAwaitingBroadcast = false

        fun create(plugin: CV, name: String?, service: String?, queued: Boolean = false)
        {
            val vote = Vote(service, name, "0.0.0.0", Date().time.toString())
            CustomVote(plugin, vote, queued)
        }
    }

    init
    {
        forwardVote()
    }
}