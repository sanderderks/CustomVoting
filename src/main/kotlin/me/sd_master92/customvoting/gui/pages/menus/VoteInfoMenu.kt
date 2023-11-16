package me.sd_master92.customvoting.gui.pages.menus

import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.inventory.GUIWithPagination
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.interfaces.Voter
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
    GUIWithPagination<VoteSite>(
        plugin,
        null,
        VoteSite.getAllActive(plugin),
        { VoteSite.getAllActive(plugin).indexOf(it) },
        { _, site, _ ->
            val siteItem = site.getGUIItem(false)
            val last = site.getLastByDate(voter)
            val timeDifference =
                if (last != null) last + (site.interval * 60 * 60 * 1000) - System.currentTimeMillis() else 0L
            siteItem.setLore(
                ";" + PMessage.GRAY + "Next vote: " + PMessage.RED + timeDifference.toTimeString()
            )
            siteItem
        },
        page,
        if (other) PMessage.VOTE_INFO_INVENTORY_NAME_OTHERS_X.with(voter.name) else PMessage.VOTE_INFO_INVENTORY_NAME.toString(),
        PMessage.GENERAL_ITEM_NAME_NEXT.toString(),
        PMessage.GENERAL_ITEM_NAME_PREVIOUS.toString(),
        differentStartIndex = 18
    )
{
    private var isClosed = false

    override fun newInstance(page: Int): GUI
    {
        return VoteInfoMenu(plugin, voter, other, page)
    }

    override fun newInstance(): GUI
    {
        return newInstance(page)
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        isClosed = true
        SoundType.CLOSE.play(plugin, player)
    }

    override fun onPaginate(player: Player, page: Int)
    {
        isClosed = true
        SoundType.CLICK.play(plugin, player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    override fun open(player: Player)
    {
        if (!isClosed)
        {
            super.open(player)
            TaskTimer.delay(plugin, 20L)
            {
                if (!isClosed)
                {
                    cancelCloseEvent = true
                    newInstance(page).open(player)
                }
            }.run()
        }
    }

    init
    {
        if (page == 0)
        {
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
                            Material.NETHER_STAR,
                            PMessage.MILESTONE_ITEM_NAME_X.with("#").replace("#", ""),
                            ";${PMessage.GRAY}Next milestone: " + PMessage.YELLOW + "in $required vote" + if (required != 1) "s" else ""
                        )
                    )
                }
            }
        }
    }
}