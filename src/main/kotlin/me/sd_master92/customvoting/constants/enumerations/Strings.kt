package me.sd_master92.customvoting.constants.enumerations

import me.sd_master92.customvoting.replaceIfNotNull
import org.bukkit.ChatColor

enum class Strings(private val value: String)
{
    ERROR("Something went wrong!"),

    YES(ChatColor.GREEN.toString() + "Yes"),
    NO(ChatColor.RED.toString() + "No"),
    NEVER(ChatColor.RED.toString() + "never"),
    REFRESH(ChatColor.RED.toString() + "Refresh"),

    DATABASE_CONNECTED(ChatColor.GREEN.toString() + "Connected"),
    DATABASE_DISABLED(ChatColor.RED.toString() + "Disabled"),

    NOT_EXIST_X(ChatColor.RED.toString() + "That %X% does not exist."),
    ALREADY_EXIST_X(ChatColor.RED.toString() + "That %X% already exists."),
    INVALID_ARGUMENT_NOT_NUMBER_X(ChatColor.RED.toString() + "Invalid argument: \'%X%\' must be a number."),
    INVALID_ARGUMENT_NOT_POSITIVE_X(ChatColor.RED.toString() + "Invalid argument: \'%X%\' must be a positive number."),

    PLAYER_NAME_UNKNOWN("Unknown"),

    BREAK_BLOCK_NO_PERMISSION(ChatColor.RED.toString() + "You do not have permission to break this block."),
    OPEN_CHEST_NO_PERMISSION(ChatColor.RED.toString() + "You do not have permission to open this chest."),
    OPEN_CHEST_NEED_KEY(ChatColor.RED.toString() + "You need a key to open this crate."),
    OPEN_CHEST_WRONG_KEY(ChatColor.RED.toString() + "Wrong key for this crate!"),
    INTERACT_OPEN_CRATE(ChatColor.RED.toString() + "You need to open a crate with this key."),

