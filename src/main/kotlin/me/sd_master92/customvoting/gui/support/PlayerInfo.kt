package me.sd_master92.customvoting.gui.support

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
import java.util.*

class PlayerInfo(private val plugin: CV, private var page: Int = 0) :
    GUI(plugin, Strings.GUI_TITLE_PLAYER_INFO_X.with("" + (page + 1)), 54)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.openInventory(Support(plugin).inventory)
            }

            else             ->
            {
                if (item.itemMeta?.displayName?.contains(Strings.GUI_PAGINATION_PREVIOUS.toString()) == true && page > 0)
                {
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent = true
                    player.openInventory(PlayerInfo(plugin, page - 1).inventory)
                } else if (item.itemMeta?.displayName?.contains(Strings.GUI_PAGINATION_NEXT.toString()) == true && Voter.getTopVoters(
                        plugin
                    ).size > (page + 1) * 51
                )
                {
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent = true
                    player.openInventory(PlayerInfo(plugin, page + 1).inventory)
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
        for (voter in Voter.getTopVoters(plugin).filterIndexed { i, _ -> i >= page * 51 }.take(51))
        {
            inventory.addItem(InfoPlayer(voter).getSkull())
        }
        inventory.setItem(51, BaseItem(Material.FEATHER, Strings.GUI_PAGINATION_PREVIOUS.toString()))
        inventory.setItem(52, BaseItem(Material.FEATHER, Strings.GUI_PAGINATION_NEXT.toString()))
        inventory.setItem(53, BACK_ITEM)
    }
}

class InfoPlayer(private val voter: Voter)
{
    fun getSkull(): ItemStack
    {
        val skull = voter.name.getOfflinePlayer().getSkull()
        val meta = skull.itemMeta
        val lastVote = if (voter.votes > 0) java.text.SimpleDateFormat(("dd-M-yyyy HH:mm:ss"))
            .format(Date(voter.last)) else Strings.NEVER.toString()
        meta!!.lore = listOf(
            Strings.PLAYER_INFO_VOTES_X.with("" + voter.votes),
            Strings.PLAYER_INFO_MONTHLY_VOTES_X.with("" + voter.monthlyVotes),
            Strings.PLAYER_INFO_LAST_X.with(lastVote),
            Strings.PLAYER_INFO_PERMISSION_X.with("" + voter.isOpUser),
        )
        if (meta.displayName != voter.name)
        {
            meta.setDisplayName(ChatColor.AQUA.toString() + voter.name)
        }
        skull.itemMeta = meta
        return skull
    }
}
