package me.sd_master92.customvoting.gui.pages.menus

import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.gui.buttons.actions.PaginationNextAction
import me.sd_master92.customvoting.gui.buttons.actions.PaginationPreviousAction
import me.sd_master92.customvoting.gui.items.SimpleItem
import me.sd_master92.customvoting.gui.pages.overviews.PlayerInfoOverviewPage
import me.sd_master92.customvoting.subjects.VoteSite
import me.sd_master92.customvoting.toTimeString
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class VoteInfoMenu(
    private val plugin: CV,
    private val voter: Voter,
    private val other: Boolean,
    private val page: Int = 0
) :
    GUI(
        plugin,
        null,
        if (other) PMessage.VOTE_INFO_INVENTORY_NAME_OTHERS_X.with(voter.name) else PMessage.VOTE_INFO_INVENTORY_NAME.toString(),
        calculateInventorySize(plugin),
        true,
        false
    )
{
    private val timers = mutableListOf<TaskTimer>()

    override fun newInstance(): GUI
    {
        return VoteInfoMenu(plugin, voter, other, page)
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
        timers.forEach { it.cancel() }
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    companion object
    {
        private fun calculateInventorySize(plugin: CV): Int
        {
            val voteSites = VoteSite.getAllActive(plugin).size + 20
            val size = if (voteSites % 9 == 0) voteSites else voteSites + (9 - (voteSites % 9))
            return size.coerceAtMost(54)
        }
    }

    init
    {
        setItem(
            nonClickableSizeWithNull - 1,
            object : PaginationNextAction(plugin, this, page)
            {
                override fun onNext(player: Player, newPage: Int)
                {
                    VoteInfoMenu(plugin, voter, other, newPage).open(player)
                }
            })
        setItem(nonClickableSizeWithNull - 1, object : PaginationPreviousAction(plugin, this, page)
        {
            override fun onPrevious(player: Player, newPage: Int)
            {
                VoteInfoMenu(plugin, voter, other, newPage).open(player)
            }
        })
        setItem(4, PlayerInfoOverviewPage.getSkull(plugin, voter))
        val milestonePath = plugin.data.getConfigurationSection(Data.MILESTONES.path)
        if (milestonePath != null)
        {
            val nextMilestone = milestonePath.getKeys(false).map { it.toInt() }
                .filter { it > voter.votes }
                .minOrNull()

            if (nextMilestone != null)
            {
                val required = nextMilestone - voter.votes
                setItem(
                    9,
                    SimpleItem(
                        Material.TORCH,
                        PMessage.MILESTONE_ITEM_NAME_X.with("#").replace("#", ""),
                        ";${PMessage.GRAY}Next milestone: " + PMessage.YELLOW + "in $required vote" + if (required != 1) "s" else ""
                    )
                )
            }
        }

        val start = page * nonClickableSizeWithNull
        for ((i, site) in VoteSite.getAllActive(plugin).drop(start).withIndex())
        {
            val index = i + 18
            timers.add(
                TaskTimer.repeat(plugin, 20L)
                {
                    if (index < size - 2)
                    {
                        setItem(index, object : SimpleItem(site.getGUIItem(false))
                        {
                            init
                            {
                                val last = site.getLastByDate(voter)
                                val timeDifference =
                                    if (last != null) last + (site.interval * 60 * 60 * 1000) - System.currentTimeMillis() else 0L
                                setLore(
                                    ";" + PMessage.GRAY + "Next vote: " + PMessage.RED + timeDifference.toTimeString()
                                )
                            }
                        })
                    }
                }.run()
            )
        }
    }
}