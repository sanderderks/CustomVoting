package me.sd_master92.customvoting.gui.general

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.VotePartyType
import me.sd_master92.customvoting.gui.VoteSettings
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class GeneralSettings(private val plugin: CV) : GUI(plugin, "General Settings", 18)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER           ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(VoteSettings(plugin).inventory)
            }

            Material.CLOCK             ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.MONTHLY_RESET.path] = !plugin.config.getBoolean(Settings.MONTHLY_RESET.path)
                plugin.config.saveConfig()
                event.currentItem = MonthlyResetItem(plugin)
            }

            Material.TNT               ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.MONTHLY_VOTES.path] = !plugin.config.getBoolean(Settings.MONTHLY_VOTES.path)
                plugin.config.saveConfig()
                event.currentItem = MonthlyVotesItem(plugin)
            }

            Material.MUSIC_DISC_CAT    ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.USE_SOUND_EFFECTS.path] =
                    !plugin.config.getBoolean(Settings.USE_SOUND_EFFECTS.path)
                plugin.config.saveConfig()
                event.currentItem = SoundEffectsItem(plugin)
            }

            Material.FIREWORK_ROCKET   ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.FIREWORK.path] = !plugin.config.getBoolean(Settings.FIREWORK.path)
                plugin.config.saveConfig()
                event.currentItem = FireworkItem(plugin)
            }

            Material.EXPERIENCE_BOTTLE ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.VOTE_PARTY.path] = !plugin.config.getBoolean(Settings.VOTE_PARTY.path)
                plugin.config.saveConfig()
                event.currentItem = DoVotePartyItem(plugin)
            }

            Material.SPLASH_POTION     ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.setNumber(
                    Settings.VOTE_PARTY_TYPE.path,
                    VotePartyType.next(plugin).value
                )
                event.currentItem = VotePartyTypeItem(plugin)
            }

            Material.ENCHANTED_BOOK    ->
            {
                SoundType.CHANGE.play(plugin, player)
                if (plugin.config.getNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY.path) < 100)
                {
                    plugin.config.addNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY.path, 10)
                } else
                {
                    plugin.config.setNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY.path, 10)
                }
                event.currentItem = VotesUntilItem.getInstance(plugin)
            }

            Material.ENDER_CHEST       ->
            {
                SoundType.CHANGE.play(plugin, player)
                if (plugin.config.getNumber(Settings.VOTE_PARTY_COUNTDOWN.path) < 60)
                {
                    plugin.config.addNumber(Settings.VOTE_PARTY_COUNTDOWN.path, 10)
                } else
                {
                    plugin.config.setNumber(Settings.VOTE_PARTY_COUNTDOWN.path, 0)
                }
                event.currentItem = VotePartyCountdownItem(plugin)
            }

            Material.TOTEM_OF_UNDYING  ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.LUCKY_VOTE.path] = !plugin.config.getBoolean(Settings.LUCKY_VOTE.path)
                plugin.config.saveConfig()
                event.currentItem = LuckyVoteItem(plugin)
            }

            Material.GRASS_BLOCK       ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(DisabledWorlds(plugin).inventory)
            }

            Material.PLAYER_HEAD       ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.UUID_STORAGE.path] = !plugin.config.getBoolean(Settings.UUID_STORAGE.path)
                plugin.config.saveConfig()
                event.currentItem = UUIDSupportItem(plugin)
            }

            else                       ->
            {
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    init
    {
        inventory.addItem(MonthlyResetItem(plugin))
        inventory.addItem(MonthlyVotesItem(plugin))
        inventory.addItem(SoundEffectsItem(plugin))
        inventory.addItem(FireworkItem(plugin))
        inventory.addItem(LuckyVoteItem(plugin))
        inventory.addItem(DoVotePartyItem(plugin))
        inventory.addItem(VotePartyTypeItem(plugin))
        inventory.addItem(VotesUntilItem.getInstance(plugin))
        inventory.addItem(VotePartyCountdownItem(plugin))
        inventory.addItem(BaseItem(Material.GRASS_BLOCK, ChatColor.LIGHT_PURPLE.toString() + "Disabled Worlds"))
        inventory.addItem(UUIDSupportItem(plugin))
        inventory.setItem(17, BACK_ITEM)
    }
}

class VotesUntilItem private constructor(votesRequired: Int, votesUntil: Int) : BaseItem(
    Material.ENCHANTED_BOOK, ChatColor.LIGHT_PURPLE.toString() + "Votes until Vote Party",
    ChatColor.GRAY.toString() + "Required: " + ChatColor.AQUA + votesRequired + ";" + ChatColor.GRAY + "Votes left:" +
            " " + ChatColor.GREEN + votesUntil
)
{
    companion object
    {
        fun getInstance(plugin: CV): VotesUntilItem
        {
            val votesRequired = plugin.config.getNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY.path)
            val votesUntil = votesRequired - plugin.data.getNumber(Data.CURRENT_VOTES)
            return VotesUntilItem(votesRequired, votesUntil)
        }
    }
}

class VotePartyTypeItem(plugin: CV) : BaseItem(
    Material.SPLASH_POTION, ChatColor.LIGHT_PURPLE.toString() + "Vote Party Type",
    ChatColor.GRAY.toString() + "Status: " + ChatColor.AQUA + VotePartyType.valueOf(
        plugin.config.getNumber(
            Settings.VOTE_PARTY_TYPE.path
        )
    ).label
)

class VotePartyCountdownItem(plugin: CV) : BaseItem(
    Material.ENDER_CHEST, ChatColor.LIGHT_PURPLE.toString() + "Vote Party Countdown",
    ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.config.getNumber(Settings.VOTE_PARTY_COUNTDOWN.path)
)

class SoundEffectsItem(plugin: CV) : StatusItem(
    Material.MUSIC_DISC_CAT, "Sound Effects",
    plugin.config, Settings.USE_SOUND_EFFECTS.path
)

class MonthlyResetItem(plugin: CV) : StatusItem(
    Material.CLOCK, "Monthly Reset",
    plugin.config, Settings.MONTHLY_RESET.path
)

class MonthlyVotesItem(plugin: CV) : StatusItem(
    Material.TNT, "Monthly Votes",
    plugin.config, Settings.MONTHLY_VOTES.path
)

class LuckyVoteItem(plugin: CV) : StatusItem(
    Material.TOTEM_OF_UNDYING, "Lucky Vote",
    plugin.config, Settings.LUCKY_VOTE.path
)

class FireworkItem(plugin: CV) : StatusItem(
    Material.FIREWORK_ROCKET, "Firework",
    plugin.config, Settings.FIREWORK.path
)

class DoVotePartyItem(plugin: CV) : StatusItem(
    Material.EXPERIENCE_BOTTLE, "Vote Party",
    plugin.config, Settings.VOTE_PARTY.path
)

class UUIDSupportItem(plugin: CV) : StatusItem(
    Material.PLAYER_HEAD, ChatColor.RED.toString() + "UUID Storage",
    plugin.config, Settings.UUID_STORAGE.path
)