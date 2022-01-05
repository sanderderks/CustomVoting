package me.sd_master92.customvoting.gui

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.VotePartyType
import me.sd_master92.customvoting.gui.items.*
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
        inventory.setItem(17, BACK_ITEM)
    }
}