    GUI_STATUS_X(ChatColor.GRAY.toString() + "Status: %X%"),
    GUI_CURRENT_X(ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + "%X%"),
    GUI_CURRENT_XY(ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + "%X% " + ChatColor.GRAY + "%Y%"),
    GUI_ENABLED_X(ChatColor.GRAY.toString() + "Enabled: %X%"),
    GUI_DISABLED_X(ChatColor.GRAY.toString() + "Disabled: %X%"),
    GUI_DELETE(ChatColor.RED.toString() + "Delete"),
    GUI_TITLE_GENERAL_SETTINGS("General Settings"),
    GUI_TITLE_MESSAGE_SETTINGS("Message Settings"),
    GUI_TITLE_VOTE_LINKS("Vote Links"),
    GUI_TITLE_VOTE_CRATE("Vote Crate"),
    GUI_TITLE_VOTE_CRATES("Vote Crates"),
    GUI_TITLE_MILESTONES("Vote Milestones"),
    GUI_TITLE_ENABLED_GROUPS("Enabled Groups"),
    GUI_TITLE_ENABLED_USERS_X("Enabled Users #%X%"),
    GUI_TITLE_ITEM_REWARDS("Item Rewards"),
    GUI_TITLE_LUCKY_REWARDS("Lucky Rewards"),
    GUI_TITLE_DONATORS("Donators"),
    GUI_TITLE_PLAYER_INFO_X("Player Info #%X%"),
    GUI_TITLE_STATISTICS("Statistics"),
    GUI_TITLE_SUPPORT("Support"),
    GUI_TITLE_VOTE_PARTY_CHEST_X("Vote Party Chest #%X%"),
    GUI_TITLE_VOTE_SETTINGS("Vote Settings"),
    GUI_PAGINATION_NEXT(ChatColor.AQUA.toString() + "Next"),
    GUI_PAGINATION_PREVIOUS(ChatColor.AQUA.toString() + "Previous"),
    GUI_DISABLED_WORLDS(ChatColor.LIGHT_PURPLE.toString() + "Disabled Worlds"),
    GUI_VOTE_PARTY(ChatColor.LIGHT_PURPLE.toString() + "Vote Party"),
    GUI_VOTE_PARTY_VOTES_UNTIL(ChatColor.LIGHT_PURPLE.toString() + "Votes until Vote Party"),
    GUI_VOTE_PARTY_VOTES_UNTIL_XY(ChatColor.GRAY.toString() + "Required: " + ChatColor.AQUA + "%X%;" + ChatColor.GRAY + "Votes left: " + ChatColor.GREEN + "%Y%"),
    GUI_VOTE_PARTY_TYPE(ChatColor.LIGHT_PURPLE.toString() + "Vote Party Type"),
    GUI_VOTE_PARTY_COUNTDOWN(ChatColor.LIGHT_PURPLE.toString() + "Vote Party Countdown"),
    GUI_SOUND_EFFECTS(ChatColor.LIGHT_PURPLE.toString() + "Sound Effects"),
    GUI_MONTHLY_RESET(ChatColor.LIGHT_PURPLE.toString() + "Monthly Reset"),
    GUI_MONTHLY_VOTES(ChatColor.LIGHT_PURPLE.toString() + "Monthly Votes"),
    GUI_LUCKY_VOTE(ChatColor.LIGHT_PURPLE.toString() + "Lucky Vote"),
    GUI_FIREWORK(ChatColor.LIGHT_PURPLE.toString() + "Firework"),
    GUI_UUID_STORAGE(ChatColor.RED.toString() + "UUID Storage"),
    GUI_COMMAND_REWARDS(ChatColor.LIGHT_PURPLE.toString() + "Command Rewards"),
    GUI_COMMAND_REWARDS_VOTE_PARTY(ChatColor.LIGHT_PURPLE.toString() + "Vote Party Commands"),
    GUI_ITEM_REWARDS(ChatColor.LIGHT_PURPLE.toString() + "Item Rewards"),
    GUI_VOTE_LINKS(ChatColor.LIGHT_PURPLE.toString() + "Vote Links"),
    GUI_VOTE_LINKS_LORE(ChatColor.GRAY.toString() + "Place items in this inventory;;" + ChatColor.GRAY + "Right-click to edit an item"),
    GUI_VOTE_LINKS_INVENTORY(ChatColor.LIGHT_PURPLE.toString() + "Vote Links Inventory"),
    GUI_VOTE_BROADCAST(ChatColor.LIGHT_PURPLE.toString() + "Vote Broadcast"),
    GUI_MILESTONE_BROADCAST(ChatColor.LIGHT_PURPLE.toString() + "Milestone Broadcast"),
    GUI_BROADCAST_VOTE_PARTY_UNTIL(ChatColor.LIGHT_PURPLE.toString() + "VoteParty Votes Broadcast"),
    GUI_BROADCAST_VOTE_PARTY_COUNT(ChatColor.LIGHT_PURPLE.toString() + "VoteParty Count Broadcast"),
    GUI_BROADCAST_VOTE_PARTY_COUNTDOWN_END(ChatColor.LIGHT_PURPLE.toString() + "VoteParty Count Ending Broadcast"),
    GUI_VOTE_STAND_BREAK(ChatColor.LIGHT_PURPLE.toString() + "Break Armorstand Message"),
    GUI_DISABLED_WORLD_MESSAGE(ChatColor.LIGHT_PURPLE.toString() + "Disabled World Message"),
    GUI_VOTE_REMINDER(ChatColor.LIGHT_PURPLE.toString() + "Hourly Vote Reminder"),
    GUI_VOTE_CRATE_ADD(ChatColor.GREEN.toString() + "Add Crate"),
    GUI_VOTE_CRATE_RENAME(ChatColor.LIGHT_PURPLE.toString() + "Change Name"),
    GUI_VOTE_CRATE_KEY(ChatColor.AQUA.toString() + "Get Key"),
    GUI_VOTE_CRATE_REWARDS_PERCENTAGE_X(ChatColor.LIGHT_PURPLE.toString() + "Crate Rewards %X%%"),
    GUI_MILESTONE_REWARDS_X("Milestone Item Rewards #%X%"),

    SECOND_TYPE_MULTIPLE("seconds"),

    RESET_VOTES_MONTHLY("Reset monthly votes?"),
    RESET_VOTES_ALL("Reset ALL votes?"),
    RESET_VOTES_CANCEL(ChatColor.RED.toString() + "Votes are not reset."),
    RESET_VOTES_CONFIRM(ChatColor.GREEN.toString() + "A new month has started and votes can be reset in the /votesettings."),

