package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.inventory.GUIWithPagination
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.getOfflinePlayer
import me.sd_master92.customvoting.getSkull
import me.sd_master92.customvoting.hasPowerRewards
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class PlayerInfoOverviewPage(
    private val plugin: CV,
    backPage: GUI?,
    private val page: Int = 0,
    private val voters: List<Voter>
) :
    GUIWithPagination<Voter>(
        plugin,
        backPage,
        voters,
        { it.hashCode() },
        { _, item, _ -> getSkull(plugin, item) },
        page,
        PMessage.PLAYER_INFO_INVENTORY_NAME.toString(),
        PMessage.GENERAL_ITEM_NAME_NEXT.toString(),
        PMessage.GENERAL_ITEM_NAME_PREVIOUS.toString(),
        sortedByKey = false
    )
{
    override fun newInstance(page: Int): GUI
    {
        return PlayerInfoOverviewPage(plugin, backPage?.newInstance(), page, voters)
    }

    override fun newInstance(): GUI
    {
        return newInstance(page)
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    override fun onPaginate(player: Player, page: Int)
    {
        SoundType.CLICK.play(plugin, player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    companion object
    {
        suspend fun getSkull(plugin: CV, voter: Voter): ItemStack
        {
            val name = voter.getName()
            val player = name.getOfflinePlayer(plugin)
            val skull = player.getSkull()
            val meta = skull.itemMeta
            val votes = voter.getVotes()
            val last = voter.getLast()
            val lastVote =
                if (votes > 0 && last > 0) java.text.SimpleDateFormat(PMessage.GENERAL_FORMAT_DATE.toString())
                    .format(Date(last)) else PMessage.PLAYER_INFO_VALUE_NEVER.toString()
            meta!!.lore = listOf(
                PMessage.PLAYER_INFO_ITEM_LORE_VOTES_X.with("" + votes),
                PMessage.PLAYER_INFO_ITEM_LORE_VOTES_MONTHLY_X.with("" + voter.getVotesMonthly()),
                PMessage.PLAYER_INFO_ITEM_LORE_VOTES_WEEKLY_X.with("" + voter.getVotesWeekly()),
                PMessage.PLAYER_INFO_ITEM_LORE_VOTES_DAILY_X.with("" + voter.getVotesDaily()),
                PMessage.PLAYER_INFO_ITEM_LORE_STREAK_DAILY_X.with("" + voter.getStreakDaily()),
                PMessage.PLAYER_INFO_ITEM_LORE_LAST_X.with(lastVote),
                PMessage.PLAYER_INFO_ITEM_LORE_POWER_X.with(
                    if (player?.hasPowerRewards(plugin) == true
                    ) PMessage.GENERAL_VALUE_TRUE.toString() else
                        PMessage.GENERAL_VALUE_FALSE.toString()
                ),
            )
            if (meta.displayName != name)
            {
                meta.setDisplayName(PMessage.AQUA.getColor() + name)
            }
            skull.itemMeta = meta
            return skull
        }
    }
}
