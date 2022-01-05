package me.sd_master92.customvoting.constants

import me.sd_master92.customvoting.CV

object Settings
{
    const val MONTHLY_RESET = "monthly_reset"
    const val MONTHLY_PERIOD = "monthly_period"
    const val USE_SOUND_EFFECTS = "sound_effects"
    const val VOTE_PARTY_COUNTDOWN = "vote_party_countdown"
    const val FIREWORK = "firework"
    const val VOTE_PARTY = "vote_party"
    const val VOTE_PARTY_TYPE = "vote_party_type"
    const val ITEM_REWARD_TYPE = "item_reward_type"
    const val VOTES_REQUIRED_FOR_VOTE_PARTY = "votes_required_for_vote_party"
    const val VOTE_REWARD_MONEY = "vote_reward_money"
    const val VOTE_REWARD_EXPERIENCE = "vote_reward_xp"
    const val LUCKY_VOTE = "lucky_vote"
    const val LUCKY_VOTE_CHANCE = "lucky_vote_chance"
    const val VOTE_LINK_INVENTORY = "vote_link_inventory"
    const val FORBIDDEN_COMMANDS = "forbidden_commands"
    const val INGAME_UPDATES = "ingame_updates"
    const val DISABLED_BROADCAST_VOTE = "disabled_broadcasts.vote"
    const val DISABLED_BROADCAST_STREAK = "disabled_broadcasts.vote_streak"
    const val DISABLED_BROADCAST_OFFLINE = "disabled_broadcasts.offline"
    const val DISABLED_BROADCAST_ARMOR_STAND = "disabled_broadcasts.armor_stand"
    const val DISABLED_BROADCAST_VOTE_PARTY_UNTIL = "disabled_broadcasts.vote_party.until"
    const val DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN = "disabled_broadcasts.vote_party_countdown"
    const val DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING = "disabled_broadcasts.vote_party.countdown_ending"
    const val FIRST_VOTE_BROADCAST_ONLY = "first_vote_broadcast_only"
    const val USE_DATABASE = "use_database"
    const val DATABASE = "database"
    private const val DATABASE_HOST = "$DATABASE.host"
    private const val DATABASE_PORT = "$DATABASE.port"
    private const val DATABASE_DATABASE = "$DATABASE.database"
    private const val DATABASE_USER = "$DATABASE.user"
    private const val DATABASE_PASSWORD = "$DATABASE.password"
    fun initialize(plugin: CV)
    {
        setDefault(plugin, MONTHLY_RESET, false)
        setDefault(plugin, MONTHLY_PERIOD, false)
        setDefault(plugin, USE_SOUND_EFFECTS, true)
        setDefault(plugin, VOTE_PARTY_COUNTDOWN, 30)
        setDefault(plugin, FIREWORK, true)
        setDefault(plugin, VOTE_PARTY, true)
        setDefault(plugin, VOTES_REQUIRED_FOR_VOTE_PARTY, 10)
        setDefault(plugin, VOTE_REWARD_MONEY, 100)
        setDefault(plugin, VOTE_REWARD_EXPERIENCE, 3)
        setDefault(plugin, LUCKY_VOTE, true)
        setDefault(plugin, LUCKY_VOTE_CHANCE, 50)
        setDefault(plugin, VOTE_LINK_INVENTORY, false)
        setDefault(plugin, FORBIDDEN_COMMANDS, arrayOf("fakevote", "op", "stop", "restart", "reload"))
        setDefault(plugin, INGAME_UPDATES, true)
        setDefault(plugin, DISABLED_BROADCAST_VOTE, false)
        setDefault(plugin, DISABLED_BROADCAST_STREAK, false)
        setDefault(plugin, DISABLED_BROADCAST_OFFLINE, false)
        setDefault(plugin, DISABLED_BROADCAST_ARMOR_STAND, false)
        setDefault(plugin, DISABLED_BROADCAST_VOTE_PARTY_UNTIL, false)
        setDefault(plugin, DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN, false)
        setDefault(plugin, DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING, false)
        setDefault(plugin, USE_DATABASE, false)
        setDefault(plugin, DATABASE_HOST, "localhost")
        setDefault(plugin, DATABASE_PORT, 3306)
        setDefault(plugin, DATABASE_DATABASE, "customvoting")
        setDefault(plugin, DATABASE_USER, "root")
        setDefault(plugin, DATABASE_PASSWORD, "root")
        setDefault(plugin, FIRST_VOTE_BROADCAST_ONLY, false)
        plugin.config.saveConfig()
    }

    private fun setDefault(plugin: CV, path: String, value: Any)
    {
        if (plugin.config[path] == null)
        {
            plugin.config[path] = value
        }
    }
}