package me.sd_master92.customvoting.gui.general

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.VotePartyType
import me.sd_master92.customvoting.gui.VoteSettings
import me.sd_master92.customvoting.gui.items.StatusItem
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class GeneralSettings(private val plugin: CV) : GUI(plugin, "General Settings", 18, false, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER           ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(VoteSettings(plugin).inventory)
            }
            Material.CLOCK             ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.MONTHLY_RESET] = !plugin.config.getBoolean(Settings.MONTHLY_RESET)
                plugin.config.saveConfig()
                event.currentItem = MonthlyResetItem(plugin)
            }
            Material.TNT               ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.MONTHLY_PERIOD] = !plugin.config.getBoolean(Settings.MONTHLY_PERIOD)
                plugin.config.saveConfig()
                event.currentItem = MonthlyPeriodItem(plugin)
            }
            Material.MUSIC_DISC_CAT    ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.USE_SOUND_EFFECTS] = !plugin.config.getBoolean(Settings.USE_SOUND_EFFECTS)
                plugin.config.saveConfig()
                event.currentItem = SoundEffectsItem(plugin)
            }
            Material.FIREWORK_ROCKET   ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.FIREWORK] = !plugin.config.getBoolean(Settings.FIREWORK)
                plugin.config.saveConfig()
                event.currentItem = FireworkItem(plugin)
            }
            Material.EXPERIENCE_BOTTLE ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.VOTE_PARTY] = !plugin.config.getBoolean(Settings.VOTE_PARTY)
                plugin.config.saveConfig()
                event.currentItem = DoVotePartyItem(plugin)
            }
            Material.SPLASH_POTION     ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.setNumber(
                    Settings.VOTE_PARTY_TYPE,
                    VotePartyType.next(plugin).value
                )
                event.currentItem = VotePartyTypeItem(plugin)
            }
            Material.ENCHANTED_BOOK    ->
            {
                SoundType.CHANGE.play(plugin, player)
                if (plugin.config.getNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY) < 100)
                {
                    plugin.config.addNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY, 10)
                } else
                {
                    plugin.config.setNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY, 10)
                }
                event.currentItem = VotesUntilItem.getInstance(plugin)
            }
            Material.ENDER_CHEST       ->
            {
                SoundType.CHANGE.play(plugin, player)
                if (plugin.config.getNumber(Settings.VOTE_PARTY_COUNTDOWN) < 60)
                {
                    plugin.config.addNumber(Settings.VOTE_PARTY_COUNTDOWN, 10)
                } else
                {
                    plugin.config.setNumber(Settings.VOTE_PARTY_COUNTDOWN, 0)
                }
                event.currentItem = VotePartyCountdownItem(plugin)
            }
            Material.TOTEM_OF_UNDYING  ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.LUCKY_VOTE] = !plugin.config.getBoolean(Settings.LUCKY_VOTE)
                plugin.config.saveConfig()
                event.currentItem = LuckyVoteItem(plugin)
            }
            Material.GRASS_BLOCK       ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(DisabledWorlds(plugin).inventory)
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
        inventory.addItem(MonthlyPeriodItem(plugin))
        inventory.addItem(SoundEffectsItem(plugin))
        inventory.addItem(FireworkItem(plugin))
        inventory.addItem(LuckyVoteItem(plugin))
        inventory.addItem(DoVotePartyItem(plugin))
        inventory.addItem(VotePartyTypeItem(plugin))
        inventory.addItem(VotesUntilItem.getInstance(plugin))
        inventory.addItem(VotePartyCountdownItem(plugin))
        inventory.addItem(BaseItem(Material.GRASS_BLOCK, ChatColor.LIGHT_PURPLE.toString() + "Disabled Worlds"))
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
            val votesRequired = plugin.config.getNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY)
            val votesUntil = votesRequired - plugin.data.getNumber(Data.CURRENT_VOTES)
            return VotesUntilItem(votesRequired, votesUntil)
        }
    }
}

class VotePartyTypeItem(plugin: CV) : BaseItem(
    Material.SPLASH_POTION, ChatColor.LIGHT_PURPLE.toString() + "Vote Party Type",
    ChatColor.GRAY.toString() + "Status: " + ChatColor.AQUA + VotePartyType.valueOf(
        plugin.config.getNumber(
            Settings.VOTE_PARTY_TYPE
        )
    ).label
)

class VotePartyCountdownItem(plugin: CV) : BaseItem(
    Material.ENDER_CHEST, ChatColor.LIGHT_PURPLE.toString() + "Vote Party Countdown",
    ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.config.getNumber(Settings.VOTE_PARTY_COUNTDOWN)
)

class SoundEffectsItem(plugin: CV) : StatusItem(
    plugin,
    Material.MUSIC_DISC_CAT, "Sound Effects",
    Settings.USE_SOUND_EFFECTS
)

class MonthlyResetItem(plugin: CV) : StatusItem(
    plugin,
    Material.CLOCK, "Monthly Reset",
    Settings.MONTHLY_RESET
)

class MonthlyPeriodItem(plugin: CV) : StatusItem(
    plugin,
    Material.TNT, "Monthly Period",
    Settings.MONTHLY_PERIOD
)

class LuckyVoteItem(plugin: CV) : StatusItem(
    plugin,
    Material.TOTEM_OF_UNDYING, "Lucky Vote",
    Settings.LUCKY_VOTE
)

class FireworkItem(plugin: CV) : StatusItem(
    plugin,
    Material.FIREWORK_ROCKET, "Firework",
    Settings.FIREWORK
)

class DoVotePartyItem(plugin: CV) : StatusItem(
    plugin,
    Material.EXPERIENCE_BOTTLE, "Vote Party",
    Settings.VOTE_PARTY
)