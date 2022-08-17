package me.sd_master92.customvoting.gui.rewards.crate

import me.sd_master92.core.input.PlayerStringInput
import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.addToInventoryOrDrop
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.items.ItemsRewardItem
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class VoteCrateSettings(private val plugin: CV, private val number: Int) : GUI(
    plugin,
    (plugin.data.getString(Data.VOTE_CRATES + ".$number.name") ?: "Vote Crate") + " #$number",
    9,
    false,
    true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER  ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(VoteCrates(plugin).inventory)
            }

            Material.RED_WOOL ->
            {
                SoundType.FAILURE.play(plugin, player)
                plugin.data.delete(Data.VOTE_CRATES + ".$number")
                cancelCloseEvent()
                player.openInventory(VoteCrates(plugin).inventory)
            }

            Material.CHEST    ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                try
                {
                    player.openInventory(
                        VoteCrateItemRewards(
                            plugin,
                            number,
                            item.itemMeta?.displayName?.filter { it.isDigit() }?.toInt() ?: Data.CRATE_REWARD_CHANCES[0]
                        ).inventory
                    )
                } catch (_: Exception)
                {
                }
            }

            Material.DIAMOND  ->
            {
                SoundType.SUCCESS.play(plugin, player)
                player.addToInventoryOrDrop(
                    BaseItem(
                        Material.TRIPWIRE_HOOK,
                        ChatColor.AQUA.toString() + plugin.data.getString(Data.VOTE_CRATES + ".$number.name") + " | crate key #$number",
                        null,
                        true
                    )
                )
            }

            Material.OAK_SIGN ->
            {
                SoundType.CHANGE.play(plugin, player)
                cancelCloseEvent()
                player.closeInventory()
                player.sendMessage(ChatColor.GREEN.toString() + "Please enter a new name")
                player.sendMessage(ChatColor.GRAY.toString() + "Type 'cancel' to go back")
                object : PlayerStringInput(plugin, player)
                {
                    override fun onInputReceived(input: String)
                    {
                        SoundType.SUCCESS.play(plugin, player)
                        plugin.data.set(Data.VOTE_CRATES + ".$number.name", input)
                        plugin.data.saveConfig()
                        player.sendMessage(ChatColor.GREEN.toString() + "Name changed to $input!")
                        player.openInventory(VoteCrateSettings(plugin, number).inventory)
                        cancel()
                    }

                    override fun onCancel()
                    {
                        SoundType.FAILURE.play(plugin, player)
                        player.openInventory(VoteCrateSettings(plugin, number).inventory)
                    }
                }
            }

            else              ->
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
        val DELETE_ITEM = BaseItem(Material.RED_WOOL, ChatColor.RED.toString() + "Delete")
    }

    init
    {
        inventory.addItem(BaseItem(Material.OAK_SIGN, ChatColor.LIGHT_PURPLE.toString() + "Change Name"))
        inventory.addItem(BaseItem(Material.DIAMOND, ChatColor.AQUA.toString() + "Get Key", null, true))
        for (chance in Data.CRATE_REWARD_CHANCES)
        {
            inventory.addItem(
                ItemsRewardItem(
                    plugin,
                    "${Data.VOTE_CRATES}.$number.${Data.ITEM_REWARDS}.$chance",
                    "Crate Rewards $chance%"
                )
            )
        }
        inventory.setItem(7, DELETE_ITEM)
        inventory.setItem(8, BACK_ITEM)
    }
}