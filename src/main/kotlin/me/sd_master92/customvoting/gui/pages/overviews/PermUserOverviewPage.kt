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
import me.sd_master92.customvoting.gui.pages.settings.RewardSettingsPage
import me.sd_master92.customvoting.stripColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class PermUserOverviewPage(private val plugin: CV, private var page: Int = 0) :
    GUI(plugin, PMessage.PERM_USER_OVERVIEW_INVENTORY_NAME_X.with("" + (page + 1)), 54)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        RewardSettingsPage(plugin, true).open(player)
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        if (event.currentItem!!.type == Material.PLAYER_HEAD)
        {
            SoundType.CHANGE.play(plugin, player)
            var name = event.currentItem!!.itemMeta?.displayName?.stripColor()
            if (name == null)
            {
                name = ""
            }
            val voter = Voter.getByName(plugin, name)
            if (voter != null)
            {
                voter.setIsOpUser(!voter.isOpUser)
                event.currentItem = getSkull(voter)
            } else
            {
                event.currentItem = null
            }
        }
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
        meta!!.lore = listOf(
            PMessage.GENERAL_ITEM_LORE_ENABLED_X.with(
                if (voter.isOpUser)
                    PMessage.GENERAL_VALUE_YES.toString() else PMessage.GENERAL_VALUE_NO.toString()
            )
        )
        meta.lore!!.addAll(PMessage.PERM_USER_OVERVIEW_ITEM_LORE.toString().split(";;"))
        if (meta.displayName != voter.name)
        {
            meta.setDisplayName(PMessage.PLAYER_ITEM_NAME_SKULL_X.with(voter.name))
        }
        skull.itemMeta = meta
        return skull
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
            addItem(getSkull(voter))
        }
        setItem(51, object : PaginationPreviousAction(plugin, this, page)
        {
            override fun onPrevious(player: Player, newPage: Int)
            {
                PermUserOverviewPage(plugin, newPage).open(player)
            }
        })
        setItem(52, object : PaginationNextAction(plugin, this, page, Voter.getTopVoters(plugin).size)
        {
            override fun onNext(player: Player, newPage: Int)
            {
                PermUserOverviewPage(plugin, newPage).open(player)
            }
        })
    }
}
