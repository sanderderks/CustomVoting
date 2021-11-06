package me.sd_master92.customvoting.constants

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.constants.enumerations.VotePartyType
import me.sd_master92.customvoting.gui.GUI.Companion.createItem
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Settings
{
    const val MONTHLY_RESET = "monthly_reset"
    const val USE_SOUND_EFFECTS = "sound_effects"
    const val VOTE_PARTY_COUNTDOWN = "vote_party_countdown"
    const val FIREWORK = "firework"
    const val VOTE_PARTY = "vote_party"
    const val VOTE_PARTY_TYPE = "vote_party_type"
    const val VOTES_REQUIRED_FOR_VOTE_PARTY = "votes_required_for_vote_party"
    const val VOTE_REWARD_MONEY = "vote_reward_money"
    const val VOTE_REWARD_EXPERIENCE = "vote_reward_xp"
    const val LUCKY_VOTE = "lucky_vote"
    const val LUCKY_VOTE_CHANCE = "lucky_vote_chance"
    const val VOTE_LINK_INVENTORY = "vote_link_inventory"
    const val FORBIDDEN_COMMANDS = "forbidden_commands"
    const val INGAME_UPDATES = "ingame_updates"
    const val DISABLED_BROADCAST_VOTE = "disabled_broadcasts.vote"
    const val DISABLED_BROADCAST_VOTE_PARTY_UNTIL = "disabled_broadcasts.vote_party.until"
    const val DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN = "disabled_broadcasts.vote_party_countdown"
    const val DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING = "disabled_broadcasts.vote_party.countdown_ending"
    const val USE_DATABASE = "use_database"
    const val DATABASE = "database"
    private const val DATABASE_HOST = "$DATABASE.host"
    private const val DATABASE_PORT = "$DATABASE.port"
    private const val DATABASE_DATABASE = "$DATABASE.database"
    private const val DATABASE_USER = "$DATABASE.user"
    private const val DATABASE_PASSWORD = "$DATABASE.password"
    fun initialize(plugin: Main)
    {
        setDefault(plugin, MONTHLY_RESET, false)
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
        setDefault(plugin, DISABLED_BROADCAST_VOTE_PARTY_UNTIL, false)
        setDefault(plugin, DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN, false)
        setDefault(plugin, DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING, false)
        setDefault(plugin, USE_DATABASE, false)
        setDefault(plugin, DATABASE_HOST, "localhost")
        setDefault(plugin, DATABASE_PORT, 3306)
        setDefault(plugin, DATABASE_DATABASE, "customvoting")
        setDefault(plugin, DATABASE_USER, "root")
        setDefault(plugin, DATABASE_PASSWORD, "root")
        plugin.config.saveConfig()
    }

    private fun setDefault(plugin: Main, path: String, value: Any)
    {
        if (plugin.config[path] == null)
        {
            plugin.config[path] = value
        }
    }

    fun getDoMonthlyResetSetting(plugin: Main): ItemStack
    {
        return createItem(
            Material.CLOCK, ChatColor.LIGHT_PURPLE.toString() + "Monthly Reset",
            ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(MONTHLY_RESET)) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
        )
    }

    fun getUseSoundEffectsSetting(plugin: Main): ItemStack
    {
        return createItem(
            Material.MUSIC_DISC_CAT, ChatColor.LIGHT_PURPLE.toString() + "Sound Effects",
            ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(USE_SOUND_EFFECTS)) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
        )
    }

    fun getUseFireworkSetting(plugin: Main): ItemStack
    {
        return createItem(
            Material.FIREWORK_ROCKET, ChatColor.LIGHT_PURPLE.toString() + "Firework",
            ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(FIREWORK)) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
        )
    }

    fun getDoVotePartySetting(plugin: Main): ItemStack
    {
        return createItem(
            Material.EXPERIENCE_BOTTLE, ChatColor.LIGHT_PURPLE.toString() + "Vote Party",
            ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(VOTE_PARTY)) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
        )
    }

    fun getDoLuckyVoteSetting(plugin: Main): ItemStack
    {
        return createItem(
            Material.TOTEM_OF_UNDYING, ChatColor.LIGHT_PURPLE.toString() + "Lucky Vote",
            ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(LUCKY_VOTE)) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
        )
    }

    fun getVotePartyTypeSetting(plugin: Main): ItemStack
    {
        return createItem(
            Material.SPLASH_POTION, ChatColor.LIGHT_PURPLE.toString() + "Vote Party Type",
            ChatColor.GRAY.toString() + "Status: " + ChatColor.AQUA + VotePartyType.valueOf(
                plugin.config.getNumber(
                    VOTE_PARTY_TYPE
                )
            ).label
        )
    }

    fun getVotesUntilVotePartySetting(plugin: Main): ItemStack
    {
        val votesRequired = plugin.config.getNumber(VOTES_REQUIRED_FOR_VOTE_PARTY)
        val votesUntil = votesRequired - plugin.data.getNumber(Data.CURRENT_VOTES)
        return createItem(
            Material.ENCHANTED_BOOK, ChatColor.LIGHT_PURPLE.toString() + "Votes until Vote Party",
            ChatColor.GRAY.toString() + "Required: " + ChatColor.AQUA + votesRequired + ";" + ChatColor.GRAY + "Votes left:" +
                    " " + ChatColor.GREEN + votesUntil
        )
    }

    fun getVotePartyCountdownSetting(plugin: Main): ItemStack
    {
        return createItem(
            Material.ENDER_CHEST, ChatColor.LIGHT_PURPLE.toString() + "Vote Party Countdown",
            ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.config.getNumber(VOTE_PARTY_COUNTDOWN)
        )
    }

    fun getLuckyVoteChanceSetting(plugin: Main): ItemStack
    {
        return createItem(
            Material.ENDER_EYE, ChatColor.LIGHT_PURPLE.toString() + "Lucky Vote Chance",
            ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.config.getNumber(LUCKY_VOTE_CHANCE) + ChatColor.GRAY + "%"
        )
    }

    fun getUseVoteLinksInventory(plugin: Main): ItemStack
    {
        return createItem(
            Material.CHEST, ChatColor.LIGHT_PURPLE.toString() + "Vote Links Inventory",
            ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(VOTE_LINK_INVENTORY)) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
        )
    }

    fun getMoneyRewardSetting(plugin: Main): ItemStack
    {
        return createItem(
            Material.GOLD_INGOT, ChatColor.LIGHT_PURPLE.toString() + "Money Reward",
            if (Main.economy != null) ChatColor.GRAY.toString() + "Currently: " + ChatColor.GREEN + Main.economy!!.format(
                plugin.config.getDouble(VOTE_REWARD_MONEY)
            ) else ChatColor.RED.toString() + "Disabled"
        )
    }

    fun getExperienceRewardSetting(plugin: Main): ItemStack
    {
        return createItem(
            Material.EXPERIENCE_BOTTLE, ChatColor.LIGHT_PURPLE.toString() + "XP Reward",
            ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.config.getNumber(VOTE_REWARD_EXPERIENCE) + ChatColor.GRAY + " levels"
        )
    }

    fun getDoIngameUpdatesSetting(plugin: Main): ItemStack
    {
        return createItem(
            Material.FILLED_MAP, ChatColor.LIGHT_PURPLE.toString() + "Ingame Updates",
            ChatColor.GRAY.toString() + "Status: " + if (plugin.config.getBoolean(INGAME_UPDATES)) ChatColor.GREEN.toString() + "ON" else ChatColor.RED.toString() + "OFF"
        )
    }
}