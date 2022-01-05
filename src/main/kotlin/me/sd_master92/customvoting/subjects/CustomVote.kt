package me.sd_master92.customvoting.subjects

import com.vexsoftware.votifier.model.Vote
import me.clip.placeholderapi.PlaceholderAPI
import me.sd_master92.customfile.PlayerFile
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.ItemRewardType
import me.sd_master92.customvoting.database.PlayerRow
import me.sd_master92.customvoting.helpers.ParticleHelper
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.text.DecimalFormat
import java.util.*

class CustomVote(private val plugin: CV, vote: Vote, private val queued: Boolean = false) : Vote()
{
    private fun forwardVote()
    {
        val player = Bukkit.getPlayer(username)
        if (player == null)
        {
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
            val message = Messages.VOTE_BROADCAST.getMessage(plugin, placeholders)
            plugin.server.broadcastMessage(message)
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
                                plugin.server.broadcastMessage(
                                    Messages.VOTE_PARTY_UNTIL.getMessage(
                                        plugin,
                                        placeholders
                                    )
                                )
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
                    player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent(Messages.VOTE_REWARD_PREFIX.getMessage(plugin) + message)
                    )
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
        var path = Data.ITEM_REWARDS
        var random = plugin.config.getNumber(Settings.ITEM_REWARD_TYPE) == ItemRewardType.ALL_ITEMS.value
        if (op)
        {
            path += Data.OP_REWARDS
            random =
                plugin.config.getNumber(Settings.ITEM_REWARD_TYPE + "." + Data.OP_REWARDS) == ItemRewardType.ALL_ITEMS.value
        }
        val rewards = plugin.data.getItems(path)
        if (!random)
        {
            for (reward in rewards)
            {
                for (item in player.inventory.addItem(reward).values)
                {
                    player.world.dropItemNaturally(player.location, item)
                }
            }
        } else
        {
            for (item in player.inventory.addItem(rewards[Random().nextInt(rewards.size)]).values)
            {
                player.world.dropItemNaturally(player.location, item)
            }
        }
    }

    private fun giveMoney(player: Player, op: Boolean): Double
    {
        val economy = CV.ECONOMY
        if (economy != null && economy.hasAccount(player))
        {
            var path = Settings.VOTE_REWARD_MONEY
            if (op)
            {
                path += Data.OP_REWARDS
            }
            val amount = plugin.config.getDouble(path)
            economy.depositPlayer(player, amount)
            return amount
        }
        return 0.0
    }

    private fun giveExperience(player: Player, op: Boolean): Int
    {
        var path = Settings.VOTE_REWARD_EXPERIENCE
        if (op)
        {
            path += Data.OP_REWARDS
        }
        val amount = plugin.config.getNumber(path)
        player.level = player.level + amount
        return amount
    }

    private fun giveLuckyReward(player: Player)
    {
        val random = Random()
        var i = random.nextInt(100)
        if (i < plugin.config.getNumber(Settings.LUCKY_VOTE_CHANCE))
        {
            val luckyRewards = plugin.data.getItems(Data.LUCKY_REWARDS)
            if (luckyRewards.isNotEmpty())
            {
                i = random.nextInt(luckyRewards.size)
                for (item in player.inventory.addItem(luckyRewards[i]).values)
                {
                    player.world.dropItemNaturally(player.location, item)
                }
                player.sendMessage(Messages.VOTE_LUCKY.getMessage(plugin))
            }
        }
    }

    private fun executeCommands(player: Player, op: Boolean)
    {
        var path = Data.VOTE_COMMANDS
        if (op)
        {
            path += Data.OP_REWARDS
        }
        for (command in plugin.data.getStringList(path))
        {
            var cmd = command.replace(
                "%PLAYER%",
                player.name
            )
            if (CV.PAPI)
            {
                cmd = PlaceholderAPI.setPlaceholders(player, cmd)
            }
            plugin.server.dispatchCommand(plugin.server.consoleSender, cmd)
        }
    }

    private fun giveStreakRewards(player: Player)
    {
        val votes = VoteFile(player, plugin).votes
        if (plugin.data.contains(Data.VOTE_STREAKS + "." + votes))
        {
            if (!plugin.config.getBoolean(Settings.DISABLED_BROADCAST_STREAK))
            {
                plugin.server.broadcastMessage(ChatColor.AQUA.toString() + player.name + ChatColor.LIGHT_PURPLE.toString() + " reached vote streak #" + ChatColor.AQUA.toString() + votes + ChatColor.LIGHT_PURPLE.toString() + "!")
            }

            val permissions = plugin.data.getStringList(Data.VOTE_STREAKS + "." + votes + ".permissions")
            if (permissions.isNotEmpty() && CV.PERMISSION != null)
            {
                for (permission in permissions)
                {
                    if (CV.PERMISSION!!.playerAdd(null, player, permission))
                    {
                        val placeholders = HashMap<String, String>()
                        placeholders["%PLAYER%"] = player.name
                        placeholders["%STREAK%"] = "" + votes
                        player.sendMessage(Messages.VOTE_STREAK_REACHED.getMessage(plugin, placeholders))
                    }
                }
            }
            val commands = plugin.data.getStringList(Data.VOTE_STREAKS + "." + votes + ".commands")
            if (commands.isNotEmpty())
            {
                for (command in commands)
                {
                    var cmd = command.replace(
                        "%PLAYER%",
                        player.name
                    )
                    if (CV.PAPI)
                    {
                        cmd = PlaceholderAPI.setPlaceholders(player, cmd)
                    }
                    plugin.server.dispatchCommand(plugin.server.consoleSender, cmd)
                }
            }
            for (reward in plugin.data.getItems("${Data.VOTE_STREAKS}.$votes.${Data.ITEM_REWARDS}"))
            {
                for (item in player.inventory.addItem(reward).values)
                {
                    player.world.dropItemNaturally(player.location, item)
                }
            }
        }
    }

    companion object
    {
        private var isAwaitingBroadcast = false

        fun create(plugin: CV, name: String?, service: String?, queued: Boolean = false)
        {
            val vote = Vote()
            vote.username = name
            vote.serviceName = service
            vote.address = "0.0.0.0"
            val date = Date()
            vote.timeStamp = date.time.toString()

            CustomVote(plugin, vote, queued)
        }
    }

    init
    {
        username = vote.username
        serviceName = vote.serviceName
        address = vote.address
        timeStamp = vote.timeStamp
        forwardVote()
    }
}