    VOTE_SETTINGS_DISABLED(ChatColor.RED.toString() + "The /votesettings GUI has been disabled. You can change this in the settings.yml."),
    VOTE_SETTINGS_GENERAL(ChatColor.AQUA.toString() + "General"),
    VOTE_SETTINGS_REWARDS(ChatColor.LIGHT_PURPLE.toString() + "Rewards"),
    VOTE_SETTINGS_MESSAGES(ChatColor.YELLOW.toString() + "Messages"),
    VOTE_SETTINGS_SUPPORT(ChatColor.GREEN.toString() + "Support"),

    VOTE_CRATE_REWARDS_NAME_XY("%X%% Rewards '%Y%'"),
    VOTE_CRATE_NO_PRICE(ChatColor.RED.toString() + "No price!"),
    VOTE_CRATE_NO_PRICE_MESSAGE(ChatColor.RED.toString() + "No price this time :\"("),
    VOTE_CRATE_REWARD_X(ChatColor.GREEN.toString() + "You received a " + ChatColor.AQUA + "%X%% " + ChatColor.GREEN + "chance reward!"),
    VOTE_CRATE_EMPTY(ChatColor.RED.toString() + "There are no rewards inside this crate..."),
    VOTE_CRATE_NAME_DEFAULT_X("Crate %X%"),
    VOTE_CRATE_NAME_X(ChatColor.LIGHT_PURPLE.toString() + "%X%"),
    VOTE_CRATE_LORE_X(ChatColor.GRAY.toString() + "#%X%"),
    VOTE_CRATE_DELETED_X(ChatColor.RED.toString() + "%X% deleted."),
    VOTE_CRATE_PLACE_HERE_X("Place '%X%' here?"),

    PLAYER_INFO_VOTES_X(ChatColor.GRAY.toString() + "Votes: " + ChatColor.LIGHT_PURPLE + "%X%"),
    PLAYER_INFO_MONTHLY_VOTES_X(ChatColor.GRAY.toString() + "Monthly votes: " + ChatColor.LIGHT_PURPLE + "%X%"),
    PLAYER_INFO_LAST_X(ChatColor.GRAY.toString() + "Last vote: " + ChatColor.LIGHT_PURPLE + "%X%"),
    PLAYER_INFO_PERMISSION_X(ChatColor.GRAY.toString() + "Permission rewards: " + ChatColor.LIGHT_PURPLE + "%X%"),

    INPUT_CANCEL_BACK(ChatColor.GRAY.toString() + "Type 'cancel' to go back"),
    INPUT_CANCEL_CONTINUE(ChatColor.GRAY.toString() + "Type 'cancel' to continue"),
    INPUT_ADDED_TO_LIST_XY(ChatColor.GREEN.toString() + "Added %X% to %Y%"),
    INPUT_REMOVED_FROM_LIST_XY(ChatColor.RED.toString() + "Removed %X% from %Y%"),
    INPUT_ALTER_LIST_X(ChatColor.GREEN.toString() + "Please enter a %X% to add or remove from the list"),
    INPUT_LIST_EMPTY_X(ChatColor.RED.toString() + "There are currently no %X%."),
    INPUT_COMMANDS(ChatColor.GRAY.toString() + "Commands:"),
    INPUT_COMMAND_TYPE("command"),
    INPUT_COMMAND_TYPE_MULTIPLE("commands"),
    INPUT_COMMANDS_ADD_LORE(ChatColor.GREEN.toString() + "(with %PLAYER% as placeholder)"),
    INPUT_COMMAND_FORBIDDEN(ChatColor.RED.toString() + "This command is forbidden."),
    INPUT_PERMISSION_TYPE("permission"),
    INPUT_PERMISSION_TYPE_MULTIPLE("permissions"),
    INPUT_VOTE_LINK_TITLE(ChatColor.GREEN.toString() + "Enter a title for this item (with & colors)"),
    INPUT_VOTE_LINK_LORE(ChatColor.GREEN.toString() + "Add lore (subtext) to this item"),
    INPUT_VOTE_LINK_MORE_LORE(ChatColor.GREEN.toString() + "Add more lore (subtext) to this item"),
    INPUT_VOTE_LINK_URL(ChatColor.GREEN.toString() + "Add a link to this item"),
    INPUT_VOTE_CRATE_NAME_CHANGE(ChatColor.GREEN.toString() + "Please enter a new name"),
    INPUT_VOTE_CRATE_NAME_CHANGED_X(ChatColor.GREEN.toString() + "Name changed to \'%X%\'!"),
    INPUT_VOTE_CRATE_DELETED_X(ChatColor.RED.toString() + "%X% deleted!"),
    INPUT_MILESTONE(ChatColor.GREEN.toString() + "Please enter a milestone number"),

