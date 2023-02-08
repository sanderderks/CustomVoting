package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.getOfflinePlayer
import me.sd_master92.customvoting.getSkull
import me.sd_master92.customvoting.gui.buttons.actions.PaginationNextAction
import me.sd_master92.customvoting.gui.buttons.actions.PaginationPreviousAction
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class PlayerInfoOverviewPage(private val plugin: CV, backPage: GUI?, private var page: Int = 0) :
    GUI(plugin, backPage, PMessage.PLAYER_INFO_INVENTORY_NAME_X.with("" + (page + 1)), 54)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
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

    private fun getSkull(voter: Voter): ItemStack
    {
        val skull = voter.name.getOfflinePlayer().getSkull()
        val meta = skull.itemMeta
        val lastVote = if (voter.votes > 0) java.text.SimpleDateFormat(PMessage.GENERAL_FORMAT_DATE.toString())
            .format(Date(voter.last)) else PMessage.PLAYER_INFO_VALUE_NEVER.toString()
        meta!!.lore = listOf(
            PMessage.PLAYER_INFO_ITEM_LORE_VOTES_X.with("" + voter.votes),
            PMessage.PLAYER_INFO_ITEM_LORE_MONTHLY_VOTES_X.with("" + voter.monthlyVotes),
            PMessage.PLAYER_INFO_ITEM_LORE_LAST_X.with(lastVote),
            PMessage.PLAYER_INFO_ITEM_LORE_PERMISSION_X.with(
                if (voter.isOpUser) PMessage.RED.toString() + voter.isOpUser else
                    PMessage.PURPLE.toString() + voter.isOpUser
            ),
        )
        if (meta.displayName != voter.name)
        {
            meta.setDisplayName(PMessage.AQUA.toString() + voter.name)
        }
        skull.itemMeta = meta
        return skull
    }

    init
    {
        for (voter in Voter.getTopVoters(plugin).filterIndexed { i, _ -> i >= page * 51 }.take(51))
        {
            addItem(getSkull(voter))
        }
        setItem(51, object : PaginationPreviousAction(plugin, this, page)
        {
            override fun onPrevious(player: Player, newPage: Int)
            {
                PlayerInfoOverviewPage(plugin, backPage, newPage).open(player)
            }
        })
        setItem(52, object : PaginationNextAction(plugin, this, page, Voter.getTopVoters(plugin).size)
        {
            override fun onNext(player: Player, newPage: Int)
            {
                PlayerInfoOverviewPage(plugin, backPage, newPage).open(player)
            }
        })
    }
}
