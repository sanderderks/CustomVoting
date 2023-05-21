package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.replaceIfNotNull
import org.bukkit.ChatColor
import java.util.*

/**
 * SUBJECT_TYPE\[_NAME]
 * TYPES:
 * - NAME
 * - VALUE
 * - MESSAGE
 * - ERROR (message)
 * - UNIT
 * - FORMAT
 * - ITEM_NAME
 * - ITEM_LORE
 * - INVENTORY_NAME
 */
enum class PMessage(
    private val color: ChatColor? = null,
    private val colorX: ChatColor? = null,
    private val colorY: ChatColor? = null
)
{
    GREEN(ChatColor.GREEN),
    RED(ChatColor.RED),
    PURPLE(ChatColor.LIGHT_PURPLE),
    AQUA(ChatColor.AQUA),
    GRAY(ChatColor.GRAY),
    YELLOW(ChatColor.YELLOW),
    BOLD(ChatColor.BOLD),
    UNDERLINE(ChatColor.UNDERLINE),

    ACTION_ERROR_BREAK_BLOCK_NO_PERMISSION(RED.color),
    ACTION_ERROR_INTERACT_NEED_CRATE(RED.color),
    ACTION_ERROR_OPEN_CHEST_NEED_KEY(RED.color),
    ACTION_ERROR_OPEN_CHEST_NO_PERMISSION(RED.color),
    ACTION_ERROR_OPEN_CHEST_WRONG_KEY(RED.color),

    COMMAND_REWARDS_ERROR_FORBIDDEN(RED.color),
    COMMAND_REWARDS_ITEM_NAME(PURPLE.color),
    COMMAND_REWARDS_ITEM_NAME_VOTE_PARTY(PURPLE.color),
    COMMAND_REWARDS_MESSAGE_PLACEHOLDER(GREEN.color),
    COMMAND_REWARDS_MESSAGE_TITLE(GRAY.color),
    COMMAND_REWARDS_UNIT,
    COMMAND_REWARDS_UNIT_MULTIPLE,

    CRATE_INVENTORY_NAME_OVERVIEW,
    CRATE_ITEM_NAME_OVERVIEW(PURPLE.color),
    CRATE_ITEM_LORE_OVERVIEW(GRAY.color),
    CRATE_INVENTORY_NAME,
    CRATE_INVENTORY_NAME_PLACE_CONFIRM_X,
    CRATE_INVENTORY_NAME_PERC_REWARDS_XY,
    CRATE_ITEM_LORE_KEY_X(GRAY.color),
    CRATE_ITEM_LORE_PLACE(GRAY.color),
    CRATE_ITEM_NAME_ADD(GREEN.color),
    CRATE_ITEM_NAME_KEY_X(AQUA.color),
    CRATE_ITEM_NAME_KEY_GET(AQUA.color),
    CRATE_ITEM_NAME_NO_PRICE(RED.color),
    CRATE_ITEM_NAME_RENAME(PURPLE.color),
    CRATE_ITEM_NAME_REWARDS_PERCENTAGE_X(PURPLE.color),
    CRATE_MESSAGE_NAME_CHANGED_X(GREEN.color),
    CRATE_MESSAGE_NAME_ENTER(GREEN.color),
    CRATE_MESSAGE_NO_PRICE(RED.color),
    CRATE_MESSAGE_REWARD_X(GREEN.color, AQUA.color),
    CRATE_ERROR_EMPTY(RED.color),
    CRATE_NAME_DEFAULT_X(PURPLE.color),
    CRATE_NAME_X(PURPLE.color),
    CRATE_NAME_STAND_X(AQUA.color),

    DANGER_ZONE_INVENTORY_NAME,
    DANGER_ZONE_ITEM_NAME(RED.color),

    DATABASE_VALUE_CONNECTED(GREEN.color),
    DATABASE_VALUE_NOT_FUNCTIONING(RED.color),
    DATABASE_ITEM_NAME(RED.color),
    DATABASE_ITEM_LORE(GRAY.color),

    DISABLED_WORLD_OVERVIEW_INVENTORY_NAME,
    DISABLED_WORLD_OVERVIEW_ITEM_NAME(PURPLE.color),
    DISABLED_WORLD_OVERVIEW_ITEM_LORE(GRAY.color),
    DISABLED_WORLD_ITEM_NAME_X(PURPLE.color),
    DISABLED_WORLD_ITEM_NAME_MESSAGE(PURPLE.color),
    DISABLED_WORLD_ITEM_NAME_OVERWORLD(GREEN.color),
    DISABLED_WORLD_ITEM_NAME_NETHER(RED.color),
    DISABLED_WORLD_ITEM_NAME_END(AQUA.color),
    DISABLED_WORLD_NAME_OVERWORLD,
    DISABLED_WORLD_NAME_NETHER,
    DISABLED_WORLD_NAME_END,

    DISCORD_ITEM_LORE(GRAY.color),
    DISCORD_ITEM_NAME(PURPLE.color),
    DISCORD_MESSAGE(AQUA.color),
    DISCORD_MESSAGE_URL(GREEN.color),

    ENUM_ITEM_REWARD_TYPE_ALL,
    ENUM_ITEM_REWARD_TYPE_RANDOM,
    ENUM_SORT_TYPE_ALL,
    ENUM_SORT_TYPE_MONTHLY,
    ENUM_SORT_TYPE_WEEKLY,
    ENUM_SORT_TYPE_DAILY,
    ENUM_VOTE_PARTY_TYPE_RANDOM,
    ENUM_VOTE_PARTY_TYPE_RANDOM_CHEST_AT_A_TIME,
    ENUM_VOTE_PARTY_TYPE_ALL_CHESTS_AT_ONCE,
    ENUM_VOTE_PARTY_TYPE_ONE_CHEST_AT_A_TIME,
    ENUM_VOTE_PARTY_TYPE_ADD_TO_INVENTORY,
    ENUM_VOTE_PARTY_TYPE_EXPLODE_CHESTS,
    ENUM_VOTE_PARTY_TYPE_SCARY,
    ENUM_VOTE_PARTY_TYPE_PIG_HUNT,

    SUPPORTER_INVENTORY_NAME,
    SUPPORTER_ITEM_LORE(GRAY.color),
    SUPPORTER_ITEM_NAME(PURPLE.color),

    PERM_GROUP_OVERVIEW_INVENTORY_NAME,
    PERM_GROUP_OVERVIEW_ITEM_NAME(PURPLE.color),
    PERM_GROUP_OVERVIEW_ITEM_LORE_X(GRAY.color, GREEN.color),
    PERM_GROUP_ITEM_NAME_X(PURPLE.color),

    PERM_USER_OVERVIEW_INVENTORY_NAME_X,
    PERM_USER_OVERVIEW_ITEM_LORE(GRAY.color),
    PERM_USER_OVERVIEW_ITEM_NAME(PURPLE.color),

    FAKE_VOTE_MESSAGE_COMMAND_USAGE(RED.color),
    FAKE_VOTE_VALUE_WEBSITE,

    FIREWORK_ITEM_NAME(PURPLE.color),

    GENERAL_ERROR(RED.color),
    GENERAL_ERROR_ALREADY_EXIST_X(RED.color),
    GENERAL_ERROR_INVALID_ARGUMENT_NOT_NUMBER_X(RED.color),
    GENERAL_ERROR_INVALID_ARGUMENT_NOT_POSITIVE_X(RED.color),
    GENERAL_ERROR_NOT_EXIST_X(RED.color),
    GENERAL_ITEM_LORE_CURRENT_X(GRAY.color, AQUA.color),
    GENERAL_ITEM_LORE_CURRENT_XY(GRAY.color, AQUA.color),
    GENERAL_ITEM_LORE_DISABLED_X(GRAY.color),
    GENERAL_ITEM_LORE_ENABLED_X(GRAY.color),
    GENERAL_ITEM_LORE_STATUS_X(GRAY.color, AQUA.color),
    GENERAL_ITEM_LORE_STATUS(GRAY.color),
    GENERAL_ITEM_NAME_DELETE(RED.color),
    GENERAL_ITEM_NAME_NEXT(AQUA.color),
    GENERAL_ITEM_NAME_PREVIOUS(AQUA.color),
    GENERAL_ITEM_NAME_REFRESH(RED.color),
    GENERAL_MESSAGE_CANCEL_BACK(GRAY.color),
    GENERAL_MESSAGE_CANCEL_CONTINUE(GRAY.color),
    GENERAL_MESSAGE_CREATE_SUCCESS_X(GREEN.color),
    GENERAL_MESSAGE_DELETE_SUCCESS_X(RED.color),
    GENERAL_MESSAGE_LIST_ADDED_XY(GREEN.color),
    GENERAL_MESSAGE_LIST_ALTER_X(GREEN.color),
    GENERAL_MESSAGE_LIST_EMPTY_X(RED.color),
    GENERAL_MESSAGE_LIST_REMOVED_XY(RED.color),
    GENERAL_MESSAGE_NUMBER_ENTER(GREEN.color),
    GENERAL_MESSAGE_UPDATE_FAIL_X(RED.color),
    GENERAL_MESSAGE_UPDATE_NOTHING_CHANGED(GRAY.color),
    GENERAL_MESSAGE_UPDATE_SUCCESS_X(GREEN.color),
    GENERAL_VALUE_DISABLED(RED.color),
    GENERAL_VALUE_NO(RED.color),
    GENERAL_VALUE_YES(GREEN.color),
    GENERAL_VALUE_FALSE(PURPLE.color),
    GENERAL_VALUE_TRUE(RED.color),
    GENERAL_VALUE_SAVE(GREEN.color),
    GENERAL_VALUE_BACK(RED.color),
    GENERAL_VALUE_CONFIRM(GREEN.color),
    GENERAL_VALUE_CANCEL(RED.color),
    GENERAL_VALUE_ON(GREEN.color),
    GENERAL_VALUE_OFF(RED.color),
    GENERAL_FORMAT_DATE,

    GENERAL_SETTINGS_INVENTORY_NAME,

    ITEM_REWARDS_INVENTORY_NAME,
    ITEM_REWARDS_ITEM_NAME(PURPLE.color),
    ITEM_REWARDS_ITEM_NAME_TYPE(PURPLE.color),
    ITEM_REWARDS_UNIT_STACKS_MULTIPLE,

    LANGUAGE_ITEM_NAME(AQUA.color),
    LANGUAGE_ITEM_LORE_X(GREEN.color),

    LUCKY_INVENTORY_NAME_SETTINGS,
    LUCKY_INVENTORY_NAME_REWARDS,
    LUCKY_ITEM_NAME(PURPLE.color),
    LUCKY_ITEM_NAME_SETTINGS(PURPLE.color),
    LUCKY_ITEM_NAME_CHANCE(PURPLE.color),
    LUCKY_ITEM_NAME_REWARDS(PURPLE.color),
    LUCKY_ITEM_LORE_SETTINGS(GRAY.color),

    MERGE_DUPLICATES_ITEM_LORE(GRAY.color, AQUA.color),
    MERGE_DUPLICATES_ITEM_NAME(RED.color),
    MERGE_DUPLICATES_MESSAGE_DELETED_X(RED.color),

    MESSAGE_SETTINGS_INVENTORY_NAME,

    MILESTONE_INVENTORY_NAME_OVERVIEW,
    MILESTONE_INVENTORY_NAME_X,
    MILESTONE_ITEM_NAME_OVERVIEW(PURPLE.color),
    MILESTONE_ITEM_LORE_OVERVIEW(GRAY.color),
    MILESTONE_ITEM_NAME_X(PURPLE.color),
    MILESTONE_ITEM_NAME_ADD(GREEN.color),
    MILESTONE_ITEM_NAME_REWARDS_X,
    MILESTONE_ITEM_NAME_BROADCAST(PURPLE.color),
    MILESTONE_MESSAGE_NUMBER_ENTER(GREEN.color),

    STREAK_INVENTORY_NAME_OVERVIEW,
    STREAK_INVENTORY_NAME_X,
    STREAK_ITEM_NAME_OVERVIEW(PURPLE.color),
    STREAK_ITEM_LORE_OVERVIEW(GRAY.color),
    STREAK_ITEM_NAME_X(PURPLE.color),
    STREAK_ITEM_NAME_ADD(GREEN.color),
    STREAK_ITEM_NAME_REWARDS_X,
    STREAK_ITEM_NAME_BROADCAST(PURPLE.color),
    STREAK_MESSAGE_NUMBER_ENTER(GREEN.color),

    MONEY_REWARD_ITEM_NAME(PURPLE.color),
    MONEY_REWARD_UNIT,

    MONTHLY_VOTES_ITEM_NAME(RED.color),
    MONTHLY_VOTES_MESSAGE_RESET_COMMAND_USAGE(RED.color),
    MONTHLY_VOTES_MESSAGE_RESET_SUCCESS_OTHER_X(GREEN.color, AQUA.color),
    MONTHLY_VOTES_MESSAGE_RESET_SUCCESS_SELF(GREEN.color),

    POWER_REWARDS_INVENTORY_NAME,
    POWER_REWARDS_ITEM_NAME(PURPLE.color),
    POWER_REWARDS_ITEM_LORE(GRAY.color),

    PERMISSION_REWARDS_ITEM_NAME(PURPLE.color),
    PERMISSION_REWARDS_MESSAGE_TITLE(GRAY.color),
    PERMISSION_REWARDS_UNIT,
    PERMISSION_REWARDS_UNIT_MULTIPLE,

    PLAYER_INFO_INVENTORY_NAME_X,
    PLAYER_INFO_ITEM_LORE_LAST_X(GRAY.color, PURPLE.color),
    PLAYER_INFO_ITEM_LORE_POWER_X(GRAY.color),
    PLAYER_INFO_ITEM_LORE_VOTES_X(GRAY.color, PURPLE.color),
    PLAYER_INFO_ITEM_LORE_VOTES_MONTHLY_X(GRAY.color, PURPLE.color),
    PLAYER_INFO_ITEM_LORE_VOTES_WEEKLY_X(GRAY.color, PURPLE.color),
    PLAYER_INFO_ITEM_LORE_VOTES_DAILY_X(GRAY.color, PURPLE.color),
    PLAYER_INFO_ITEM_LORE_STREAK_DAILY_X(GRAY.color, YELLOW.color),
    PLAYER_INFO_VALUE_NEVER(PURPLE.color),
    PLAYER_INFO_ITEM_NAME(PURPLE.color),
    PLAYER_INFO_ITEM_LORE(GRAY.color),

    PLAYER_ERROR_NOT_EXIST_X,
    PLAYER_ITEM_NAME_SKULL_X(AQUA.color),
    PLAYER_NAME_UNKNOWN,
    PLAYER_NAME_UNKNOWN_COLORED(RED.color),

    PLUGIN_UPDATE_ITEM_NAME_INGAME(PURPLE.color),
    PLUGIN_UPDATE_MESSAGE_DISABLE(GRAY.color),

    PLUGIN_VERSION_ITEM_LORE_LATEST_X(GRAY.color, GREEN.color),
    PLUGIN_VERSION_ITEM_NAME(PURPLE.color),
    PLUGIN_VERSION_ITEM_LORE_BETA(YELLOW.color),

    QUEUE_ERROR_DELETE_XY,
    QUEUE_MESSAGE_ADD,
    QUEUE_MESSAGE_FORWARD_XY,

    RELOAD_ERROR_FAIL(RED.color),
    RELOAD_MESSAGE_FINISH_X(GREEN.color),
    RELOAD_MESSAGE_START_X(GRAY.color),

    RESET_VOTES_INVENTORY_NAME,
    RESET_VOTES_MESSAGE_CANCEL(RED.color),

    REWARD_SETTINGS_INVENTORY_NAME_OVERVIEW,
    REWARD_SETTINGS_ITEM_NAME_OVERVIEW(PURPLE.color),

    SETTINGS_ERROR_DISABLED(RED.color),
    SETTINGS_INVENTORY_NAME,
    SETTINGS_ITEM_NAME_GENERAL(AQUA.color),
    SETTINGS_ITEM_NAME_MESSAGES(YELLOW.color),
    SETTINGS_ITEM_NAME_SUPPORT(GREEN.color),

    SOUND_EFFECTS_ITEM_NAME(PURPLE.color),

    STATISTICS_INVENTORY_NAME,
    STATISTICS_ITEM_LORE_COUNTRY(GRAY.color),
    STATISTICS_ITEM_LORE_MC_VERSION_X(GRAY.color, GREEN.color),
    STATISTICS_ITEM_LORE_VOTE_TOP_SITES(GRAY.color),
    STATISTICS_ITEM_LORE_VOTE_TOP_SITES_END(GRAY.color),
    STATISTICS_ITEM_LORE(GRAY.color),
    STATISTICS_ITEM_NAME(PURPLE.color),
    STATISTICS_ITEM_NAME_COUNTRY(PURPLE.color),
    STATISTICS_ITEM_NAME_MC_VERSION(PURPLE.color),
    STATISTICS_ITEM_NAME_VOTE_TOP_SITES(PURPLE.color),

    SUPPORT_INVENTORY_NAME,

    TIME_UNIT_SECONDS_MULTIPLE,

    UUID_STORAGE_ITEM_NAME(RED.color),
    UUID_STORAGE_ITEM_LORE(GRAY.color),

    VOTES_SET_MESSAGE_COMMAND_USAGE(RED.color),
    VOTES_SET_MESSAGE_OTHER_XY(GREEN.color, AQUA.color, AQUA.color),
    VOTES_SET_MESSAGE_SELF_X(GREEN.color, AQUA.color),

    VOTE_ITEM_NAME_BROADCAST(PURPLE.color),

    VOTE_LINKS_INVENTORY_NAME,
    VOTE_LINKS_ITEM_LORE(GRAY.color),
    VOTE_LINKS_ITEM_NAME(PURPLE.color),
    VOTE_LINKS_ITEM_NAME_GUI(PURPLE.color),
    VOTE_LINKS_MESSAGE_LORE_ENTER(GREEN.color),
    VOTE_LINKS_MESSAGE_LORE_ENTER_MORE(GREEN.color),
    VOTE_LINKS_MESSAGE_TITLE_ENTER(GREEN.color),
    VOTE_LINKS_MESSAGE_URL(GREEN.color),

    VOTE_PARTY_ERROR_NO_CHESTS(RED.color),
    VOTE_PARTY_INVENTORY_NAME_SETTINGS,
    VOTE_PARTY_INVENTORY_NAME_CHEST_OVERVIEW,
    VOTE_PARTY_INVENTORY_NAME_CHEST_X,
    VOTE_PARTY_ITEM_LORE_CHEST(GRAY.color),
    VOTE_PARTY_ITEM_LORE_VOTES_UNTIL_XY(GRAY.color, AQUA.color, GREEN.color),
    VOTE_PARTY_ITEM_NAME_BROADCAST_COUNT(PURPLE.color),
    VOTE_PARTY_ITEM_NAME_BROADCAST_COUNTDOWN_END(PURPLE.color),
    VOTE_PARTY_ITEM_NAME_BROADCAST_UNTIL(PURPLE.color),
    VOTE_PARTY_ITEM_NAME(PURPLE.color),
    VOTE_PARTY_ITEM_NAME_SETTINGS(PURPLE.color),
    VOTE_PARTY_ITEM_LORE_SETTINGS(GRAY.color),
    VOTE_PARTY_ITEM_NAME_OVERVIEW(PURPLE.color),
    VOTE_PARTY_ITEM_NAME_CHEST(PURPLE.color),
    VOTE_PARTY_ITEM_NAME_CHEST_X(PURPLE.color),
    VOTE_PARTY_ITEM_NAME_COUNTDOWN(PURPLE.color),
    VOTE_PARTY_ITEM_NAME_TYPE(PURPLE.color),
    VOTE_PARTY_ITEM_NAME_VOTES_UNTIL(PURPLE.color),
    VOTE_PARTY_MESSAGE_CHEST_CREATED_X(GREEN.color),
    VOTE_PARTY_MESSAGE_CHEST_DELETED_X(RED.color),
    VOTE_PARTY_MESSAGE_CHEST_RECEIVED(GREEN.color),
    VOTE_PARTY_MESSAGE_COMMAND_USAGE(RED.color),
    VOTE_PARTY_NAME_PIG_X(PURPLE.color, BOLD.color),
    VOTE_PARTY_UNIT_CHEST_MULTIPLE,

    VOTE_REMINDER_ITEM_NAME(PURPLE.color),

    VOTE_REWARDS_INVENTORY_NAME,
    VOTE_REWARDS_ITEM_NAME(PURPLE.color),
    VOTE_REWARDS_ITEM_LORE(GRAY.color),

    VOTE_TOP_ERROR_CREATE,
    VOTE_TOP_ERROR_DELETE,
    VOTE_TOP_ITEM_NAME_STAND_BREAK_MESSAGE(PURPLE.color),
    VOTE_TOP_MESSAGE_CREATE_COMMAND_USAGE(RED.color),
    VOTE_TOP_MESSAGE_DELETE_COMMAND_USAGE(RED.color),
    VOTE_TOP_MESSAGE_SIGN_CREATED_X(GREEN.color),
    VOTE_TOP_MESSAGE_SIGN_DELETED_X(RED.color),
    VOTE_TOP_MESSAGE_STAND_CREATED_X(GREEN.color),
    VOTE_TOP_MESSAGE_STAND_DELETED_X(RED.color),
    VOTE_TOP_UNIT_STAND,

    XP_REWARD_ITEM_NAME(PURPLE.color),

    XP_UNIT_LEVELS_MULTIPLE;

    fun getValue(locale: Locale? = null): String
    {
        var value = if (locale == null) CV.RESOURCE_BUNDLE?.getString(name.uppercase())
            ?: "" else ResourceBundle.getBundle(CV.RESOURCE_BUNDLE?.baseBundleName ?: "", locale)
            .getString(name.uppercase())
        if (color != null)
        {
            value = "$color$value"
            value = value.split(";").joinToString(";${color}")
        }
        return value
    }

    fun with(x_: String, y_: String? = null): String
    {
        var x = x_
        if (colorX != null)
        {
            x = "$colorX$x"
            color?.let { x += it }
        }
        var y = y_
        if (colorY != null && y != null)
        {
            y = "$colorY$y"
            color?.let { y += it }
        }
        return getValue().replace("\$X", x).replaceIfNotNull("\$Y", y)
    }

    fun getColor(): String
    {
        return color.toString()
    }

    override fun toString(): String
    {
        return getValue()
    }
}