    ENABLED_USERS_LORE(ChatColor.GRAY.toString() + "This setting overrides;;" + ChatColor.GRAY + "the group permissions."),
    PERMISSION_REWARDS(ChatColor.LIGHT_PURPLE.toString() + "Permission Rewards"),

    MILESTONE_SETTINGS_TITLE_X("Vote Milestone Settings #%X%"),
    MILESTONE_NAME_X("Milestone #%X%"),
    MILESTONE_ADD(ChatColor.GREEN.toString() + "Add Milestone"),
    MILESTONE_DELETED_X(ChatColor.RED.toString() + "%X% deleted!"),

    CREATE_SUCCESS_X(ChatColor.GREEN.toString() + "%X% created!"),
    UPDATE_NOTHING_CHANGED(ChatColor.GRAY.toString() + "Nothing changed!"),
    UPDATE_SUCCESS_X(ChatColor.GREEN.toString() + "Successfully updated the %X%!"),
    UPDATE_FAIL_X(ChatColor.RED.toString() + "Failed to update the %X%!"),

    VOTE_PARTY_COMMAND_USAGE(ChatColor.RED.toString() + "- /voteparty create | start"),
    VOTE_PARTY_CHEST(ChatColor.LIGHT_PURPLE.toString() + "Vote Party Chest"),
    VOTE_PARTY_CHEST_LORE(ChatColor.GRAY.toString() + "Place this chest somewhere in the sky.;" + ChatColor.GRAY + "The contents of this chest" + " will;" + ChatColor.GRAY + "start dropping when the voteparty starts."),
    VOTE_PARTY_CHEST_RECEIVE(ChatColor.GREEN.toString() + "You have been given the Vote Party Chest."),
    VOTE_PARTY_CHEST_DELETED_X(ChatColor.RED.toString() + "Vote Party Chest #%X% deleted."),
    VOTE_PARTY_CHEST_CREATED_X(ChatColor.GREEN.toString() + "Vote Party Chest #%X% registered."),
    VOTE_PARTY_CHEST_NONE(ChatColor.RED.toString() + "There are no registered Vote Party Chests."),

    MONTHLY_VOTES_RESET_COMMAND_USAGE(ChatColor.RED.toString() + "- /clearmonthlyvotes <name>"),
    MONTHLY_VOTES_RESET_SELF(ChatColor.GREEN.toString() + "Your monthly votes have been reset."),
    MONTHLY_VOTES_RESET_OTHER_X(ChatColor.AQUA.toString() + "%X%\'s" + ChatColor.GREEN + " monthly votes have been reset."),

    VOTE_TOP_STAND_NAME("Vote Stand"),
    VOTE_TOP_STAND_CREATED_X(ChatColor.GREEN.toString() + "Registered Vote Stand #%X%"),
    VOTE_TOP_STAND_DELETED_X(ChatColor.RED.toString() + "Unregistered Vote Stand #%X%"),
    VOTE_TOP_SIGN_CREATED_X(ChatColor.GREEN.toString() + "Registered Vote Sign #%X%"),
    VOTE_TOP_SIGN_DELETED_X(ChatColor.RED.toString() + "Unregistered Vote Sign #%X%"),
    VOTE_TOP_CREATE_COMMAND_USAGE(ChatColor.RED.toString() + "- /createtop <top>"),
    VOTE_TOP_CREATE_ERROR("Error while creating vote top"),
    VOTE_TOP_DELETE_COMMAND_USAGE(ChatColor.RED.toString() + "- /deletetop <top>"),
    VOTE_TOP_DELETE_ERROR("Error while deleting vote top"),

    STATISTICS_VOTE_TOP_SITES(ChatColor.LIGHT_PURPLE.toString() + "Top vote sites"),
    STATISTICS_VOTE_TOP_SITES_LORE(ChatColor.GRAY.toString() + "Top vote sites by;" + ChatColor.GRAY + "CustomVoting users:;"),
    STATISTICS_VOTE_TOP_SITES_LORE_END(";;" + ChatColor.GRAY + "RED = not setup yet"),
    STATISTICS_MC_VERSION(ChatColor.LIGHT_PURPLE.toString() + "Minecraft Version"),
    STATISTICS_MC_VERSION_LORE_X(ChatColor.GRAY.toString() + "Most popular version: " + ChatColor.GREEN + "%X%"),
    STATISTICS_COUNTRY(ChatColor.LIGHT_PURPLE.toString() + "Country"),
    STATISTICS_COUNTRY_LORE(ChatColor.GRAY.toString() + "Most popular in:;"),

