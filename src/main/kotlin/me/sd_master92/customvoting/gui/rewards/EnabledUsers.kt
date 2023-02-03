package me.sd_master92.customvoting.gui.rewards

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.Strings
import me.sd_master92.customvoting.getOfflinePlayer
import me.sd_master92.customvoting.getSkull
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class EnabledUsers(private val plugin: CV, private var page: Int = 0) :
    GUI(plugin, Strings.GUI_TITLE_ENABLED_USERS_X.with("" + (page + 1)), 54)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER     ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(RewardSettings(plugin, true).inventory)
            }

            Material.PLAYER_HEAD ->
            {
                SoundType.CHANGE.play(plugin, player)
                var name = ChatColor.stripColor(item.itemMeta?.displayName)
                if (name == null)
                {
                    name = ""
                }
                val voter = Voter.getByName(plugin, name)
                if (voter != null)
                {
                    voter.setIsOpUser(!voter.isOpUser)
                    event.currentItem = EnabledUser(voter).getSkull()
                } else
                {
                    event.currentItem = null
                }
            }

            else                 ->
            {
                if (item.itemMeta?.displayName?.contains(Strings.GUI_PAGINATION_PREVIOUS.toString()) == true && page > 0)
                {
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent = true
                    player.openInventory(EnabledUsers(plugin, page - 1).inventory)
                } else if (item.itemMeta?.displayName?.contains(Strings.GUI_PAGINATION_NEXT.toString()) == true && Voter.getTopVoters(
                        plugin
                    ).size > (page + 1) * 51
                )
                {
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent = true
                    player.openInventory(EnabledUsers(plugin, page + 1).inventory)
                }
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    init
    {
        val voters = Voter.getTopVoters(plugin)
        if (page < 0 || voters.size < 51 * page)
        {
            page = 0
        }
        for (voter in voters.asSequence().filterIndexed { i, _ -> i >= 51 * page }.take(51))
        {
            inventory.addItem(EnabledUser(voter).getSkull())
        }
        inventory.setItem(51, BaseItem(Material.FEATHER, Strings.GUI_PAGINATION_PREVIOUS.toString()))
        inventory.setItem(52, BaseItem(Material.FEATHER, Strings.GUI_PAGINATION_NEXT.toString()))
        inventory.setItem(53, BACK_ITEM)
    }
}

class EnabledUser(private val voter: Voter)
{
    fun getSkull(): ItemStack
    {
        val skull = voter.name.getOfflinePlayer().getSkull()
        val meta = skull.itemMeta
        meta!!.lore = listOf(
            Strings.GUI_ENABLED_X.with(
                if (voter.isOpUser)
                    Strings.YES.toString() else Strings.NO.toString()
            )
        )
        meta.lore!!.addAll(Strings.ENABLED_USERS_LORE.toString().split(";;"))
        if (meta.displayName != voter.name)
        {
            meta.setDisplayName(Strings.PLAYER_SKULL_NAME_X.with(voter.name))
        }
        skull.itemMeta = meta
        return skull
    }
}
