package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV

enum class Setting(val path: String, private val defaultValue: Any? = null)
{
    LANGUAGE("language", 0),
    VOTES_SORT_TYPE("votes_sort_type", 0),
    USE_SOUND_EFFECTS("sound_effects", true),
    VOTE_PARTY_COUNTDOWN("vote_party_countdown", 30),
    FIREWORK("firework", true),
    VOTE_PARTY_TYPE("vote_party_type"),
    VOTE_PARTY("vote_party"),
    VOTE_PARTY_ENABLED("$VOTE_PARTY.enabled", true),
    VOTE_PARTY_PIG_HUNT("$VOTE_PARTY.pig_hunt"),
    VOTE_PARTY_PIG_HUNT_HEALTH("$VOTE_PARTY_PIG_HUNT.health", 20),
    VOTE_PARTY_PIG_HUNT_DAMAGE_MAX("$VOTE_PARTY_PIG_HUNT.max_damage", 2),
    ITEM_REWARD_TYPE("item_reward_type"),
    VOTES_REQUIRED_FOR_VOTE_PARTY("votes_required_for_vote_party", 10),
    VOTE_REWARD_MONEY("vote_reward_money", 100),
    VOTE_REWARD_EXPERIENCE("vote_reward_xp", 3),
    LUCKY_VOTE("lucky_vote", true),
    LUCKY_VOTE_CHANCE("lucky_vote_chance", 50),
    VOTE_LINK_INVENTORY("vote_link_inventory", false),
    VOTE_INFO_INVENTORY("vote_info_inventory", false),
    FORBIDDEN_COMMANDS("forbidden_commands", arrayOf("fakevote", "op", "stop", "restart", "reload")),
    LAST_VOTE_RESET_MONTH("vote_reset.last_month", -1),
    LAST_VOTE_RESET_WEEK("vote_reset.last_week", -1),
    LAST_VOTE_RESET_DAY("vote_reset.last_day", -1),
    INGAME_UPDATES("ingame_updates", true),
    ALLOW_CRATE_CLOSE("allow_crate_close", false),
    VOTE_COMMAND_OVERRIDE("vote_command_override", false),
    VOTE_STREAK_CONSECUTIVE("vote_streak_consecutive", true),
    VOTE_DELAY("vote_delay", 0),

    DISABLED_BROADCASTS("disabled_broadcasts"),
    DISABLED_BROADCAST_VOTE("$DISABLED_BROADCASTS.vote", false),
    DISABLED_BROADCAST_MILESTONE("$DISABLED_BROADCASTS.milestone", false),
    DISABLED_BROADCAST_STREAK("$DISABLED_BROADCASTS.streak", false),
    DISABLED_BROADCAST_OFFLINE("$DISABLED_BROADCASTS.offline", false),
    DISABLED_BROADCAST_VOTE_PARTY_UNTIL("$DISABLED_BROADCASTS.vote_party.until", false),
    DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN("$DISABLED_BROADCASTS.vote_party_countdown", false),
    DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING("$DISABLED_BROADCASTS.vote_party.countdown_ending", false),
    DISABLED_MESSAGE_ARMOR_STAND("$DISABLED_BROADCASTS.armor_stand", false),
    DISABLED_MESSAGE_DISABLED_WORLD("$DISABLED_BROADCASTS.disabled_world", false),
    DISABLED_MESSAGE_VOTE_REMINDER("$DISABLED_BROADCASTS.vote_reminder", false),

    DOUBLE_REWARDS("double_rewards", null),
    DOUBLE_REWARDS_REGULAR("$DOUBLE_REWARDS.regular", null),
    DOUBLE_REWARDS_POWER("$DOUBLE_REWARDS.power", null),

    FIRST_VOTE_BROADCAST_ONLY("first_vote_broadcast_only", false),
    WORLD_EXCLUSION("excluded_worlds", null),
    WORLD_EXCLUSION_LIST("$WORLD_EXCLUSION.list"),
    WORLD_EXCLUSION_TYPE("$WORLD_EXCLUSION.type"),
    ENABLED_PERM_GROUPS("enabled_op_groups"),

    UUID_STORAGE("uuid_storage", true),
    PREFIX_SUPPORT("prefix_support"),
    IGNORE_PLAYERNAME_CASING("ignore_playername_casing", false),

    USE_DATABASE("use_database", false),

    DATABASE("database"),
    DATABASE_HOST("$DATABASE.host", "localhost"),
    DATABASE_PORT("$DATABASE.port", 3306),
    DATABASE_DATABASE("$DATABASE.database", "customvoting"),
    DATABASE_USER("$DATABASE.user", "root"),
    DATABASE_PASSWORD("$DATABASE.password", "root"),

    SETTINGS_ENABLED("vote_settings_enabled", true);

    override fun toString(): String
    {
        return path
    }

    companion object
    {
        const val POWER_REWARDS = "_op"

        fun initialize(plugin: CV)
        {
            migrate(plugin)
            for (setting in entries)
            {
                if (setting.defaultValue != null && plugin.config[setting.path] == null)
                {
                    plugin.config[setting.path] = setting.defaultValue
                }
            }
            plugin.config.saveConfig()
        }

        private fun migrate(plugin: CV)
        {
            val keyMigrations = mapOf(
                Pair("disabled_broadcasts.vote_streak", DISABLED_BROADCAST_MILESTONE.path),
                Pair("monthly_period", "monthly_votes"),
                Pair("suffix_support", PREFIX_SUPPORT.path),
                Pair("disabled_worlds", WORLD_EXCLUSION_LIST.path)
            )

            plugin.config.keyMigrations(keyMigrations)
        }
    }
}