    SUPPORT_DISCORD_CHAT(ChatColor.AQUA.toString() + "Join the Discord server:"),
    SUPPORT_DISCORD_CHAT_URL(ChatColor.GREEN.toString() + "https://discord.gg/v3qmJu7jWD"),
    SUPPORT_DISCORD(ChatColor.LIGHT_PURPLE.toString() + "Discord"),
    SUPPORT_DISCORD_LORE(ChatColor.GRAY.toString() + "Join the Discord server"),
    SUPPORT_DATABASE(ChatColor.LIGHT_PURPLE.toString() + "Database"),
    SUPPORT_DONATORS(ChatColor.LIGHT_PURPLE.toString() + "Donators"),
    SUPPORT_DONATORS_LORE(ChatColor.GRAY.toString() + "CustomVoting supporters!"),
    SUPPORT_PLAYER_INFO(ChatColor.LIGHT_PURPLE.toString() + "Player Info"),
    SUPPORT_PLAYER_INFO_LORE(ChatColor.GRAY.toString() + "Player vote information"),
    SUPPORT_STATISTICS(ChatColor.LIGHT_PURPLE.toString() + "Statistics"),
    SUPPORT_STATISTICS_LORE(ChatColor.GRAY.toString() + "CustomVoting BStats"),
    SUPPORT_UP_TO_DATE(ChatColor.LIGHT_PURPLE.toString() + "Up to date?"),
    SUPPORT_LATEST_X(ChatColor.GRAY.toString() + "Latest: " + ChatColor.GREEN + "%X%;;" + ChatColor.GRAY + "Click to download"),
    SUPPORT_INGAME_UPDATE(ChatColor.LIGHT_PURPLE.toString() + "Ingame Updates"),
    SUPPORT_MERGE_DUPLICATES(ChatColor.RED.toString() + "Merge Duplicates"),
    SUPPORT_MERGE_DUPLICATES_L0RE_X(ChatColor.GRAY.toString() + "Find and merge duplicate;" + ChatColor.GRAY + "playerfiles;" + ChatColor.GRAY + "Currently: " + ChatColor.GREEN + "%X% files"),
    VOTE_FILES_DELETED_X(ChatColor.RED.toString() + "Deleted %X% votefiles!"),

    FAKE_VOTE_WEBSITE("fakevote.com"),
    FAKE_VOTE_COMMAND_USAGE(ChatColor.RED.toString() + "- /fakevote <name> [website]"),

    INSPECT_VOTE_COMMAND_USAGE(ChatColor.RED.toString() + "- /inspectvote <name> [website]"),

    RELOAD_START_X(ChatColor.GRAY.toString() + "Reloading configuration" + "%X%" + "..."),
    RELOAD_FINISH_X(ChatColor.GREEN.toString() + "Configuration" + "%X%" + " reloaded!"),
    RELOAD_FAIL(ChatColor.RED.toString() + "Could not reload configuration!"),

    PLUGIN_UPDATE(ChatColor.GRAY.toString() + "Updates can be turned off in the /votesettings"),

    VOTES_SET_SELF_X(ChatColor.GREEN.toString() + "Your votes have been set to " + ChatColor.AQUA + "%X%" + ChatColor.GREEN + "."),
    VOTES_SET_OTHER_XY(ChatColor.AQUA.toString() + "%X%'s " + ChatColor.GREEN + "votes have been set to " + ChatColor.AQUA + "%Y%" + ChatColor.GREEN + "."),
    VOTES_SET_COMMAND_USAGE(ChatColor.RED.toString() + "- /setvotes <amount> [name]"),

    QUEUE_DELETE_ERROR_XY("Failed to delete queue of %X%|%Y%"),
    QUEUE_FORWARD_XY("%X% queued votes found for %Y%. Forwarding in 10 seconds...");

    fun with(x: String, y: String? = null): String
    {
        return value.replace("%X%", x).replaceIfNotNull("%Y%", y)
    }

    override fun toString(): String
    {
        return value
    }
}
