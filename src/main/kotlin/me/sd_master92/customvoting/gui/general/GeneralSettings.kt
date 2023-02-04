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

class GeneralSettings(private val plugin: CV) : GUI(plugin, PMessage.GENERAL_SETTINGS_INVENTORY_NAME.toString(), 18)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        VoteSettings(plugin).open(player)
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    init
    {
        addItem(MonthlyResetItem(plugin))
        addItem(MonthlyVotesItem(plugin))
        addItem(SoundEffectsItem(plugin))
        addItem(FireworkItem(plugin))
        addItem(LuckyVoteItem(plugin))
        addItem(DoVotePartyItem(plugin))
        addItem(VotePartyTypeItem(plugin))
        addItem(VotesUntilItem.getInstance(plugin))
        addItem(VotePartyCountdownItem(plugin))
        addItem(object : BaseItem(Material.GRASS_BLOCK, PMessage.DISABLED_WORLD_OVERVIEW_ITEM_NAME.toString())
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                DisabledWorlds(plugin).open(player)
            }

        })
        addItem(UUIDSupportItem(plugin))
    }
}

class VotesUntilItem private constructor(private val plugin: CV, votesRequired: Int, votesUntil: Int) : BaseItem(
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
            return VotesUntilItem(plugin, votesRequired, votesUntil)
        }
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        if (plugin.config.getNumber(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path) < 100)
        {
            plugin.config.addNumber(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path, 10)
        } else
        {
            plugin.config.setNumber(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path, 10)
        }
        event.currentItem = getInstance(plugin)
    }
}

class VotePartyTypeItem(private val plugin: CV) : BaseItem(
    Material.SPLASH_POTION, PMessage.VOTE_PARTY_ITEM_NAME_TYPE.toString(),
    PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(VotePartyType.valueOf(plugin.config.getNumber(Setting.VOTE_PARTY_TYPE.path)).label)
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.setNumber(
            Setting.VOTE_PARTY_TYPE.path,
            VotePartyType.next(plugin).value
        )
        event.currentItem = VotePartyTypeItem(plugin)
    }
}

class VotePartyCountdownItem(private val plugin: CV) : BaseItem(
    Material.ENDER_CHEST, PMessage.VOTE_PARTY_ITEM_NAME_COUNTDOWN.toString(),
    PMessage.GENERAL_ITEM_LORE_CURRENT_XY.with(
        "" + plugin.config.getNumber(Setting.VOTE_PARTY_COUNTDOWN.path),
        PMessage.TIME_UNIT_SECONDS_MULTIPLE.toString()
    )
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
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
}

class SoundEffectsItem(private val plugin: CV) : StatusItem(
    Material.MUSIC_DISC_CAT, PMessage.SOUND_EFFECTS_ITEM_NAME.toString(),
    plugin.config, Setting.USE_SOUND_EFFECTS.path
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config[Setting.USE_SOUND_EFFECTS.path] =
            !plugin.config.getBoolean(Setting.USE_SOUND_EFFECTS.path)
        plugin.config.saveConfig()
        event.currentItem = SoundEffectsItem(plugin)
    }
}

class MonthlyResetItem(private val plugin: CV) : StatusItem(
    Material.CLOCK, PMessage.RESET_VOTES_ITEM_NAME_MONTHLY.toString(),
    plugin.config, Setting.MONTHLY_RESET.path
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config[Setting.MONTHLY_RESET.path] = !plugin.config.getBoolean(Setting.MONTHLY_RESET.path)
        plugin.config.saveConfig()
        event.currentItem = MonthlyResetItem(plugin)
    }
}

class MonthlyVotesItem(private val plugin: CV) : StatusItem(
    Material.TNT, PMessage.MONTHLY_VOTES_ITEM_NAME.toString(),
    plugin.config, Setting.MONTHLY_VOTES.path
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config[Setting.MONTHLY_VOTES.path] = !plugin.config.getBoolean(Setting.MONTHLY_VOTES.path)
        plugin.config.saveConfig()
        event.currentItem = MonthlyVotesItem(plugin)
    }
}

class LuckyVoteItem(private val plugin: CV) : StatusItem(
    Material.TOTEM_OF_UNDYING, PMessage.LUCKY_VOTE_ITEM_NAME.toString(),
    plugin.config, Setting.LUCKY_VOTE.path
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config[Setting.LUCKY_VOTE.path] = !plugin.config.getBoolean(Setting.LUCKY_VOTE.path)
        plugin.config.saveConfig()
        event.currentItem = LuckyVoteItem(plugin)
    }
}

class FireworkItem(private val plugin: CV) : StatusItem(
    Material.FIREWORK_ROCKET, PMessage.FIREWORK_ITEM_NAME.toString(),
    plugin.config, Setting.FIREWORK.path
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config[Setting.FIREWORK.path] = !plugin.config.getBoolean(Setting.FIREWORK.path)
        plugin.config.saveConfig()
        event.currentItem = FireworkItem(plugin)
    }
}

class DoVotePartyItem(private val plugin: CV) : StatusItem(
    Material.EXPERIENCE_BOTTLE, PMessage.VOTE_PARTY_ITEM_NAME.toString(),
    plugin.config, Setting.VOTE_PARTY.path
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config[Setting.VOTE_PARTY.path] = !plugin.config.getBoolean(Setting.VOTE_PARTY.path)
        plugin.config.saveConfig()
        event.currentItem = DoVotePartyItem(plugin)
    }
}

class UUIDSupportItem(private val plugin: CV) : StatusItem(
    Material.PLAYER_HEAD, PMessage.UUID_STORAGE_ITEM_NAME.toString(),
    plugin.config, Setting.UUID_STORAGE.path
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config[Setting.UUID_STORAGE.path] = !plugin.config.getBoolean(Setting.UUID_STORAGE.path)
        plugin.config.saveConfig()
        event.currentItem = UUIDSupportItem(plugin)
    }
}