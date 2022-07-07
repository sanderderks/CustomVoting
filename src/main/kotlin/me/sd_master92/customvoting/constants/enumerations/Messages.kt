package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV

enum class Messages(private val path: String, private val defaultValue: Any)
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
    VOTE_REWARD_PREFIX("vote_rewards.prefix", "&aReceived: "),
    VOTE_REWARD_DIVIDER("vote_rewards.divider", " &a+ "),
    VOTE_REWARD_MONEY("vote_rewards.money", "&b\$%MONEY%"),
    VOTE_REWARD_XP("vote_rewards.xp", "&b%XP% &aXP level%s%"),
    VOTES_COMMAND_SELF("votes_command.self", "&aYou currently have &b%VOTES% &avote%s%!"),
    VOTES_COMMAND_OTHERS("votes_command.others", "&b%PLAYER% &acurrently has &b%VOTES% &avote%s%!"),
    VOTES_COMMAND_NOT_FOUND("votes_command.not_found", "&cThat player does not exist."),
    VOTE_TOP_COMMAND_FORMAT(
        "vote_top_command.format",
        listOf("&aTop Voters", "&b---------------", "%PLAYERS%", "&b---------------")
    ),
    VOTE_TOP_COMMAND_PLAYERS("vote_top_command.players", "&d%PLAYER%&7: &b%VOTES%"),
    VOTE_TOP_COMMAND_NOT_FOUND("vote_top_command.not_found", "&cThere are no players to show."),
    VOTE_TOP_SIGNS_TITLE_SIGN("vote_top_signs.title_sign", listOf("&4Top Voters", "&c&lof", "&4&lSome Server!", "")),
    VOTE_TOP_SIGNS_PLAYER_SIGNS_FORMAT(
        "vote_top_signs.player_signs.format",
        listOf("&4&lTop %NUMBER%:", "&b%PLAYER%", "&d%VOTES% vote%s%", "")
    ),
    VOTE_TOP_SIGNS_PLAYER_SIGNS_NOT_FOUND("vote_top_signs.player_signs.not_found", "&cNot found"),
    VOTE_TOP_SIGNS_PLAYER_SIGNS_OUTDATED("vote_top_signs.player_signs.outdated", "&cOutdated"),
    VOTE_TOP_STANDS_TOP("vote_top_stands.top", "&a&lTop %TOP%:"),
    VOTE_TOP_STANDS_CENTER("vote_top_stands.center", "&b%PLAYER%"),
    VOTE_TOP_STANDS_BOTTOM("vote_top_stands.bottom", "&7Votes: &d%VOTES%"),
    VOTE_TOP_STANDS_DONT("vote_top_stands.dont", "&cDon't break me!"),
    VOTE_PARTY_UNTIL("vote_party.until", "&b%VOTES% &avote%s% until the &bVote Party&a!"),
    VOTE_PARTY_COUNTDOWN("vote_party.countdown", "&aVote Party starting in &b%TIME% &aseconds!"),
    VOTE_PARTY_COUNTDOWN_ENDING("vote_party.countdown_ending", "&aVote Party starting in &b%TIME%"),
    VOTE_PARTY_START("vote_party.start", "&aThe Vote Party has begun!"),
    VOTE_PARTY_END("vote_party.end", "&7The Vote Party has ended!"),
    VOTE_STREAK_REACHED("vote_streak.streak_reached", "&b%PLAYER% &dreached vote streak #&b%STREAK%&d!"),
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

    companion object
    {
        fun initialize(plugin: CV)
        {
            for (message in values())
            {
                if (plugin.messages[message.path] == null)
                {
                    plugin.messages[message.path] = message.defaultValue
                }
            }
            plugin.messages.saveConfig()
        }
    }
}