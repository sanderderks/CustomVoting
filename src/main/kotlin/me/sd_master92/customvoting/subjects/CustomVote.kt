package me.sd_master92.customvoting.subjects

import com.vexsoftware.votifier.model.Vote
import com.vexsoftware.votifier.model.VotifierEvent
import me.sd_master92.customfile.PlayerFile
import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Messages
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.database.PlayerRow
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.text.DecimalFormat
import java.util.*

class CustomVote(private val plugin: Main, vote: Vote) : Vote()
{
    private fun forwardVote()
    {
        val player = Bukkit.getPlayer(username)
        if (player == null)
        {
            queue()
        } else
        {
            if (plugin.hasDatabaseConnection())
            {
                PlayerRow(plugin, player.uniqueId.toString()).addVote(true)
            } else
            {
                VoteFile(player.uniqueId.toString(), plugin).addVote(true)
            }
            broadcast()
            shootFirework(plugin, player.location)
            giveRewards(player)
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
            val playerFile = PlayerFile.getByName(username, plugin)
            if (playerFile != null)
            {
                VoteFile(playerFile.uuid, plugin).addQueue(serviceName)
            }
        }
    }

    private fun broadcast()
    {
        val placeholders = HashMap<String, String>()
        placeholders["%PLAYER%"] = username
        placeholders["%SERVICE%"] = serviceName
        val message = Messages.VOTE_BROADCAST.getMessage(plugin, placeholders)
        plugin.server.broadcastMessage(message)
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
                            if (updatedVotesUntil != votesRequired)
                            {
                                val placeholders = HashMap<String, String>()
                                placeholders["%VOTES%"] = "" + updatedVotesUntil
                                placeholders["%s%"] = if (updatedVotesUntil == 1) "" else "s"
                                plugin.server.broadcastMessage(Messages.VOTE_PARTY_UNTIL.getMessage(plugin, placeholders))
                            }
                            isAwaitingBroadcast = false
                        }
                    }.runTaskLater(plugin, 40)
                }
            }
        }
    }

    private fun giveRewards(player: Player)
    {
        giveItems(player)
        giveLuckyReward(player)
        executeCommands(player)
        giveStreakRewards(player)
        var rewardMessage = ""
        val money = giveMoney(player)
        if (Main.economy != null && money > 0)
        {
            val placeholders = HashMap<String, String>()
            placeholders["%MONEY%"] = DecimalFormat("#.##").format(money)
            rewardMessage += Messages.VOTE_REWARD_MONEY.getMessage(plugin, placeholders)
        }
        val xp = giveExperience(player)
        if (xp > 0)
        {
            val placeholders = HashMap<String, String>()
            placeholders["%XP%"] = "" + xp
            placeholders["%s%"] = if (xp == 1) "" else "s"
            rewardMessage += if (rewardMessage.isEmpty()) "" else Messages.VOTE_REWARD_DIVIDER.getMessage(plugin)
            rewardMessage += Messages.VOTE_REWARD_XP.getMessage(plugin, placeholders)
        }
        val message = rewardMessage
        if (message.isNotEmpty())
        {
            object : BukkitRunnable()
            {
                var i = 40
                override fun run()
                {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            TextComponent(Messages.VOTE_REWARD_PREFIX.getMessage(plugin) + message))
                    if (i == 0)
                    {
                        cancel()
                    }
                    i--
                }
            }.runTaskTimer(plugin, 0, 1)
        }
    }

    private fun giveItems(player: Player)
    {
        for (reward in plugin.data.getItems(Data.ITEM_REWARDS))
        {
            for (item in player.inventory.addItem(reward).values)
            {
                player.world.dropItemNaturally(player.location, item)
            }
        }
    }

    private fun giveMoney(player: Player): Double
    {
        val economy = Main.economy
        if (economy != null && economy.hasAccount(player))
        {
            val amount = plugin.config.getDouble(Settings.VOTE_REWARD_MONEY)
            economy.depositPlayer(player, amount)
            return amount
        }
        return 0.0
    }

    private fun giveExperience(player: Player): Int
    {
        val amount = plugin.config.getNumber(Settings.VOTE_REWARD_EXPERIENCE)
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

    private fun executeCommands(player: Player)
    {
        for (command in plugin.data.getStringList(Data.VOTE_COMMANDS))
        {
            plugin.server.dispatchCommand(plugin.server.consoleSender, command.replace("%PLAYER%",
                    player.name))
        }
    }

    private fun giveStreakRewards(player: Player)
    {
        val votes = VoteFile(player, plugin).votes
        if (plugin.data.contains(Data.VOTE_STREAKS + "." + votes))
        {
            val permissions = plugin.data.getStringList(Data.VOTE_STREAKS + "." + votes + ".permissions")
            if (permissions.isNotEmpty() && Main.permission != null)
            {
                plugin.server.broadcastMessage(ChatColor.AQUA.toString() + player.name + ChatColor.LIGHT_PURPLE.toString() + " reached vote streak #" + ChatColor.AQUA.toString() + votes + ChatColor.LIGHT_PURPLE.toString() + "!")
                for (permission in permissions)
                {
                    if (Main.permission!!.playerAdd(null, player, permission))
                    {
                        val placeholders = HashMap<String, String>()
                        placeholders["%PLAYER%"] = player.name
                        placeholders["%STREAK%"] = "" + votes
                        player.sendMessage(Messages.VOTE_STREAK_REACHED.getMessage(plugin, placeholders))
                    }
                }
            }
        }
    }

    companion object
    {
        private var isAwaitingBroadcast = false
        fun shootFirework(plugin: Main, loc: Location)
        {
            if (plugin.config.getBoolean(Settings.FIREWORK))
            {
                val world = loc.world
                if (world != null)
                {
                    val firework = world.spawnEntity(loc, EntityType.FIREWORK) as Firework
                    val fireworkMeta = firework.fireworkMeta
                    val random = Random()
                    val colors = arrayOf(Color.AQUA, Color.BLUE, Color.FUCHSIA, Color.GREEN, Color.LIME, Color.MAROON,
                            Color.NAVY,
                            Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.TEAL)
                    val fireworkEffects = arrayOf(FireworkEffect.Type.BALL, FireworkEffect.Type.BALL_LARGE,
                            FireworkEffect.Type.BURST, FireworkEffect.Type.STAR)
                    val effect = FireworkEffect.builder()
                            .flicker(random.nextBoolean())
                            .withColor(colors[random.nextInt(colors.size)])
                            .withFade(colors[random.nextInt(colors.size)])
                            .with(fireworkEffects[random.nextInt(fireworkEffects.size)])
                            .trail(random.nextBoolean()).build()
                    fireworkMeta.addEffect(effect)
                    fireworkMeta.power = random.nextInt(2) + 1
                    firework.fireworkMeta = fireworkMeta
                }
            }
        }

        fun create(plugin: Main, name: String?, service: String?)
        {
            val vote = Vote()
            vote.username = name
            vote.serviceName = service
            vote.address = "0.0.0.0"
            val date = Date()
            vote.timeStamp = date.time.toString()
            plugin.server.pluginManager.callEvent(VotifierEvent(vote))
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