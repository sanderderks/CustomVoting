package me.sd_master92.customvoting.gui.support

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Voter
import me.sd_master92.customvoting.constants.enumerations.SoundType
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
    GUI(plugin, "Player Info #${page + 1}", 54, false, true)
{
    override fun onClick(event: InventoryClickEvent, player: Player, item: ItemStack)
    {
        when (item.type)
        {
            Material.BARRIER ->
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent()
                player.openInventory(Support(plugin).inventory)
            }

            else             ->
            {
                if (item.itemMeta?.displayName?.contains("Previous") == true && page > 0)
                {
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent()
                    player.openInventory(PlayerInfo(plugin, page - 1).inventory)
                } else if (item.itemMeta?.displayName?.contains("Next") == true && Voter.getTopVoters(plugin).size > (page + 1) * 51)
                {
                    SoundType.CLICK.play(plugin, player)
                    cancelCloseEvent()
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
        inventory.setItem(51, BaseItem(Material.FEATHER, ChatColor.AQUA.toString() + "Previous"))
        inventory.setItem(52, BaseItem(Material.FEATHER, ChatColor.AQUA.toString() + "Next"))
        inventory.setItem(53, BACK_ITEM)
    }
}

class InfoPlayer(private val voter: Voter)
{
    fun getSkull(): ItemStack
    {
        val skull = voter.name.getOfflinePlayer().getSkull()
        val meta = skull.itemMeta
        val lastVote = java.text.SimpleDateFormat(("dd-M-yyyy HH:mm:ss")).format(Date(voter.last))
        meta!!.lore = listOf(
            ChatColor.GRAY.toString() + "Votes: " + ChatColor.LIGHT_PURPLE + voter.votes,
            ChatColor.GRAY.toString() + "Monthly votes: " + ChatColor.LIGHT_PURPLE + voter.monthlyVotes,
            ChatColor.GRAY.toString() + "Last vote: " + ChatColor.LIGHT_PURPLE + lastVote,
            ChatColor.GRAY.toString() + "Permission rewards: " + ChatColor.LIGHT_PURPLE + voter.isOpUser,
        )
        if (meta.displayName != voter.name)
        {
            meta.setDisplayName(ChatColor.AQUA.toString() + voter.name)
        }
        skull.itemMeta = meta
        return skull
    }
}
