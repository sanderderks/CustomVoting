package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV

enum class Messages(private val path: String, private val defaultValue: Any?)
{
    NO_PERMISSION("no_permission", "&cYou do not have permission to perform this command."),
    MUST_BE_PLAYER("must_be_player", "&cYou must be a player to perform this command."),
    INVALID_PLAYER("invalid_player", "&cThat player does not exist."),
    MONTHLY_RESET("monthly_reset", "&d&lAll votes have been reset, because a new month has started!"),
    DISABLED_WORLD("disabled_world", "&cPlease go to another world to receive your rewards."),
    VOTE_BROADCAST("vote_broadcast", "&d%PLAYER% &7just voted at &d%SERVICE%&7!"),
    VOTE_LUCKY("lucky_vote", "&aYou received a &dLucky Reward&a!"),
    VOTE_COMMAND(
        "vote_command", listOf(
            "&bVote for &dServerName &b!",
            "&b---------------",
            "&dVote for SomeWebsite",
            "&avotelink1.com",
            "&dVote for AnotherWebsite",
            "&avotelink2.com",
            "&dVote for AnotherWebsite",
            "&avotelink3.com",
            "&b---------------",
        )
    ),
    VOTE_REWARDS("vote_rewards", null),
    VOTE_REWARD_PREFIX("$VOTE_REWARDS.prefix", "&aReceived: "),
    VOTE_REWARD_DIVIDER("$VOTE_REWARDS.divider", " &a+ "),
    VOTE_REWARD_MONEY("$VOTE_REWARDS.money", "&b\$%MONEY%"),
    VOTE_REWARD_XP("$VOTE_REWARDS.xp", "&b%XP% &aXP level%s%"),

    VOTES_COMMAND("votes_command", null),
    VOTES_COMMAND_SELF("$VOTES_COMMAND.self", "&aYou currently have &b%VOTES% &avote%s%!"),
    VOTES_COMMAND_OTHERS("$VOTES_COMMAND.others", "&b%PLAYER% &acurrently has &b%VOTES% &avote%s%!"),
    VOTES_COMMAND_NOT_FOUND("$VOTES_COMMAND.not_found", "&cThat player does not exist."),

    VOTE_TOP_COMMAND("vote_top_command", null),
    VOTE_TOP_COMMAND_FORMAT(
        "$VOTE_TOP_COMMAND.format",
        listOf("&aTop Voters", "&b---------------", "%PLAYERS%", "&b---------------")
    ),
    VOTE_TOP_COMMAND_PLAYERS("$VOTE_TOP_COMMAND.players", "&d%PLAYER%&7: &b%VOTES%"),
    VOTE_TOP_COMMAND_NOT_FOUND("$VOTE_TOP_COMMAND.not_found", "&cThere are no players to show."),

    VOTE_TOP_SIGNS("vote_top_signs", null),
    VOTE_TOP_SIGNS_TITLE_SIGN("$VOTE_TOP_SIGNS.title_sign", listOf("&4Top Voters", "&c&lof", "&4&lSome Server!", "")),
    VOTE_TOP_SIGNS_PLAYER_SIGNS_FORMAT(
        "$VOTE_TOP_SIGNS.player_signs.format",
        listOf("&4&lTop %NUMBER%:", "&b%PLAYER%", "&d%VOTES% vote%s%", "")
    ),
    VOTE_TOP_SIGNS_PLAYER_SIGNS_NOT_FOUND("$VOTE_TOP_SIGNS.player_signs.not_found", "&cNot found"),
    VOTE_TOP_SIGNS_PLAYER_SIGNS_OUTDATED("$VOTE_TOP_SIGNS.player_signs.outdated", "&cOutdated"),

    VOTE_TOP_STANDS("vote_top_stands", null),
    VOTE_TOP_STANDS_TOP("$VOTE_TOP_STANDS.top", "&a&lTop %TOP%:"),
    VOTE_TOP_STANDS_CENTER("$VOTE_TOP_STANDS.center", "&b%PLAYER%"),
    VOTE_TOP_STANDS_BOTTOM("$VOTE_TOP_STANDS.bottom", "&7Votes: &d%VOTES%"),
    VOTE_TOP_STANDS_DONT("$VOTE_TOP_STANDS.dont", "&cDon't break me!"),

    VOTE_PARTY("vote_party", null),
    VOTE_PARTY_UNTIL("$VOTE_PARTY.until", "&b%VOTES% &avote%s% until the &bVote Party&a!"),
    VOTE_PARTY_COUNTDOWN("$VOTE_PARTY.countdown", "&aVote Party starting in &b%TIME% &aseconds!"),
    VOTE_PARTY_COUNTDOWN_ENDING("$VOTE_PARTY.countdown_ending", "&aVote Party starting in &b%TIME%"),
    VOTE_PARTY_START("$VOTE_PARTY.start", "&aThe Vote Party has begun!"),
    VOTE_PARTY_END("$VOTE_PARTY.end", "&7The Vote Party has ended!"),
    VOTE_PARTY_PIG_KILLED("$VOTE_PARTY.pig_killed", "&dPig killed by &b%KILLER%&d! Only &b%TOGO% &dpig%s% to go!"),

    MILESTONE("milestone", null),
    MILESTONE_REACHED("$MILESTONE.milestone_reached", "&b%PLAYER% &dreached milestone #&b%MILESTONE%&d!"),
    VOTE_REMINDER(
        "vote_reminder",
        listOf("&bYou haven't voted for our server yet today.", "&dUse /vote for nice rewards!")
    );

    fun getMessage(plugin: CV, placeholders: Map<String, String> = HashMap()): String
    {
        return plugin.messages.getMessage(path, placeholders)
    }

    fun getMessages(plugin: CV, placeholders: Map<String, String> = HashMap()): List<String>
    {
        return plugin.messages.getMessages(path, placeholders)
    }

    override fun toString(): String
    {
        return path
    }

    companion object
    {
        fun initialize(plugin: CV)
        {
            migrate(plugin)
            for (message in values())
            {
                if (message.defaultValue != null && plugin.messages[message.path] == null)
                {
                    plugin.messages[message.path] = message.defaultValue
                }
            }
            plugin.messages.saveConfig()
        }

        private fun migrate(plugin: CV)
        {
            val keyMigrations = mapOf(
                Pair("vote_streak", MILESTONE.path),
                Pair("$MILESTONE.streak_reached", MILESTONE_REACHED.path),
            )

            for (migration in keyMigrations)
            {
                if (plugin.messages.contains(migration.key))
                {
                    plugin.messages.set(migration.value, plugin.messages.get(migration.key))
                    plugin.messages.delete(migration.key)
                }
            }

            val valueMigrations = mapOf(
                Pair("%PERIOD%", "%MONTHLY_VOTES%")
            )

            for (key in plugin.messages.getKeys(true))
            {
                val value = plugin.messages.getString(key)
                for (migration in valueMigrations)
                {
                    if (value?.contains(migration.key) == true)
                    {
                        plugin.messages.set(
                            key,
                            value.replace(migration.key, migration.value)
                        )
                    }
                }
            }
            plugin.messages.saveConfig()
        }
    }
}