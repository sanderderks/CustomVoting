package me.sd_master92.customvoting.constants

import me.sd_master92.customvoting.CV

enum class Messages(private val path: String)
{
    NO_PERMISSION("no_permission"),
    MUST_BE_PLAYER("must_be_player"),
    INVALID_PLAYER("invalid_player"),
    MONTHLY_RESET("monthly_reset"),
    DISABLED_WORLD("disabled_world"),
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
    VOTE_TOP_STANDS_DONT("vote_top_stands.dont"),
    VOTE_PARTY_UNTIL("vote_party.until"),
    VOTE_PARTY_COUNTDOWN("vote_party.countdown"),
    VOTE_PARTY_COUNTDOWN_ENDING("vote_party.countdown_ending"),
    VOTE_PARTY_START("vote_party.start"),
    VOTE_PARTY_END("vote_party.end"),
    VOTE_STREAK_REACHED("vote_streak.streak_reached");

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
            setDefault(plugin, NO_PERMISSION, "&cYou do not have permission to perform this command.")
            setDefault(plugin, MUST_BE_PLAYER, "&cYou must be a player to perform this command.")
            setDefault(plugin, INVALID_PLAYER, "&cThat player does not exist.")

            setDefault(plugin, MONTHLY_RESET, "&d&lAll votes have been reset, because a new month has started!")

            setDefault(plugin, VOTE_BROADCAST, "&d%PLAYER% &7just voted at &d%SERVICE%&7!")
            setDefault(plugin, VOTE_LUCKY, "&aYou received a &dLucky Reward&a!")
            setDefault(plugin, DISABLED_WORLD, "&cPlease go to another world to receive your rewards.")
            setDefault(
                plugin,
                VOTE_COMMAND,
                listOf(
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
            )

            setDefault(plugin, VOTE_REWARD_PREFIX, "&aReceived: ")
            setDefault(plugin, VOTE_REWARD_DIVIDER, " &a+ ")
            setDefault(plugin, VOTE_REWARD_MONEY, "&b\$%MONEY%")
            setDefault(plugin, VOTE_REWARD_XP, "&b%XP% &aXP level%s%")

            setDefault(plugin, VOTES_COMMAND_SELF, "&aYou currently have &b%VOTES% &avote%s%!")
            setDefault(plugin, VOTES_COMMAND_OTHERS, "&b%PLAYER% &acurrently has &b%VOTES% &avote%s%!")
            setDefault(plugin, VOTES_COMMAND_NOT_FOUND, "&cThat player does not exist.")

            setDefault(
                plugin,
                VOTE_TOP_COMMAND_FORMAT,
                listOf("&aTop Voters", "&b---------------", "%PLAYERS%", "&b---------------")
            )
            setDefault(plugin, VOTE_TOP_COMMAND_PLAYERS, "&d%PLAYER%&7: &b%VOTES%")
            setDefault(plugin, VOTE_TOP_COMMAND_NOT_FOUND, "&cThere are no players to show.")

            setDefault(plugin, VOTE_TOP_SIGNS_TITLE_SIGN, listOf("&4Top Voters", "&c&lof", "&4&lSome Server!", ""))
            setDefault(
                plugin,
                VOTE_TOP_SIGNS_PLAYER_SIGNS_FORMAT,
                listOf("&4&lTop %NUMBER%:", "&b%PLAYER%", "&d%VOTES% vote%s%", "")
            )
            setDefault(plugin, VOTE_TOP_SIGNS_PLAYER_SIGNS_NOT_FOUND, "&cNot found")
            setDefault(plugin, VOTE_TOP_SIGNS_PLAYER_SIGNS_OUTDATED, "&cOutdated")
            setDefault(plugin, VOTE_TOP_STANDS_TOP, "&a&lTop %TOP%:")
            setDefault(plugin, VOTE_TOP_STANDS_CENTER, "&b%PLAYER%")
            setDefault(plugin, VOTE_TOP_STANDS_BOTTOM, "&7Votes: &d%VOTES%")
            setDefault(plugin, VOTE_TOP_STANDS_DONT, "&cDon't break me!")

            setDefault(plugin, VOTE_PARTY_UNTIL, "&b%VOTES% &avote%s% until the &bVote Party&a!")
            setDefault(plugin, VOTE_PARTY_COUNTDOWN, "&aVote Party starting in &b%TIME% &aseconds!")
            setDefault(plugin, VOTE_PARTY_COUNTDOWN_ENDING, "&aVote Party starting in &b%TIME%")
            setDefault(plugin, VOTE_PARTY_START, "&aThe Vote Party has begun!")
            setDefault(plugin, VOTE_PARTY_END, "&7The Vote Party has ended!")

            setDefault(plugin, VOTE_STREAK_REACHED, "&b%PLAYER% &dreached vote streak #&b%STREAK%&d!")
            plugin.messages.saveConfig()
        }

        private fun setDefault(plugin: CV, message: Messages, value: Any)
        {
            if (plugin.messages[message.path] == null)
            {
                plugin.messages[message.path] = value
            }
        }
    }
}