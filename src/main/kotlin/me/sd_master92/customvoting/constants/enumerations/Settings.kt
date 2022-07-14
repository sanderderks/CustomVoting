package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV

enum class Settings(val path: String, private val defaultValue: Any? = null)
{
    MONTHLY_RESET("monthly_reset", false),
    MONTHLY_VOTES("monthly_votes", false),
    USE_SOUND_EFFECTS("sound_effects", true),
    VOTE_PARTY_COUNTDOWN("vote_party_countdown", 30),
    FIREWORK("firework", true),
    VOTE_PARTY("vote_party", true),
    VOTE_PARTY_TYPE("vote_party_type"),
    ITEM_REWARD_TYPE("item_reward_type"),
    VOTES_REQUIRED_FOR_VOTE_PARTY("votes_required_for_vote_party", 10),
    VOTE_REWARD_MONEY("vote_reward_money", 100),
    VOTE_REWARD_EXPERIENCE("vote_reward_xp", 3),
    LUCKY_VOTE("lucky_vote", true),
    LUCKY_VOTE_CHANCE("lucky_vote_chance", 50),
    VOTE_LINK_INVENTORY("vote_link_inventory", false),
    FORBIDDEN_COMMANDS("forbidden_commands", arrayOf("fakevote", "op", "stop", "restart", "reload")),
    INGAME_UPDATES("ingame_updates", true),
    DISABLED_BROADCAST_VOTE("disabled_broadcasts.vote", false),
    DISABLED_BROADCAST_STREAK("disabled_broadcasts.vote_streak", false),
    DISABLED_BROADCAST_OFFLINE("disabled_broadcasts.offline", false),
    DISABLED_BROADCAST_VOTE_PARTY_UNTIL("disabled_broadcasts.vote_party.until", false),
    DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN("disabled_broadcasts.vote_party_countdown", false),
    DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING("disabled_broadcasts.vote_party.countdown_ending", false),
    DISABLED_MESSAGE_ARMOR_STAND("disabled_broadcasts.armor_stand", false),
    DISABLED_MESSAGE_DISABLED_WORLD("disabled_broadcasts.disabled_world", false),
    DISABLED_MESSAGE_VOTE_REMINDER("disabled_broadcasts.vote_reminder", false),
    FIRST_VOTE_BROADCAST_ONLY("first_vote_broadcast_only", false),
    DISABLED_WORLDS("disabled_worlds"),
    ENABLED_OP_GROUPS("enabled_op_groups"),
    USE_DATABASE("use_database", false),
    DATABASE("database"),
    DATABASE_HOST("${DATABASE.path}.host", "localhost"),
    DATABASE_PORT("${DATABASE.path}.port", 3306),
    DATABASE_DATABASE("${DATABASE.path}.database", "customvoting"),
    DATABASE_USER("${DATABASE.path}.user", "root"),
    DATABASE_PASSWORD("${DATABASE.path}.password", "root");

    companion object
    {
        fun initialize(plugin: CV)
        {
            migrate(plugin)
            for (setting in values())
            {
                if (setting.defaultValue != null)
                {
                    if (plugin.config[setting.path] == null)
                    {
                        plugin.config[setting.path] = setting
                    }
                }
            }
            plugin.config.saveConfig()
        }

        private fun migrate(plugin: CV)
        {
            if (plugin.config.contains("monthly_period"))
            {
                plugin.config.set("monthly_votes", plugin.config.get("monthly_period"))
                plugin.config.delete("monthly_period")
            }
        }
    }
}