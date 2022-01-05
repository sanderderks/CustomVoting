package me.sd_master92.customvoting.gui.messages

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Settings
import me.sd_master92.customvoting.constants.enumerations.Materials
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.GUI
import me.sd_master92.customvoting.gui.VoteSettings
import me.sd_master92.customvoting.gui.items.BaseItem
import me.sd_master92.customvoting.gui.items.StatusItem
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class MessageSettings(private val plugin: CV) : GUI(plugin, "Message Settings", 9, false, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Materials.SOUL_TORCH.get() ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(VoteLinks(plugin).inventory)
            }
            Material.CHEST             ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(Settings.VOTE_LINK_INVENTORY, !plugin.config.getBoolean(Settings.VOTE_LINK_INVENTORY))
                plugin.config.saveConfig()
                event.currentItem = UseVoteLinkItem(plugin)
            }
            Material.DIAMOND           ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.DISABLED_BROADCAST_VOTE,
                    !plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE)
                )
                plugin.config.saveConfig()
                event.currentItem = VoteBroadcast(plugin)
            }
            Material.ENDER_PEARL       ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.DISABLED_BROADCAST_STREAK,
                    !plugin.config.getBoolean(Settings.DISABLED_BROADCAST_STREAK)
                )
                plugin.config.saveConfig()
                event.currentItem = StreakBroadcast(plugin)
            }
            Material.BOOKSHELF         ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.DISABLED_BROADCAST_VOTE_PARTY_UNTIL,
                    !plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE_PARTY_UNTIL)
                )
                plugin.config.saveConfig()
                event.currentItem = VotePartyUntilBroadcast(plugin)
            }
            Material.NOTE_BLOCK        ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN,
                    !plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN)
                )
                plugin.config.saveConfig()
                event.currentItem = VotePartyCountBroadcast(plugin)
            }
            Material.FIREWORK_ROCKET   ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING,
                    !plugin.config.getBoolean(Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING)
                )
                plugin.config.saveConfig()
                event.currentItem = VotePartyCountEndBroadcast(plugin)
            }
            Material.ARMOR_STAND       ->
            {
                SoundType.CHANGE.play(plugin, player)
                plugin.config.set(
                    Settings.DISABLED_BROADCAST_ARMOR_STAND,
                    !plugin.config.getBoolean(Settings.DISABLED_BROADCAST_ARMOR_STAND)
                )
                plugin.config.saveConfig()
                event.currentItem = ArmorStandBreakMessage(plugin)
            }
            Material.BARRIER           ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
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
        inventory.setItem(8, BACK_ITEM)
    }
}

class UseVoteLinkItem(plugin: CV) : StatusItem(
    plugin,
    Material.CHEST, "Vote Links Inventory",
    Settings.VOTE_LINK_INVENTORY
)

class VoteBroadcast(plugin: CV) : StatusItem(
    plugin,
    Material.DIAMOND, "Vote Broadcast",
    Settings.DISABLED_BROADCAST_VOTE,
    true
)

class StreakBroadcast(plugin: CV) : StatusItem(
    plugin,
    Material.ENDER_PEARL, "Streak Broadcast",
    Settings.DISABLED_BROADCAST_STREAK,
    true
)

class VotePartyUntilBroadcast(plugin: CV) : StatusItem(
    plugin,
    Material.BOOKSHELF, "VoteParty Votes Broadcast",
    Settings.DISABLED_BROADCAST_VOTE_PARTY_UNTIL,
    true
)

class VotePartyCountBroadcast(plugin: CV) : StatusItem(
    plugin,
    Material.NOTE_BLOCK, "VoteParty Count Broadcast",
    Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN,
    true
)

class VotePartyCountEndBroadcast(plugin: CV) : StatusItem(
    plugin,
    Material.FIREWORK_ROCKET, "VoteParty Count Ending Broadcast",
    Settings.DISABLED_BROADCAST_VOTE_PARTY_COUNTDOWN_ENDING,
    true
)

class ArmorStandBreakMessage(plugin: CV) : StatusItem(
    plugin,
    Material.ARMOR_STAND, "Break Armorstand Message",
    Settings.DISABLED_BROADCAST_ARMOR_STAND,
    true
)