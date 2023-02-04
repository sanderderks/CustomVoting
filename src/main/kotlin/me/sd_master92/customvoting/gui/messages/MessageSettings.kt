package me.sd_master92.customvoting.gui.messages

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.VMaterial
import me.sd_master92.customvoting.gui.VoteSettings
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class MessageSettings(private val plugin: CV) : GUI(plugin, PMessage.MESSAGE_SETTINGS_INVENTORY_NAME.toString(), 18)
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
        addItem(object : BaseItem(
            VMaterial.SOUL_TORCH.get(),
            PMessage.VOTE_LINKS_ITEM_NAME.toString(),
            PMessage.VOTE_LINKS_ITEM_LORE.toString()
        )
        {
            override fun onClick(event: InventoryClickEvent, player: Player)
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                VoteLinks(plugin).open(player)
            }
        })
        addItem(UseVoteLinkItem(plugin))
        addItem(VoteBroadcast(plugin))
        addItem(MilestoneBroadcast(plugin))
        addItem(VotePartyUntilBroadcast(plugin))
        addItem(VotePartyCountBroadcast(plugin))
        addItem(VotePartyCountEndBroadcast(plugin))
        addItem(ArmorStandBreakMessage(plugin))
        addItem(DisabledWorldMessage(plugin))
        addItem(VoteRemindMessage(plugin))
    }
}

class UseVoteLinkItem(private val plugin: CV) : StatusItem(
    Material.CHEST, PMessage.VOTE_LINKS_ITEM_NAME_GUI.toString(),
    plugin.config, Setting.VOTE_LINK_INVENTORY.path
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(
            Setting.VOTE_LINK_INVENTORY.path,
            !plugin.config.getBoolean(Setting.VOTE_LINK_INVENTORY.path)
        )
        plugin.config.saveConfig()
        event.currentItem = UseVoteLinkItem(plugin)
    }
}

class VoteBroadcast(private val plugin: CV) : StatusItem(
    Material.DIAMOND, PMessage.VOTE_ITEM_NAME_BROADCAST.toString(),
    plugin.config, Setting.DISABLED_BROADCAST_VOTE.path,
    true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(
            Setting.DISABLED_BROADCAST_VOTE.path,
            !plugin.config.getBoolean(Setting.DISABLED_BROADCAST_VOTE.path)
        )
        plugin.config.saveConfig()
        event.currentItem = VoteBroadcast(plugin)
    }
}

class MilestoneBroadcast(private val plugin: CV) : StatusItem(
    Material.ENDER_PEARL, PMessage.MILESTONE_ITEM_NAME_BROADCAST.toString(),
    plugin.config, Setting.DISABLED_BROADCAST_MILESTONE.path,
    true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(
            Setting.DISABLED_BROADCAST_MILESTONE.path,
            !plugin.config.getBoolean(Setting.DISABLED_BROADCAST_MILESTONE.path)
        )
        plugin.config.saveConfig()
        event.currentItem = MilestoneBroadcast(plugin)
    }
}

class VotePartyUntilBroadcast(private val plugin: CV) : StatusItem(
    Material.BOOKSHELF, PMessage.VOTE_PARTY_ITEM_NAME_BROADCAST_UNTIL.toString(),
    plugin.config, Setting.DISABLED_BROADCAST_VOTE_PARTY_UNTIL.path,
    true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(
            Setting.DISABLED_BROADCAST_VOTE_PARTY_UNTIL.path,
            !plugin.config.getBoolean(Setting.DISABLED_BROADCAST_VOTE_PARTY_UNTIL.path)
        )
        plugin.config.saveConfig()
        event.currentItem = VotePartyUntilBroadcast(plugin)
    }
}

class VotePartyCountBroadcast(private val plugin: CV) : StatusItem(
    Material.NOTE_BLOCK, PMessage.VOTE_PARTY_ITEM_NAME_BROADCAST_COUNT.toString(),
    plugin.config, Setting.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN.path,
    true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(
            Setting.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN.path,
            !plugin.config.getBoolean(Setting.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN.path)
        )
        plugin.config.saveConfig()
        event.currentItem = VotePartyCountBroadcast(plugin)
    }
}

class VotePartyCountEndBroadcast(private val plugin: CV) : StatusItem(
    Material.FIREWORK_ROCKET, PMessage.VOTE_PARTY_ITEM_NAME_BROADCAST_COUNTDOWN_END.toString(),
    plugin.config, Setting.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING.path,
    true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(
            Setting.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING.path,
            !plugin.config.getBoolean(Setting.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING.path)
        )
        plugin.config.saveConfig()
        event.currentItem = VotePartyCountEndBroadcast(plugin)
    }
}

class ArmorStandBreakMessage(private val plugin: CV) : StatusItem(
    Material.ARMOR_STAND, PMessage.VOTE_TOP_ITEM_NAME_STAND_BREAK_MESSAGE.toString(),
    plugin.config, Setting.DISABLED_MESSAGE_ARMOR_STAND.path,
    true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(
            Setting.DISABLED_MESSAGE_ARMOR_STAND.path,
            !plugin.config.getBoolean(Setting.DISABLED_MESSAGE_ARMOR_STAND.path)
        )
        plugin.config.saveConfig()
        event.currentItem = ArmorStandBreakMessage(plugin)
    }
}

class DisabledWorldMessage(private val plugin: CV) : StatusItem(
    Material.GRASS_BLOCK, PMessage.DISABLED_WORLD_ITEM_NAME_MESSAGE.toString(),
    plugin.config, Setting.DISABLED_MESSAGE_DISABLED_WORLD.path,
    true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(
            Setting.DISABLED_MESSAGE_DISABLED_WORLD.path,
            !plugin.config.getBoolean(Setting.DISABLED_MESSAGE_DISABLED_WORLD.path)
        )
        plugin.config.saveConfig()
        event.currentItem = DisabledWorldMessage(plugin)
    }
}

class VoteRemindMessage(private val plugin: CV) : StatusItem(
    Material.OAK_SIGN, PMessage.VOTE_REMINDER_ITEM_NAME.toString(),
    plugin.config, Setting.DISABLED_MESSAGE_VOTE_REMINDER.path,
    true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.set(
            Setting.DISABLED_MESSAGE_VOTE_REMINDER.path,
            !plugin.config.getBoolean(Setting.DISABLED_MESSAGE_VOTE_REMINDER.path)
        )
        plugin.config.saveConfig()
        event.currentItem = VoteRemindMessage(plugin)
    }
}