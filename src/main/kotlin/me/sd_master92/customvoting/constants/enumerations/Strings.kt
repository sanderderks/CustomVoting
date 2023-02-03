package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.replaceIfNotNull
import org.bukkit.ChatColor

enum class Strings(
    private val value: String,
    private val color: Strings? = null,
    private val colorX: Strings? = null,
    private val colorY: Strings? = null
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

    ERROR("Something went wrong!", RED),

    YES("Yes", GREEN),
    NO("No", RED),
    NEVER("never", RED),
    REFRESH("Refresh", RED),

    CONNECTED("Connected", GREEN),
    DISABLED("Disabled", RED),

    NOT_EXIST_X("That $X does not exist.", RED),
    ALREADY_EXIST_X("That $X already exists.", RED),
    INVALID_ARGUMENT_NOT_NUMBER_X("Invalid argument: \'$X\' must be a number.", RED),
    INVALID_ARGUMENT_NOT_POSITIVE_X("Invalid argument: \'$X\' must be a positive number.", RED),

    PLAYER_NAME_UNKNOWN("Unknown"),
    PLAYER_NAME_UNKNOWN_COLORED("Unknown", RED),
    PLAYER_NOT_EXIST_X("A player with name $X does not exist"),
    PLAYER_NOT_ONLINE_QUEUE("The player is not online, adding vote to queue"),
    PLAYER_SKULL_NAME_X("$X", AQUA),

    BREAK_BLOCK_NO_PERMISSION("You do not have permission to break this block.", RED),
    OPEN_CHEST_NO_PERMISSION("You do not have permission to open this chest.", RED),
    OPEN_CHEST_NEED_KEY("You need a key to open this crate.", RED),
    OPEN_CHEST_WRONG_KEY("Wrong key for this crate!", RED),
    INTERACT_OPEN_CRATE("You need to open a crate with this key.", RED),

    GUI_STATUS_X("Status: $X", GRAY, AQUA),
    GUI_CURRENT_X("Currently: $X", GRAY, AQUA),
    GUI_CURRENT_XY("Currently: $X $Y", GRAY, AQUA),
    GUI_ENABLED_X("Enabled: $X", GRAY),
    GUI_DISABLED_X("Disabled: $X", GRAY),
    GUI_DELETE("Delete", RED),
    GUI_TITLE_GENERAL_SETTINGS("General Settings"),
    GUI_TITLE_MESSAGE_SETTINGS("Message Settings"),
    GUI_TITLE_VOTE_LINKS("Vote Links"),
    GUI_TITLE_VOTE_CRATE("Vote Crate"),
    GUI_TITLE_VOTE_CRATES("Vote Crates"),
    GUI_TITLE_MILESTONES("Vote Milestones"),
    GUI_TITLE_ENABLED_GROUPS("Enabled Groups"),
    GUI_TITLE_ENABLED_USERS_X("Enabled Users #$X"),
    GUI_TITLE_ITEM_REWARDS("Item Rewards"),
    GUI_TITLE_LUCKY_REWARDS("Lucky Rewards"),
    GUI_TITLE_DONATORS("Donators"),
    GUI_TITLE_PLAYER_INFO_X("Player Info #$X"),
    GUI_TITLE_STATISTICS("Statistics"),
    GUI_TITLE_SUPPORT("Support"),
    GUI_TITLE_VOTE_PARTY_CHEST_X("Vote Party Chest #$X"),
    GUI_TITLE_VOTE_SETTINGS("Vote Settings"),
    GUI_PAGINATION_NEXT("Next", AQUA),
    GUI_PAGINATION_PREVIOUS("Previous", AQUA),
    GUI_DISABLED_WORLDS("Disabled Worlds", PURPLE),
    GUI_DISABLED_WORLD_X("$X", PURPLE),
    GUI_ENABLED_GROUP("Enabled Groups", PURPLE),
    GUI_ENABLED_GROUP_X("$X", PURPLE),
    GUI_ENABLED_USERS("Enabled Users", PURPLE),
    GUI_VOTE_PARTY("Vote Party", PURPLE),
    GUI_VOTE_PARTY_VOTES_UNTIL("Votes until Vote Party", PURPLE),
    GUI_VOTE_PARTY_VOTES_UNTIL_XY("Required: $X;${GRAY}Votes left: $Y", GRAY, AQUA, GREEN),
    GUI_VOTE_PARTY_TYPE("Vote Party Type", PURPLE),
    GUI_VOTE_PARTY_COUNTDOWN("Vote Party Countdown", PURPLE),
    GUI_SOUND_EFFECTS("Sound Effects", PURPLE),
    GUI_MONTHLY_RESET("Monthly Reset", PURPLE),
    GUI_MONTHLY_VOTES("Monthly Votes", PURPLE),
    GUI_LUCKY_VOTE("Lucky Vote", PURPLE),
    GUI_LUCKY_VOTE_CHANCE("Lucky Vote Chance", PURPLE),
    GUI_LUCKY_VOTE_REWARDS("Lucky Rewards", PURPLE),
    GUI_MONEY_REWARD("Money Reward", PURPLE),
    GUI_CRATE_REWARDS("Crate Rewards", PURPLE),
    GUI_FIREWORK("Firework", PURPLE),
    GUI_UUID_STORAGE("UUID Storage", RED),
    GUI_XP_REWARD("XP Reward", PURPLE),
    GUI_ITEM_REWARD("Item Reward Type", PURPLE),
    GUI_COMMAND_REWARDS("Command Rewards", PURPLE),
    GUI_COMMAND_REWARDS_VOTE_PARTY("Vote Party Commands", PURPLE),
    GUI_ITEM_REWARDS("Item Rewards", PURPLE),
    GUI_VOTE_LINKS("Vote Links", PURPLE),
    GUI_VOTE_LINKS_LORE("Place items in this inventory;;${GRAY}Right-click to edit an item", GRAY),
    GUI_VOTE_LINKS_INVENTORY("Vote Links Inventory", PURPLE),
    GUI_VOTE_BROADCAST("Vote Broadcast", PURPLE),
    GUI_MILESTONE_BROADCAST("Milestone Broadcast", PURPLE),
    GUI_BROADCAST_VOTE_PARTY_UNTIL("VoteParty Votes Broadcast", PURPLE),
    GUI_BROADCAST_VOTE_PARTY_COUNT("VoteParty Count Broadcast", PURPLE),
    GUI_BROADCAST_VOTE_PARTY_COUNTDOWN_END("VoteParty Count Ending Broadcast", PURPLE),
    GUI_VOTE_STAND_BREAK("Break Armorstand Message", PURPLE),
    GUI_DISABLED_WORLD_MESSAGE("Disabled World Message", PURPLE),
    GUI_VOTE_REMINDER("Hourly Vote Reminder", PURPLE),
    GUI_VOTE_CRATE_ADD("Add Crate", GREEN),
    GUI_VOTE_CRATE_RENAME("Change Name", PURPLE),
    GUI_VOTE_CRATE_KEY("Get Key", AQUA),
    GUI_VOTE_CRATE_REWARDS_PERCENTAGE_X("Crate Rewards $X%", PURPLE),
    GUI_MILESTONE_REWARDS_X("Milestone Item Rewards #$X"),

    SECOND_TYPE_MULTIPLE("seconds"),
    XP_TYPE_MULTIPLE("levels"),
    ITEM_STACK_TYPE_MULTIPLE("item stacks"),

    RESET_VOTES_MONTHLY("Reset monthly votes?"),
    RESET_VOTES_ALL("Reset ALL votes?"),
    RESET_VOTES_CANCEL("Votes are not reset.", RED),
    RESET_VOTES_CONFIRM("A new month has started and votes can be reset in the /votesettings.", GREEN),

    VOTE_SETTINGS_DISABLED("The /votesettings GUI has been disabled. You can change this in the settings.yml.", RED),
    VOTE_SETTINGS_GENERAL("General", AQUA),
    VOTE_SETTINGS_REWARDS("Rewards", PURPLE),
    VOTE_SETTINGS_MESSAGES("Messages", YELLOW),
    VOTE_SETTINGS_SUPPORT("Support", GREEN),

    VOTE_CRATE_REWARDS_NAME_XY("$X% Rewards '$Y'"),
    VOTE_CRATE_NO_PRICE("No price!", RED),
    VOTE_CRATE_NO_PRICE_MESSAGE("No price this time :\"(", RED),
    VOTE_CRATE_REWARD_X("You received a $X% chance reward!", GREEN, AQUA),
    VOTE_CRATE_EMPTY("There are no rewards inside this crate...", RED),
    VOTE_CRATE_NAME_DEFAULT_X("Crate $X"),
    VOTE_CRATE_NAME_X("$X", PURPLE),
    VOTE_CRATE_LORE_X("#$X", GRAY),
    VOTE_CRATE_DELETED_X("$X deleted.", RED),
    VOTE_CRATE_PLACE_HERE_X("Place '$X' here?"),

    PLAYER_INFO_VOTES_X("Votes: $X", GRAY, PURPLE),
    PLAYER_INFO_MONTHLY_VOTES_X("Monthly votes: $X", GRAY, PURPLE),
    PLAYER_INFO_LAST_X("Last vote: $X", GRAY, PURPLE),
    PLAYER_INFO_PERMISSION_X("Permission rewards: $X", GRAY, PURPLE),

    INPUT_CANCEL_BACK("Type 'cancel' to go back", GRAY),
    INPUT_CANCEL_CONTINUE("Type 'cancel' to continue", GRAY),
    INPUT_ADDED_TO_LIST_XY("Added $X to $Y", GREEN),
    INPUT_REMOVED_FROM_LIST_XY("Removed $X from $Y"),
    INPUT_ALTER_LIST_X("Please enter a $X to add or remove from the list", GREEN),
    INPUT_LIST_EMPTY_X("There are currently no $X.", RED),
    INPUT_NUMBER("Please enter a number", GREEN),
    INPUT_COMMANDS("Commands:", GRAY),
    INPUT_COMMAND_TYPE("command"),
    INPUT_COMMAND_TYPE_MULTIPLE("commands"),
    INPUT_COMMANDS_ADD_LORE("(with %PLAYER% as placeholder)", GREEN),
    INPUT_COMMAND_FORBIDDEN("This command is forbidden.", RED),
    INPUT_PERMISSION_TYPE("permission"),
    INPUT_PERMISSION_TYPE_MULTIPLE("permissions"),
    INPUT_VOTE_LINK_TITLE("Enter a title for this item (with & colors)", GREEN),
    INPUT_VOTE_LINK_LORE("Add lore (subtext) to this item", GREEN),
    INPUT_VOTE_LINK_MORE_LORE("Add more lore (subtext) to this item", GREEN),
    INPUT_VOTE_LINK_URL("Add a link to this item", GREEN),
    INPUT_VOTE_CRATE_NAME_CHANGE("Please enter a new name", GREEN),
    INPUT_VOTE_CRATE_NAME_CHANGED_X("Name changed to \'$X\'!", GREEN),
    INPUT_VOTE_CRATE_DELETED_X("$X deleted!", RED),
    INPUT_MILESTONE("Please enter a milestone number", GREEN),

    GUI_TITLE_VOTE_REWARDS("Vote Rewards"),
    GUI_TITLE_VOTE_REWARDS_PERMISSION_BASED("Vote Rewards (permission based)"),
    MONEY_REWARD("money reward"),

    ENABLED_USERS_LORE("This setting overrides;;${GRAY}the group permissions.", GRAY),
    PERMISSION_REWARDS("Permission Rewards", PURPLE),
    PERMISSION_BASED_REWARDS("Permission Rewards", PURPLE),

    MILESTONES("Milestones", PURPLE),
    MILESTONE_SETTINGS_TITLE_X("Vote Milestone Settings #$X"),
    MILESTONE_NAME_X("Milestone #$X"),
    MILESTONE_ADD("Add Milestone", GREEN),
    MILESTONE_DELETED_X("$X deleted!", RED),

    CREATE_SUCCESS_X("$X created!", GREEN),
    UPDATE_NOTHING_CHANGED("Nothing changed!", GRAY),
    UPDATE_SUCCESS_X("Successfully updated the $X!", GREEN),
    UPDATE_FAIL_X("Failed to update the $X!", RED),

    VOTE_PARTY_COMMAND_USAGE("- /voteparty create | start", RED),
    VOTE_PARTY_CHEST("Vote Party Chest", PURPLE),
    VOTE_PARTY_CHEST_LORE(
        "Place this chest somewhere in the sky.;${GRAY}The contents of this chest will;${GRAY}start dropping when the voteparty starts.",
        GRAY
    ),
    VOTE_PARTY_CHEST_RECEIVE("You have been given the Vote Party Chest.", GREEN),
    VOTE_PARTY_CHEST_DELETED_X("Vote Party Chest #$X deleted.", RED),
    VOTE_PARTY_CHEST_CREATED_X("Vote Party Chest #$X registered.", GREEN),
    VOTE_PARTY_CHEST_NONE("There are no registered Vote Party Chests.", RED),
    VOTE_PARTY_PIG_TITLE_X("$X", PURPLE, BOLD),

    MONTHLY_VOTES_RESET_COMMAND_USAGE("- /clearmonthlyvotes <name>", RED),
    MONTHLY_VOTES_RESET_SELF("Your monthly votes have been reset.", GREEN),
    MONTHLY_VOTES_RESET_OTHER_X("$X\'s monthly votes have been reset.", GREEN, AQUA),

    VOTE_TOP_STAND_NAME("Vote Stand"),
    VOTE_TOP_STAND_CREATED_X("Registered Vote Stand #$X", GREEN),
    VOTE_TOP_STAND_DELETED_X("Unregistered Vote Stand #$X", RED),
    VOTE_TOP_SIGN_CREATED_X("Registered Vote Sign #$X", GREEN),
    VOTE_TOP_SIGN_DELETED_X("Unregistered Vote Sign #$X", RED),
    VOTE_TOP_CREATE_COMMAND_USAGE("- /createtop <top>", RED),
    VOTE_TOP_CREATE_ERROR("Error while creating vote top"),
    VOTE_TOP_DELETE_COMMAND_USAGE("- /deletetop <top>", RED),
    VOTE_TOP_DELETE_ERROR("Error while deleting vote top"),

    STATISTICS_VOTE_TOP_SITES("Top vote sites", PURPLE),
    STATISTICS_VOTE_TOP_SITES_LORE("Top vote sites by;${GRAY}CustomVoting users:;", GRAY),
    STATISTICS_VOTE_TOP_SITES_LORE_END(";;${GRAY}RED = not setup yet"),
    STATISTICS_MC_VERSION("Minecraft Version", PURPLE),
    STATISTICS_MC_VERSION_LORE_X("Most popular version: $X", GRAY, GREEN),
    STATISTICS_COUNTRY("Country", PURPLE),
    STATISTICS_COUNTRY_LORE("Most popular in:;", GRAY),

    SUPPORT_DISCORD_CHAT("Join the Discord server:", AQUA),
    SUPPORT_DISCORD_CHAT_URL("https://discord.gg/v3qmJu7jWD", GREEN),
    SUPPORT_DISCORD("Discord", PURPLE),
    SUPPORT_DISCORD_LORE("Join the Discord server", GRAY),
    SUPPORT_DATABASE("Database", PURPLE),
    SUPPORT_DONATORS("Donators", PURPLE),
    SUPPORT_DONATORS_LORE("CustomVoting supporters!", GRAY),
    SUPPORT_PLAYER_INFO("Player Info", PURPLE),
    SUPPORT_PLAYER_INFO_LORE("Player vote information", GRAY),
    SUPPORT_STATISTICS("Statistics", PURPLE),
    SUPPORT_STATISTICS_LORE("CustomVoting BStats", GRAY),
    SUPPORT_UP_TO_DATE("Up to date?", PURPLE),
    SUPPORT_LATEST_X("Latest: $X;;${GRAY}Click to download", GRAY, GREEN),
    SUPPORT_INGAME_UPDATE("Ingame Updates", PURPLE),
    SUPPORT_MERGE_DUPLICATES("Merge Duplicates", RED),
    SUPPORT_MERGE_DUPLICATES_L0RE_X(
        "Find and merge duplicate;${GRAY}playerfiles;${GRAY}Currently: $X files",
        GRAY,
        GREEN
    ),
    VOTE_FILES_DELETED_X("Deleted $X votefiles!", RED),

    FAKE_VOTE_WEBSITE("fakevote.com"),
    FAKE_VOTE_COMMAND_USAGE("- /fakevote <name> [website]", RED),

    RELOAD_START_X("Reloading configuration$X...", GRAY),
    RELOAD_FINISH_X("Configuration$X reloaded!", GREEN),
    RELOAD_FAIL("Could not reload configuration!", RED),

    PLUGIN_UPDATE("Updates can be turned off in the /votesettings", GRAY),

    VOTES_SET_SELF_X("Your votes have been set to $X.", GREEN, AQUA),
    VOTES_SET_OTHER_XY("$X's votes have been set to $Y.", GREEN, AQUA, AQUA),
    VOTES_SET_COMMAND_USAGE("- /setvotes <amount> [name]", RED),

    QUEUE_DELETE_ERROR_XY("Failed to delete queue of $X|$Y"),
    QUEUE_FORWARD_XY("$X queued votes found for $Y. Forwarding in 10 seconds...");

    fun getValue(): String
    {
        return if (color != null)
        {
            "$color$value"
        } else
        {
            value
        }
    }

    fun with(x_: String, y_: String? = null): String
    {
        var x = x_
        if (colorX != null)
        {
            x = "$colorX$x$color"
        }
        var y = y_
        if (colorY != null && y != null)
        {
            y = "$colorY$y$color"
        }
        return getValue().replace("$X", x).replaceIfNotNull("$Y", y)
    }

    override fun toString(): String
    {
        return getValue()
    }
}
