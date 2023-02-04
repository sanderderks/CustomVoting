package me.sd_master92.customvoting.gui.general

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.*
import me.sd_master92.customvoting.gui.VoteSettings
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class GeneralSettings(private val plugin: CV) : GUI(plugin, Strings.GENERAL_SETTINGS_INVENTORY_NAME.toString(), 18)
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
        inventory.addItem(BaseItem(Material.GRASS_BLOCK, Strings.DISABLED_WORLD_OVERVIEW_ITEM_NAME.toString()))
        inventory.addItem(UUIDSupportItem(plugin))
        inventory.setItem(17, BACK_ITEM)
    }
}

class VotesUntilItem private constructor(votesRequired: Int, votesUntil: Int) : BaseItem(
    Material.ENCHANTED_BOOK,
    Strings.VOTE_PARTY_ITEM_NAME_VOTES_UNTIL.toString(),
    Strings.VOTE_PARTY_ITEM_LORE_VOTES_UNTIL_XY.with("$votesRequired", "$votesUntil")
)
{
    companion object
    {
        fun getInstance(plugin: CV): VotesUntilItem
        {
            val votesRequired = plugin.config.getNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY.path)
            val votesUntil = votesRequired - plugin.data.getNumber(Data.CURRENT_VOTES.path)
            return VotesUntilItem(votesRequired, votesUntil)
        }
    }
}

class VotePartyTypeItem(plugin: CV) : BaseItem(
    Material.SPLASH_POTION, Strings.VOTE_PARTY_ITEM_NAME_TYPE.toString(),
    Strings.GENERAL_ITEM_LORE_CURRENT_X.with(VotePartyType.valueOf(plugin.config.getNumber(Settings.VOTE_PARTY_TYPE.path)).label)
)

class VotePartyCountdownItem(plugin: CV) : BaseItem(
    Material.ENDER_CHEST, Strings.VOTE_PARTY_ITEM_NAME_COUNTDOWN.toString(),
    Strings.GENERAL_ITEM_LORE_CURRENT_XY.with(
        "" + plugin.config.getNumber(Settings.VOTE_PARTY_COUNTDOWN.path),
        Strings.TIME_UNIT_SECONDS_MULTIPLE.toString()
    )
)

class SoundEffectsItem(plugin: CV) : StatusItem(
    Material.MUSIC_DISC_CAT, Strings.SOUND_EFFECTS_ITEM_NAME.toString(),
    plugin.config, Settings.USE_SOUND_EFFECTS.path
)

class MonthlyResetItem(plugin: CV) : StatusItem(
    Material.CLOCK, Strings.RESET_VOTES_ITEM_NAME_MONTHLY.toString(),
    plugin.config, Settings.MONTHLY_RESET.path
)

class MonthlyVotesItem(plugin: CV) : StatusItem(
    Material.TNT, Strings.MONTHLY_VOTES_ITEM_NAME.toString(),
    plugin.config, Settings.MONTHLY_VOTES.path
)

class LuckyVoteItem(plugin: CV) : StatusItem(
    Material.TOTEM_OF_UNDYING, Strings.LUCKY_VOTE_ITEM_NAME.toString(),
    plugin.config, Settings.LUCKY_VOTE.path
)

class FireworkItem(plugin: CV) : StatusItem(
    Material.FIREWORK_ROCKET, Strings.FIREWORK_ITEM_NAME.toString(),
    plugin.config, Settings.FIREWORK.path
)

class DoVotePartyItem(plugin: CV) : StatusItem(
    Material.EXPERIENCE_BOTTLE, Strings.VOTE_PARTY_ITEM_NAME.toString(),
    plugin.config, Settings.VOTE_PARTY.path
)

class UUIDSupportItem(plugin: CV) : StatusItem(
    Material.PLAYER_HEAD, Strings.UUID_STORAGE_ITEM_NAME.toString(),
    plugin.config, Settings.UUID_STORAGE.path
)