package me.sd_master92.customvoting.gui.pages.overviews

import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.inventory.GUIWithPagination
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.getOfflinePlayer
import me.sd_master92.customvoting.getSkull
import me.sd_master92.customvoting.stripColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class PermUserOverviewPage(private val plugin: CV, backPage: GUI?, private val page: Int = 0) :
    GUIWithPagination<Voter>(
        plugin,
        backPage,
        Voter.getTopVoters(plugin),
        { it.hashCode() },
        { _, item, _ -> getSkull(plugin, item) },
        page,
        PMessage.PERM_USER_OVERVIEW_INVENTORY_NAME.toString(),
        PMessage.GENERAL_ITEM_NAME_NEXT.toString(),
        PMessage.GENERAL_ITEM_NAME_PREVIOUS.toString()
    )
{
    override fun newInstance(page: Int): GUI
    {
        return PermUserOverviewPage(plugin, backPage, page)
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
                voter.setPower(!voter.power)
                event.currentItem = getSkull(plugin, voter)
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

    override fun onPaginate(player: Player, page: Int)
    {
        SoundType.CLICK.play(plugin, player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    companion object
    {
        private fun getSkull(plugin: CV, voter: Voter): ItemStack
        {
            val skull = voter.name.getOfflinePlayer(plugin).getSkull()
            val meta = skull.itemMeta!!
            val lore = mutableListOf(
                PMessage.GENERAL_ITEM_LORE_ENABLED_X.with(
                    if (voter.power)
                        PMessage.GENERAL_VALUE_YES.toString() else PMessage.GENERAL_VALUE_NO.toString()
                )
            )
            lore.addAll((";" + PMessage.PERM_USER_OVERVIEW_ITEM_LORE.toString()).split(";"))
            meta.lore = lore
            if (meta.displayName != voter.name)
            {
                meta.setDisplayName(PMessage.PLAYER_ITEM_NAME_SKULL_X.with(voter.name))
            }
            skull.itemMeta = meta
            return skull
        }
    }
}
