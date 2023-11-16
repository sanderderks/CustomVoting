package me.sd_master92.customvoting.gui.pages.menus

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.subjects.voteparty.VotePartyChest
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class VotePartyCrate(private val plugin: CV, votePartyChest: VotePartyChest) :
    GUI(plugin, null, PMessage.VOTE_PARTY_INVENTORY_NAME_CHEST_X.with(votePartyChest.key), { 54 }, false)
{
    override fun newInstance(): GUI
    {
        return this
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
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    init
    {
        for ((i, item) in votePartyChest.items.toTypedArray().withIndex())
        {
            setItem(i, item)
        }
    }
}