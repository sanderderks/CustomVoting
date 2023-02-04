package me.sd_master92.customvoting.gui.rewards.crate

import me.sd_master92.core.input.PlayerStringInput
import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.addToInventoryOrDrop
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.gui.items.ItemsRewardItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class CrateSettings(private val plugin: CV, private val number: Int) : GUI(
    plugin,
    (plugin.data.getString(Data.VOTE_CRATES.path + ".$number.name")
        ?: Strings.CRATE_NAME_DEFAULT_X.with("$number")),
    9
)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER  ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(Crates(plugin).inventory)
            }

            Material.RED_WOOL ->
            {
                SoundType.FAILURE.play(plugin, player)
                plugin.data.delete(Data.VOTE_CRATES.path + ".$number")
                player.sendMessage(Strings.CRATE_MESSAGE_DELETED_X.with(Strings.CRATE_NAME_X.with(name)))
                cancelCloseEvent = true
                player.openInventory(Crates(plugin).inventory)
            }

            Material.CHEST    ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                try
                {
                    player.openInventory(
                        CrateItemRewards(
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
                        Strings.CRATE_ITEM_NAME_KEY_X.with(
                            plugin.data.getString(Data.VOTE_CRATES.path + ".$number.name") ?: ""
                        ),
                        Strings.CRATE_ITEM_LORE_KEY_X.with("$number"),
                        true
                    )
                )
            }

            Material.OAK_SIGN ->
            {
                SoundType.CHANGE.play(plugin, player)
                cancelCloseEvent = true
                player.closeInventory()
                player.sendMessage(Strings.CRATE_MESSAGE_NAME_ENTER.toString())
                player.sendMessage(Strings.GENERAL_MESSAGE_CANCEL_BACK.toString())
                object : PlayerStringInput(plugin, player)
                {
                    override fun onInputReceived(input: String)
                    {
                        SoundType.SUCCESS.play(plugin, player)
                        plugin.data.set(Data.VOTE_CRATES.path + ".$number.name", input)
                        plugin.data.saveConfig()
                        player.sendMessage(Strings.CRATE_MESSAGE_NAME_CHANGED_X.with(input))
                        player.openInventory(CrateSettings(plugin, number).inventory)
                        val uuid = plugin.data.getString(Data.VOTE_CRATES.path + ".$number.stand")
                        if (uuid != null)
                        {
                            val entity = Bukkit.getEntity(UUID.fromString(uuid))
                            if (entity is ArmorStand)
                            {
                                entity.customName = Strings.CRATE_NAME_STAND_X.with(input)
                            }
                        }
                        cancel()
                    }

                    override fun onCancel()
                    {
                        SoundType.FAILURE.play(plugin, player)
                        player.openInventory(CrateSettings(plugin, number).inventory)
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
        val DELETE_ITEM = BaseItem(Material.RED_WOOL, Strings.GENERAL_ITEM_NAME_DELETE.toString())
    }

    init
    {
        inventory.addItem(BaseItem(Material.OAK_SIGN, Strings.CRATE_ITEM_NAME_RENAME.toString()))
        inventory.addItem(BaseItem(Material.DIAMOND, Strings.CRATE_ITEM_NAME_KEY_GET.toString(), null, true))
        for (chance in Data.CRATE_REWARD_CHANCES)
        {
            inventory.addItem(
                ItemsRewardItem(
                    plugin,
                    "${Data.VOTE_CRATES}.$number.${Data.ITEM_REWARDS}.$chance",
                    Strings.CRATE_ITEM_NAME_REWARDS_PERCENTAGE_X.with("$chance")
                )
            )
        }
        inventory.setItem(7, DELETE_ITEM)
        inventory.setItem(8, BACK_ITEM)
    }
}