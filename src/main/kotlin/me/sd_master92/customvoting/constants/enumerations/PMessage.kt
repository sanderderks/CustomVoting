package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.replaceIfNotNull
import org.bukkit.ChatColor

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
    private val value: String,
    private val color: PMessage? = null,
    private val colorX: PMessage? = null,
    private val colorY: PMessage? = null
)
{
    X("%X%"),
    Y("%Y%"),

    GREEN(ChatColor.GREEN.toString()),
    RED(ChatColor.RED.toString()),
    PURPLE(ChatColor.LIGHT_PURPLE.toString()),
    AQUA(ChatColor.AQUA.toString()),
    GRAY(ChatColor.GRAY.toString()),
    YELLOW(ChatColor.YELLOW.toString()),
    BOLD(ChatColor.BOLD.toString()),

    ACTION_ERROR_BREAK_BLOCK_NO_PERMISSION("You do not have permission to break this block.", RED),
    ACTION_ERROR_INTERACT_NEED_CRATE("You need to open a crate with this key.", RED),
    ACTION_ERROR_OPEN_CHEST_NEED_KEY("You need a key to open this crate.", RED),
    ACTION_ERROR_OPEN_CHEST_NO_PERMISSION("You do not have permission to open this chest.", RED),
    ACTION_ERROR_OPEN_CHEST_WRONG_KEY("Wrong key for this crate!", RED),

    COMMAND_REWARDS_ERROR_FORBIDDEN("This command is forbidden.", RED),
    COMMAND_REWARDS_ITEM_NAME("Command Rewards", PURPLE),
    COMMAND_REWARDS_ITEM_NAME_VOTE_PARTY("Vote Party Commands", PURPLE),
    COMMAND_REWARDS_MESSAGE_PLACEHOLDER("(with %PLAYER% as placeholder)", GREEN),
    COMMAND_REWARDS_MESSAGE_TITLE("Commands:", GRAY),
    COMMAND_REWARDS_UNIT("command"),
    COMMAND_REWARDS_UNIT_MULTIPLE("commands"),

    CRATE_INVENTORY_NAME_OVERVIEW("Crates"),
    CRATE_ITEM_NAME_OVERVIEW("Crates", PURPLE),
    CRATE_ITEM_LORE_OVERVIEW(
        "Special chest with loads of;rewards.;But only 1 item is given;to the player who opens it.",
        GRAY
    ),
    CRATE_INVENTORY_NAME("Crate"),
    CRATE_INVENTORY_NAME_PLACE_CONFIRM_X("Place '$X' here?"),
    CRATE_INVENTORY_NAME_PERC_REWARDS_XY("$X% Rewards '$Y'"),
    CRATE_ITEM_LORE_KEY_X("#$X", GRAY),
    CRATE_ITEM_LORE_PLACE("Right-click on a block;to place crate.", GRAY),
    CRATE_ITEM_NAME_ADD("Add Crate", GREEN),
    CRATE_ITEM_NAME_KEY_X("$X", AQUA),
    CRATE_ITEM_NAME_KEY_GET("Get Key", AQUA),
    CRATE_ITEM_NAME_NO_PRICE("No price!", RED),
    CRATE_ITEM_NAME_RENAME("Change Name", PURPLE),
    CRATE_ITEM_NAME_REWARDS_PERCENTAGE_X("Crate Rewards $X%", PURPLE),
    CRATE_MESSAGE_NAME_CHANGED_X("Name changed to \'$X\'!", GREEN),
    CRATE_MESSAGE_NAME_ENTER("Please enter a new name", GREEN),
    CRATE_MESSAGE_NO_PRICE("No price this time :\"(", RED),
    CRATE_MESSAGE_REWARD_X("You received a $X% chance reward!", GREEN, AQUA),
    CRATE_ERROR_EMPTY("There are no rewards inside this crate...", RED),
    CRATE_NAME_DEFAULT_X("Crate $X", PURPLE),
    CRATE_NAME_X("$X", PURPLE),
    CRATE_NAME_STAND_X("$X", AQUA),

    DANGER_ZONE_INVENTORY_NAME("Danger Zone"),
    DANGER_ZONE_ITEM_NAME("Danger Zone", RED),

    DATABASE_VALUE_CONNECTED("Connected!", GREEN),
    DATABASE_VALUE_NOT_FUNCTIONING("Not functioning!", RED),
    DATABASE_ITEM_NAME("Database", RED),
    DATABASE_ITEM_LORE("Restart or reload the server;after changing.", GRAY),

    DISABLED_WORLD_OVERVIEW_INVENTORY_NAME("Disabled Worlds"),
    DISABLED_WORLD_OVERVIEW_ITEM_NAME("Disabled Worlds", PURPLE),
    DISABLED_WORLD_OVERVIEW_ITEM_LORE("For receiving rewards.", GRAY),
    DISABLED_WORLD_ITEM_NAME_X("$X", PURPLE),
    DISABLED_WORLD_ITEM_NAME_MESSAGE("Disabled World Message", PURPLE),

    DISCORD_ITEM_LORE("Join the Discord server", GRAY),
    DISCORD_ITEM_NAME("Discord", PURPLE),
    DISCORD_MESSAGE("Join the Discord server:", AQUA),
    DISCORD_MESSAGE_URL("https://discord.gg/v3qmJu7jWD", GREEN),

    SUPPORTER_INVENTORY_NAME("Supporters"),
    SUPPORTER_ITEM_LORE("CustomVoting supporters!", GRAY),
    SUPPORTER_ITEM_NAME("Supporters", PURPLE),

    PERM_GROUP_OVERVIEW_INVENTORY_NAME("Enabled Groups"),
    PERM_GROUP_OVERVIEW_ITEM_NAME("Enabled Groups", PURPLE),
    PERM_GROUP_OVERVIEW_ITEM_LORE_X("Plugin: $X", GRAY, GREEN),
    PERM_GROUP_ITEM_NAME_X("$X", PURPLE),

    PERM_USER_OVERVIEW_INVENTORY_NAME_X("Enabled Users #$X"),
    PERM_USER_OVERVIEW_ITEM_LORE("This setting overrides;the group permissions.", GRAY),
    PERM_USER_OVERVIEW_ITEM_NAME("Enabled Users", PURPLE),

    FAKE_VOTE_MESSAGE_COMMAND_USAGE("- /fakevote <name> [website]", RED),
    FAKE_VOTE_VALUE_WEBSITE("fakevote.com"),

    FIREWORK_ITEM_NAME("Firework", PURPLE),

    GENERAL_ERROR("Something went wrong!", RED),
    GENERAL_ERROR_ALREADY_EXIST_X("That $X already exists.", RED),
    GENERAL_ERROR_INVALID_ARGUMENT_NOT_NUMBER_X("Invalid argument: \'$X\' must be a number.", RED),
    GENERAL_ERROR_INVALID_ARGUMENT_NOT_POSITIVE_X("Invalid argument: \'$X\' must be a positive number.", RED),
    GENERAL_ERROR_NOT_EXIST_X("That $X does not exist.", RED),
    GENERAL_ITEM_LORE_CURRENT_X("Currently: $X", GRAY, AQUA),
    GENERAL_ITEM_LORE_CURRENT_XY("Currently: $X $Y", GRAY, AQUA),
    GENERAL_ITEM_LORE_DISABLED_X("Disabled: $X", GRAY),
    GENERAL_ITEM_LORE_ENABLED_X("Enabled: $X", GRAY),
    GENERAL_ITEM_LORE_STATUS_X("Status: $X", GRAY, AQUA),
    GENERAL_ITEM_NAME_DELETE("Delete", RED),
    GENERAL_ITEM_NAME_NEXT("Next", AQUA),
    GENERAL_ITEM_NAME_PREVIOUS("Previous", AQUA),
    GENERAL_ITEM_NAME_REFRESH("Refresh", RED),
    GENERAL_MESSAGE_CANCEL_BACK("Type 'cancel' to go back", GRAY),
    GENERAL_MESSAGE_CANCEL_CONTINUE("Type 'cancel' to continue", GRAY),
    GENERAL_MESSAGE_CREATE_SUCCESS_X("$X created!", GREEN),
    GENERAL_MESSAGE_DELETE_SUCCESS_X("$X deleted!", RED),
    GENERAL_MESSAGE_LIST_ADDED_XY("Added $X to $Y", GREEN),
    GENERAL_MESSAGE_LIST_ALTER_X("Please enter a $X to add or remove from the list", GREEN),
    GENERAL_MESSAGE_LIST_EMPTY_X("There are currently no $X.", RED),
    GENERAL_MESSAGE_LIST_REMOVED_XY("Removed $X from $Y", RED),
    GENERAL_MESSAGE_NUMBER_ENTER("Please enter a number", GREEN),
    GENERAL_MESSAGE_UPDATE_FAIL_X("Failed to update the $X!", RED),
    GENERAL_MESSAGE_UPDATE_NOTHING_CHANGED("Nothing changed!", GRAY),
    GENERAL_MESSAGE_UPDATE_SUCCESS_X("Successfully updated the $X!", GREEN),
    GENERAL_VALUE_DISABLED("Disabled", RED),
    GENERAL_VALUE_NO("No", RED),
    GENERAL_VALUE_YES("Yes", GREEN),
    GENERAL_FORMAT_DATE("dd-MM-yyyy"),

    GENERAL_SETTINGS_INVENTORY_NAME("General Settings"),

    ITEM_REWARDS_INVENTORY_NAME("Item Rewards"),
    ITEM_REWARDS_ITEM_NAME("Item Rewards", PURPLE),
    ITEM_REWARDS_ITEM_NAME_TYPE("Item Reward Type", PURPLE),
    ITEM_REWARDS_UNIT_STACKS_MULTIPLE("item stacks"),

    LUCKY_INVENTORY_NAME_SETTINGS("Lucky Vote Settings"),
    LUCKY_INVENTORY_NAME_REWARDS("Lucky Rewards"),
    LUCKY_ITEM_NAME("Lucky Vote", PURPLE),
    LUCKY_ITEM_NAME_SETTINGS("Lucky Rewards", PURPLE),
    LUCKY_ITEM_NAME_CHANCE("Lucky Vote Chance", PURPLE),
    LUCKY_ITEM_NAME_REWARDS("Lucky Rewards", PURPLE),
    LUCKY_ITEM_LORE_SETTINGS("One item from this inventory;is given if a player is lucky!", GRAY),

    MERGE_DUPLICATES_ITEM_LORE("Find and merge duplicate;playerfiles;Currently: $X files", GRAY, AQUA),
    MERGE_DUPLICATES_ITEM_NAME("Merge Duplicates", RED),
    MERGE_DUPLICATES_MESSAGE_DELETED_X("Deleted $X votefiles!", RED),

    MESSAGE_SETTINGS_INVENTORY_NAME("Message Settings"),

    MILESTONE_INVENTORY_NAME_OVERVIEW("Vote Milestones"),
    MILESTONE_INVENTORY_NAME_X("Vote Milestone Settings #$X"),
    MILESTONE_ITEM_NAME_OVERVIEW("Vote Milestones", PURPLE),
    MILESTONE_ITEM_LORE_OVERVIEW("Rewards given when a player;reaches a certain amount;of votes.", GRAY),
    MILESTONE_ITEM_NAME_X("Milestone #$X", PURPLE),
    MILESTONE_ITEM_NAME_ADD("Add Milestone", GREEN),
    MILESTONE_ITEM_NAME_REWARDS_X("Milestone Item Rewards #$X"),
    MILESTONE_ITEM_NAME_BROADCAST("Milestone Broadcast", PURPLE),
    MILESTONE_MESSAGE_NUMBER_ENTER("Please enter a milestone number", GREEN),

    STREAK_INVENTORY_NAME_OVERVIEW("Vote Streaks"),
    STREAK_INVENTORY_NAME_X("Vote Streak Settings #$X"),
    STREAK_ITEM_NAME_OVERVIEW("Vote Streaks", PURPLE),
    STREAK_ITEM_LORE_OVERVIEW("Rewards given when a player;votes x days in a row.", GRAY),
    STREAK_ITEM_NAME_X("Streak #$X", PURPLE),
    STREAK_ITEM_NAME_ADD("Add Streak", GREEN),
    STREAK_ITEM_NAME_REWARDS_X("Streak Item Rewards #$X"),
    STREAK_ITEM_NAME_BROADCAST("Streak Broadcast", PURPLE),
    STREAK_MESSAGE_NUMBER_ENTER("Please enter a streak number", GREEN),

    MONEY_REWARD_ITEM_NAME("Money Reward", PURPLE),
    MONEY_REWARD_UNIT("money reward"),

    MONTHLY_VOTES_ITEM_NAME("Monthly Votes", RED),
    MONTHLY_VOTES_MESSAGE_RESET_COMMAND_USAGE("- /clearmonthlyvotes <name>", RED),
    MONTHLY_VOTES_MESSAGE_RESET_SUCCESS_OTHER_X("$X\'s monthly votes have been reset.", GREEN, AQUA),
    MONTHLY_VOTES_MESSAGE_RESET_SUCCESS_SELF("Your monthly votes have been reset.", GREEN),

    POWER_REWARDS_INVENTORY_NAME("Power Rewards"),
    POWER_REWARDS_ITEM_NAME("Power Rewards", PURPLE),
    POWER_REWARDS_ITEM_LORE("Alternative vote rewards;for VIP's!", GRAY),

    PERMISSION_REWARDS_ITEM_NAME("Permission Rewards", PURPLE),
    PERMISSION_REWARDS_MESSAGE_TITLE("Permissions:", GRAY),
    PERMISSION_REWARDS_UNIT("permission"),
    PERMISSION_REWARDS_UNIT_MULTIPLE("permissions"),

    PLAYER_INFO_INVENTORY_NAME_X("Player Info #$X"),
    PLAYER_INFO_ITEM_LORE_LAST_X("Last vote: $X", GRAY, PURPLE),
    PLAYER_INFO_ITEM_LORE_POWER_X("Power rewards: $X", GRAY),
    PLAYER_INFO_ITEM_LORE_VOTES_X("All-time votes: $X", GRAY, PURPLE),
    PLAYER_INFO_ITEM_LORE_VOTES_MONTHLY_X("Monthly votes: $X", GRAY, PURPLE),
    PLAYER_INFO_ITEM_LORE_VOTES_DAILY_X("Daily votes: $X", GRAY, PURPLE),
    PLAYER_INFO_ITEM_LORE_STREAK_DAILY_X("Streak days: $X", GRAY, YELLOW),
    PLAYER_INFO_VALUE_NEVER("never", PURPLE),
    PLAYER_INFO_ITEM_NAME("Player Info", PURPLE),
    PLAYER_INFO_ITEM_LORE("Player vote information", GRAY),

    PLAYER_ERROR_NOT_EXIST_X("A player with name $X does not exist"),
    PLAYER_ITEM_NAME_SKULL_X("$X", AQUA),
    PLAYER_NAME_UNKNOWN("Unknown"),
    PLAYER_NAME_UNKNOWN_COLORED("Unknown", RED),

    PLUGIN_UPDATE_ITEM_NAME_INGAME("Ingame Updates", PURPLE),
    PLUGIN_UPDATE_MESSAGE_DISABLE("Updates can be turned off in the /votesettings", GRAY),

    PLUGIN_VERSION_ITEM_LORE_LATEST_X("Latest: $X;;Click to download", GRAY, GREEN),
    PLUGIN_VERSION_ITEM_NAME("Up to date?", PURPLE),

    QUEUE_ERROR_DELETE_XY("Failed to delete queue of $X|$Y"),
    QUEUE_MESSAGE_ADD("The player is not online, adding vote to queue"),
    QUEUE_MESSAGE_FORWARD_XY("$X queued votes found for $Y. Forwarding in 10 seconds..."),

    RELOAD_ERROR_FAIL("Could not reload configuration!", RED),
    RELOAD_MESSAGE_FINISH_X("Configuration$X reloaded!", GREEN),
    RELOAD_MESSAGE_START_X("Reloading configuration$X...", GRAY),

    RESET_VOTES_INVENTORY_NAME_ALL("Reset ALL votes?"),
    RESET_VOTES_INVENTORY_NAME_MONTHLY("Reset monthly votes?"),
    RESET_VOTES_MESSAGE_CANCEL("Votes are not reset.", RED),
    RESET_VOTES_MESSAGE_CONFIRM("A new month has started and votes can be reset in the /votesettings.", GREEN),
    RESET_VOTES_ITEM_NAME_MONTHLY("Monthly Reset", RED),

    REWARD_SETTINGS_INVENTORY_NAME_OVERVIEW("Reward Settings"),
    REWARD_SETTINGS_ITEM_NAME_OVERVIEW("Rewards", PURPLE),

    SETTINGS_ERROR_DISABLED("The /votesettings GUI has been disabled. You can change this in the settings.yml.", RED),
    SETTINGS_INVENTORY_NAME("Vote Settings"),
    SETTINGS_ITEM_NAME_GENERAL("General", AQUA),
    SETTINGS_ITEM_NAME_MESSAGES("Messages", YELLOW),
    SETTINGS_ITEM_NAME_SUPPORT("Support", GREEN),

    SOUND_EFFECTS_ITEM_NAME("Sound Effects", PURPLE),

    STATISTICS_INVENTORY_NAME("Statistics"),
    STATISTICS_ITEM_LORE_COUNTRY("Most popular in:;", GRAY),
    STATISTICS_ITEM_LORE_MC_VERSION_X("Most popular version: $X", GRAY, GREEN),
    STATISTICS_ITEM_LORE_VOTE_TOP_SITES("Top vote sites by;CustomVoting users:;", GRAY),
    STATISTICS_ITEM_LORE_VOTE_TOP_SITES_END(";;RED = not setup yet", GRAY),
    STATISTICS_ITEM_LORE("CustomVoting BStats", GRAY),
    STATISTICS_ITEM_NAME("Statistics", PURPLE),
    STATISTICS_ITEM_NAME_COUNTRY("Country", PURPLE),
    STATISTICS_ITEM_NAME_MC_VERSION("Minecraft Version", PURPLE),
    STATISTICS_ITEM_NAME_VOTE_TOP_SITES("Top vote sites", PURPLE),

    SUPPORT_INVENTORY_NAME("Support"),

    TIME_UNIT_SECONDS_MULTIPLE("seconds"),

    UUID_STORAGE_ITEM_NAME("UUID Storage", RED),
    UUID_STORAGE_ITEM_LORE("Turn off if your server;is in offline-mode.", GRAY),

    VOTES_SET_MESSAGE_COMMAND_USAGE("- /setvotes <amount> [name]", RED),
    VOTES_SET_MESSAGE_OTHER_XY("$X's votes have been set to $Y.", GREEN, AQUA, AQUA),
    VOTES_SET_MESSAGE_SELF_X("Your votes have been set to $X.", GREEN, AQUA),

    VOTE_ITEM_NAME_BROADCAST("Vote Broadcast", PURPLE),

    VOTE_LINKS_INVENTORY_NAME("Vote Links"),
    VOTE_LINKS_ITEM_LORE("Place items in this inventory;;Right-click to edit an item", GRAY),
    VOTE_LINKS_ITEM_NAME("Vote Links", PURPLE),
    VOTE_LINKS_ITEM_NAME_GUI("Vote Links Inventory", PURPLE),
    VOTE_LINKS_MESSAGE_LORE_ENTER("Add lore (subtext) to this item", GREEN),
    VOTE_LINKS_MESSAGE_LORE_ENTER_MORE("Add more lore (subtext) to this item", GREEN),
    VOTE_LINKS_MESSAGE_TITLE_ENTER("Enter a title for this item (with & colors)", GREEN),
    VOTE_LINKS_MESSAGE_URL("Add a link to this item", GREEN),

    VOTE_PARTY_ERROR_NO_CHESTS("There are no registered Vote Party Chests.", RED),
    VOTE_PARTY_INVENTORY_NAME_SETTINGS("Vote Party Settings"),
    VOTE_PARTY_INVENTORY_NAME_CHEST_OVERVIEW("Vote Party Chests"),
    VOTE_PARTY_INVENTORY_NAME_CHEST_X("Vote Party Chest #$X"),
    VOTE_PARTY_ITEM_LORE_CHEST(
        "Place this chest somewhere in the sky.;The contents of this chest will;start dropping when the voteparty starts.",
        GRAY
    ),
    VOTE_PARTY_ITEM_LORE_VOTES_UNTIL_XY("Required: $X;Votes left: $Y", GRAY, AQUA, GREEN),
    VOTE_PARTY_ITEM_NAME_BROADCAST_COUNT("VoteParty Count Broadcast", PURPLE),
    VOTE_PARTY_ITEM_NAME_BROADCAST_COUNTDOWN_END("VoteParty Count Ending Broadcast", PURPLE),
    VOTE_PARTY_ITEM_NAME_BROADCAST_UNTIL("VoteParty Votes Broadcast", PURPLE),
    VOTE_PARTY_ITEM_NAME("Vote Party", PURPLE),
    VOTE_PARTY_ITEM_NAME_SETTINGS("Vote Party", PURPLE),
    VOTE_PARTY_ITEM_LORE_SETTINGS("Chests with possibilities!", GRAY),
    VOTE_PARTY_ITEM_NAME_OVERVIEW("Vote Party Chests", PURPLE),
    VOTE_PARTY_ITEM_NAME_CHEST("Vote Party Chest", PURPLE),
    VOTE_PARTY_ITEM_NAME_CHEST_X("Vote Party Chest #$X", PURPLE),
    VOTE_PARTY_ITEM_NAME_COUNTDOWN("Vote Party Countdown", PURPLE),
    VOTE_PARTY_ITEM_NAME_TYPE("Vote Party Type", PURPLE),
    VOTE_PARTY_ITEM_NAME_VOTES_UNTIL("Votes until Vote Party", PURPLE),
    VOTE_PARTY_MESSAGE_CHEST_CREATED_X("Vote Party Chest #$X registered.", GREEN),
    VOTE_PARTY_MESSAGE_CHEST_DELETED_X("Vote Party Chest #$X deleted.", RED),
    VOTE_PARTY_MESSAGE_CHEST_RECEIVED("You have been given the Vote Party Chest.", GREEN),
    VOTE_PARTY_MESSAGE_COMMAND_USAGE("- /voteparty create | start", RED),
    VOTE_PARTY_NAME_PIG_X("$X", PURPLE, BOLD),
    VOTE_PARTY_UNIT_CHEST_MULTIPLE("chests"),

    VOTE_REMINDER_ITEM_NAME("Hourly Vote Reminder", PURPLE),

    VOTE_REWARDS_INVENTORY_NAME("Vote Rewards"),
    VOTE_REWARDS_ITEM_NAME("Vote Rewards", PURPLE),

    VOTE_TOP_ERROR_CREATE("Error while creating vote top"),
    VOTE_TOP_ERROR_DELETE("Error while deleting vote top"),
    VOTE_TOP_ITEM_NAME_STAND_BREAK_MESSAGE("Break Armorstand Message", PURPLE),
    VOTE_TOP_MESSAGE_CREATE_COMMAND_USAGE("- /createtop <top>", RED),
    VOTE_TOP_MESSAGE_DELETE_COMMAND_USAGE("- /deletetop <top>", RED),
    VOTE_TOP_MESSAGE_SIGN_CREATED_X("Registered Vote Sign #$X", GREEN),
    VOTE_TOP_MESSAGE_SIGN_DELETED_X("Unregistered Vote Sign #$X", RED),
    VOTE_TOP_MESSAGE_STAND_CREATED_X("Registered Vote Stand #$X", GREEN),
    VOTE_TOP_MESSAGE_STAND_DELETED_X("Unregistered Vote Stand #$X", RED),
    VOTE_TOP_UNIT_STAND("Vote Stand"),

    XP_REWARD_ITEM_NAME("XP Reward", PURPLE),

    XP_UNIT_LEVELS_MULTIPLE("levels");

    fun getValue(): String
    {
        var text = value
        if (color != null)
        {
            text = "$color$text"
            text = text.split(";").joinToString(";${color}")
        }
        return text
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
        return getValue().replace("$X", x).replaceIfNotNull("$Y", y)
    }

    override fun toString(): String
    {
        return getValue()
    }
}
