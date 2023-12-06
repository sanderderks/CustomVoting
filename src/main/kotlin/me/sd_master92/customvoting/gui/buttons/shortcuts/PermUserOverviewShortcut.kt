package me.sd_master92.customvoting.gui.buttons.shortcuts

import com.github.shynixn.mccoroutine.bukkit.launch
import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.gui.pages.overviews.PermUserOverviewPage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class PermUserOverviewShortcut(
    private val plugin: CV,
    private val currentPage: GUI
) : BaseItem(
    Material.PLAYER_HEAD,
    PMessage.PERM_USER_OVERVIEW_ITEM_NAME.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        currentPage.cancelCloseEvent = true
        plugin.launch {
            open(player)
        }
    }

    private suspend fun open(player: Player)
    {
        val voters = Voter.getTopVoters(plugin)
        PermUserOverviewPage(plugin, currentPage, voters = voters).open(player)
    }
}