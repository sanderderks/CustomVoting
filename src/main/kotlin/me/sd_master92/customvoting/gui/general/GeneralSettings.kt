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

class GeneralSettings(private val plugin: CV) : GUI(plugin, PMessage.GENERAL_SETTINGS_INVENTORY_NAME.toString(), 18)
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
                plugin.config[Setting.MONTHLY_RESET.path] = !plugin.config.getBoolean(Setting.MONTHLY_RESET.path)
                plugin.config.saveConfig()
                event.currentItem = MonthlyResetItem(plugin)
            }

            Material.TNT               ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Setting.MONTHLY_VOTES.path] = !plugin.config.getBoolean(Setting.MONTHLY_VOTES.path)
                plugin.config.saveConfig()
                event.currentItem = MonthlyVotesItem(plugin)
            }

            Material.MUSIC_DISC_CAT    ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Setting.USE_SOUND_EFFECTS.path] =
                    !plugin.config.getBoolean(Setting.USE_SOUND_EFFECTS.path)
                plugin.config.saveConfig()
                event.currentItem = SoundEffectsItem(plugin)
            }

            Material.FIREWORK_ROCKET   ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Setting.FIREWORK.path] = !plugin.config.getBoolean(Setting.FIREWORK.path)
                plugin.config.saveConfig()
                event.currentItem = FireworkItem(plugin)
            }

            Material.EXPERIENCE_BOTTLE ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Setting.VOTE_PARTY.path] = !plugin.config.getBoolean(Setting.VOTE_PARTY.path)
                plugin.config.saveConfig()
                event.currentItem = DoVotePartyItem(plugin)
            }

            Material.SPLASH_POTION     ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.setNumber(
                    Setting.VOTE_PARTY_TYPE.path,
                    VotePartyType.next(plugin).value
                )
                event.currentItem = VotePartyTypeItem(plugin)
            }

            Material.ENCHANTED_BOOK    ->
            {
                SoundType.CHANGE.play(plugin, player)
                if (plugin.config.getNumber(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path) < 100)
                {
                    plugin.config.addNumber(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path, 10)
                } else
                {
                    plugin.config.setNumber(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path, 10)
                }
                event.currentItem = VotesUntilItem.getInstance(plugin)
            }

            Material.ENDER_CHEST       ->
            {
                SoundType.CHANGE.play(plugin, player)
                if (plugin.config.getNumber(Setting.VOTE_PARTY_COUNTDOWN.path) < 60)
                {
                    plugin.config.addNumber(Setting.VOTE_PARTY_COUNTDOWN.path, 10)
                } else
                {
                    plugin.config.setNumber(Setting.VOTE_PARTY_COUNTDOWN.path, 0)
                }
                event.currentItem = VotePartyCountdownItem(plugin)
            }

            Material.TOTEM_OF_UNDYING  ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config[Setting.LUCKY_VOTE.path] = !plugin.config.getBoolean(Setting.LUCKY_VOTE.path)
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
                plugin.config[Setting.UUID_STORAGE.path] = !plugin.config.getBoolean(Setting.UUID_STORAGE.path)
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
        inventory.addItem(BaseItem(Material.GRASS_BLOCK, PMessage.DISABLED_WORLD_OVERVIEW_ITEM_NAME.toString()))
        inventory.addItem(UUIDSupportItem(plugin))
        inventory.setItem(17, BACK_ITEM)
    }
}

class VotesUntilItem private constructor(votesRequired: Int, votesUntil: Int) : BaseItem(
    Material.ENCHANTED_BOOK,
    PMessage.VOTE_PARTY_ITEM_NAME_VOTES_UNTIL.toString(),
    PMessage.VOTE_PARTY_ITEM_LORE_VOTES_UNTIL_XY.with("$votesRequired", "$votesUntil")
)
{
    companion object
    {
        fun getInstance(plugin: CV): VotesUntilItem
        {
            val votesRequired = plugin.config.getNumber(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path)
            val votesUntil = votesRequired - plugin.data.getNumber(Data.CURRENT_VOTES.path)
            return VotesUntilItem(votesRequired, votesUntil)
        }
    }
}

class VotePartyTypeItem(plugin: CV) : BaseItem(
    Material.SPLASH_POTION, PMessage.VOTE_PARTY_ITEM_NAME_TYPE.toString(),
    PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(VotePartyType.valueOf(plugin.config.getNumber(Setting.VOTE_PARTY_TYPE.path)).label)
)

class VotePartyCountdownItem(plugin: CV) : BaseItem(
    Material.ENDER_CHEST, PMessage.VOTE_PARTY_ITEM_NAME_COUNTDOWN.toString(),
    PMessage.GENERAL_ITEM_LORE_CURRENT_XY.with(
        "" + plugin.config.getNumber(Setting.VOTE_PARTY_COUNTDOWN.path),
        PMessage.TIME_UNIT_SECONDS_MULTIPLE.toString()
    )
)

class SoundEffectsItem(plugin: CV) : StatusItem(
    Material.MUSIC_DISC_CAT, PMessage.SOUND_EFFECTS_ITEM_NAME.toString(),
    plugin.config, Setting.USE_SOUND_EFFECTS.path
)

class MonthlyResetItem(plugin: CV) : StatusItem(
    Material.CLOCK, PMessage.RESET_VOTES_ITEM_NAME_MONTHLY.toString(),
    plugin.config, Setting.MONTHLY_RESET.path
)

class MonthlyVotesItem(plugin: CV) : StatusItem(
    Material.TNT, PMessage.MONTHLY_VOTES_ITEM_NAME.toString(),
    plugin.config, Setting.MONTHLY_VOTES.path
)

class LuckyVoteItem(plugin: CV) : StatusItem(
    Material.TOTEM_OF_UNDYING, PMessage.LUCKY_VOTE_ITEM_NAME.toString(),
    plugin.config, Setting.LUCKY_VOTE.path
)

class FireworkItem(plugin: CV) : StatusItem(
    Material.FIREWORK_ROCKET, PMessage.FIREWORK_ITEM_NAME.toString(),
    plugin.config, Setting.FIREWORK.path
)

class DoVotePartyItem(plugin: CV) : StatusItem(
    Material.EXPERIENCE_BOTTLE, PMessage.VOTE_PARTY_ITEM_NAME.toString(),
    plugin.config, Setting.VOTE_PARTY.path
)

class UUIDSupportItem(plugin: CV) : StatusItem(
    Material.PLAYER_HEAD, PMessage.UUID_STORAGE_ITEM_NAME.toString(),
    plugin.config, Setting.UUID_STORAGE.path
)