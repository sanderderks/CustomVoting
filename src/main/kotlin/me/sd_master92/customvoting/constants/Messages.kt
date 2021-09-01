package me.sd_master92.customvoting.constants

import me.sd_master92.customvoting.Main

enum class Messages(private val path: String)
{
    NO_PERMISSION("no_permission"),
    MUST_BE_PLAYER("must_be_player"),
    INVALID_PLAYER("invalid_player"),
    MONTHLY_RESET("monthly_reset"),
    VOTE_BROADCAST("vote_broadcast"),
    VOTE_LUCKY("lucky_vote"),
    VOTE_COMMAND("vote_command"),
    VOTE_REWARD_PREFIX("vote_rewards.prefix"),
    VOTE_REWARD_DIVIDER("vote_rewards.divider"),
    VOTE_REWARD_MONEY("vote_rewards.money"),
    VOTE_REWARD_XP("vote_rewards.xp"),
    VOTES_COMMAND_SELF("votes_command.self"),
    VOTES_COMMAND_OTHERS("votes_command.others"),
    VOTES_COMMAND_NOT_FOUND("votes_command.not_found"),
    VOTE_TOP_COMMAND_FORMAT("vote_top_command.format"),
    VOTE_TOP_COMMAND_PLAYERS("vote_top_command.players"),
    VOTE_TOP_COMMAND_NOT_FOUND("vote_top_command.not_found"),
    VOTE_TOP_SIGNS_TITLE_SIGN("vote_top_signs.title_sign"),
    VOTE_TOP_SIGNS_PLAYER_SIGNS_FORMAT("vote_top_signs.player_signs.format"),
    VOTE_TOP_SIGNS_PLAYER_SIGNS_NOT_FOUND("vote_top_signs.player_signs.not_found"),
    VOTE_TOP_SIGNS_PLAYER_SIGNS_OUTDATED("vote_top_signs.player_signs.outdated"),
    VOTE_TOP_STANDS_TOP("vote_top_stands.top"),
    VOTE_TOP_STANDS_CENTER("vote_top_stands.center"),
    VOTE_TOP_STANDS_BOTTOM("vote_top_stands.bottom"),
    VOTE_PARTY_UNTIL("vote_party.until"),
    VOTE_PARTY_COUNTDOWN("vote_party.countdown"),
    VOTE_PARTY_COUNTDOWN_ENDING("vote_party.countdown_ending"),
    VOTE_PARTY_START("vote_party.start"),
    VOTE_PARTY_END("vote_party.end"),
    VOTE_STREAK_REACHED("vote_streak.streak_reached");

    fun getMessage(plugin: Main, placeholders: Map<String, String>?): String
    {
        return plugin.messages.getMessage(path, placeholders)
    }

    fun getMessage(plugin: Main): String
    {
        return plugin.messages.getMessage(path)
    }

    fun getMessages(plugin: Main, placeholders: Map<String, String>?): List<String>
    {
        return plugin.messages.getMessages(path, placeholders)
    }

    fun getMessages(plugin: Main): List<String>
    {
        return plugin.messages.getMessages(path)
    }
}