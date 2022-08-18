package me.sd_master92.customvoting.gui.messages

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Materials
import me.sd_master92.customvoting.constants.enumerations.Settings
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.VoteSettings
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class MessageSettings(private val plugin: CV) : GUI(plugin, "Message Settings", 18)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Materials.SOUL_TORCH.get() ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(VoteLinks(plugin).inventory)
            }

            Material.CHEST             ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.VOTE_LINK_INVENTORY.path,
                    !plugin.config.getBoolean(Settings.VOTE_LINK_INVENTORY.path)
                )
                plugin.config.saveConfig()
                event.currentItem = UseVoteLinkItem(plugin)
            }

            Material.DIAMOND           ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.DISABLED_BROADCAST_VOTE.path,
                    !plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE.path)
                )
                plugin.config.saveConfig()
                event.currentItem = VoteBroadcast(plugin)
            }

            Material.ENDER_PEARL       ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.DISABLED_BROADCAST_STREAK.path,
                    !plugin.config.getBoolean(Settings.DISABLED_BROADCAST_STREAK.path)
                )
                plugin.config.saveConfig()
                event.currentItem = StreakBroadcast(plugin)
            }

            Material.BOOKSHELF         ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.DISABLED_BROADCAST_VOTE_PARTY_UNTIL.path,
                    !plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE_PARTY_UNTIL.path)
                )
                plugin.config.saveConfig()
                event.currentItem = VotePartyUntilBroadcast(plugin)
            }

            Material.NOTE_BLOCK        ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN.path,
                    !plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN.path)
                )
                plugin.config.saveConfig()
                event.currentItem = VotePartyCountBroadcast(plugin)
            }

            Material.FIREWORK_ROCKET   ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING.path,
                    !plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING.path)
                )
                plugin.config.saveConfig()
                event.currentItem = VotePartyCountEndBroadcast(plugin)
            }

            Material.ARMOR_STAND       ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.DISABLED_MESSAGE_ARMOR_STAND.path,
                    !plugin.config.getBoolean(Settings.DISABLED_MESSAGE_ARMOR_STAND.path)
                )
                plugin.config.saveConfig()
                event.currentItem = ArmorStandBreakMessage(plugin)
            }

            Material.GRASS_BLOCK       ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.DISABLED_MESSAGE_DISABLED_WORLD.path,
                    !plugin.config.getBoolean(Settings.DISABLED_MESSAGE_DISABLED_WORLD.path)
                )
                plugin.config.saveConfig()
                event.currentItem = DisabledWorldMessage(plugin)
            }

            Material.OAK_SIGN          ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.DISABLED_MESSAGE_VOTE_REMINDER.path,
                    !plugin.config.getBoolean(Settings.DISABLED_MESSAGE_VOTE_REMINDER.path)
                )
                plugin.config.saveConfig()
                event.currentItem = VoteRemindMessage(plugin)
            }

            Material.BARRIER           ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(VoteSettings(plugin).inventory)
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

    companion object
    {
        val VOTE_LINKS = BaseItem(
            Materials.SOUL_TORCH.get(), ChatColor.LIGHT_PURPLE.toString() + "Vote Links",
            ChatColor.GRAY.toString() + "Place items in this inventory;;" + ChatColor.GRAY + "Right-click to edit an item"
        )
    }

    init
    {
        inventory.addItem(VOTE_LINKS)
        inventory.addItem(UseVoteLinkItem(plugin))
        inventory.addItem(VoteBroadcast(plugin))
        inventory.addItem(StreakBroadcast(plugin))
        inventory.addItem(VotePartyUntilBroadcast(plugin))
        inventory.addItem(VotePartyCountBroadcast(plugin))
        inventory.addItem(VotePartyCountEndBroadcast(plugin))
        inventory.addItem(ArmorStandBreakMessage(plugin))
        inventory.addItem(DisabledWorldMessage(plugin))
        inventory.addItem(VoteRemindMessage(plugin))
        inventory.setItem(17, BACK_ITEM)
    }
}

class UseVoteLinkItem(plugin: CV) : StatusItem(
    Material.CHEST, "Vote Links Inventory",
    plugin.config, Settings.VOTE_LINK_INVENTORY.path
)

class VoteBroadcast(plugin: CV) : StatusItem(
    Material.DIAMOND, "Vote Broadcast",
    plugin.config, Settings.DISABLED_BROADCAST_VOTE.path,
    true
)

class StreakBroadcast(plugin: CV) : StatusItem(
    Material.ENDER_PEARL, "Streak Broadcast",
    plugin.config, Settings.DISABLED_BROADCAST_STREAK.path,
    true
)

class VotePartyUntilBroadcast(plugin: CV) : StatusItem(
    Material.BOOKSHELF, "VoteParty Votes Broadcast",
    plugin.config, Settings.DISABLED_BROADCAST_VOTE_PARTY_UNTIL.path,
    true
)

class VotePartyCountBroadcast(plugin: CV) : StatusItem(
    Material.NOTE_BLOCK, "VoteParty Count Broadcast",
    plugin.config, Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN.path,
    true
)

class VotePartyCountEndBroadcast(plugin: CV) : StatusItem(
    Material.FIREWORK_ROCKET, "VoteParty Count Ending Broadcast",
    plugin.config, Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING.path,
    true
)

class ArmorStandBreakMessage(plugin: CV) : StatusItem(
    Material.ARMOR_STAND, "Break Armorstand Message",
    plugin.config, Settings.DISABLED_MESSAGE_ARMOR_STAND.path,
    true
)

class DisabledWorldMessage(plugin: CV) : StatusItem(
    Material.GRASS_BLOCK, "Disabled World Message",
    plugin.config, Settings.DISABLED_MESSAGE_DISABLED_WORLD.path,
    true
)

class VoteRemindMessage(plugin: CV) : StatusItem(
    Material.OAK_SIGN, "Hourly Vote Reminder",
    plugin.config, Settings.DISABLED_MESSAGE_VOTE_REMINDER.path,
    true
)