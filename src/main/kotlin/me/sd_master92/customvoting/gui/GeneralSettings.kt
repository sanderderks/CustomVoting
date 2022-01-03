package me.sd_master92.customvoting.gui

import me.sd_master92.customvoting.Main
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.VotePartyType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class GeneralSettings(private val plugin: Main) : GUI(plugin, "General Settings", 18, false, true)
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
                event.currentItem = Settings.getDoMonthlyResetSetting(plugin)
            }
            Material.TNT               ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.MONTHLY_PERIOD] = !plugin.config.getBoolean(Settings.MONTHLY_PERIOD)
                plugin.config.saveConfig()
                event.currentItem = Settings.getDoMonthlyPeriodSetting(plugin)
            }
            Material.MUSIC_DISC_CAT    ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.USE_SOUND_EFFECTS] = !plugin.config.getBoolean(Settings.USE_SOUND_EFFECTS)
                plugin.config.saveConfig()
                event.currentItem = Settings.getUseSoundEffectsSetting(plugin)
            }
            Material.FIREWORK_ROCKET   ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.FIREWORK] = !plugin.config.getBoolean(Settings.FIREWORK)
                plugin.config.saveConfig()
                event.currentItem = Settings.getUseFireworkSetting(plugin)
            }
            Material.EXPERIENCE_BOTTLE ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.VOTE_PARTY] = !plugin.config.getBoolean(Settings.VOTE_PARTY)
                plugin.config.saveConfig()
                event.currentItem = Settings.getDoVotePartySetting(plugin)
            }
            Material.SPLASH_POTION     ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.setNumber(
                    Settings.VOTE_PARTY_TYPE,
                    VotePartyType.next(plugin).value
                )
                event.currentItem = Settings.getVotePartyTypeSetting(plugin)
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
                event.currentItem = Settings.getVotesUntilVotePartySetting(plugin)
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
                event.currentItem = Settings.getVotePartyCountdownSetting(plugin)
            }
            Material.TOTEM_OF_UNDYING  ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Settings.LUCKY_VOTE] = !plugin.config.getBoolean(Settings.LUCKY_VOTE)
                plugin.config.saveConfig()
                event.currentItem = Settings.getDoLuckyVoteSetting(plugin)
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
        inventory.addItem(Settings.getDoMonthlyResetSetting(plugin))
        inventory.addItem(Settings.getDoMonthlyPeriodSetting(plugin))
        inventory.addItem(Settings.getUseSoundEffectsSetting(plugin))
        inventory.addItem(Settings.getUseFireworkSetting(plugin))
        inventory.addItem(Settings.getDoLuckyVoteSetting(plugin))
        inventory.addItem(Settings.getDoVotePartySetting(plugin))
        inventory.addItem(Settings.getVotePartyTypeSetting(plugin))
        inventory.addItem(Settings.getVotesUntilVotePartySetting(plugin))
        inventory.addItem(Settings.getVotePartyCountdownSetting(plugin))
        inventory.setItem(17, BACK_ITEM)
    }
}