package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.VMaterial
import me.sd_master92.customvoting.gui.pages.editors.VoteLinksEditor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VoteLinksEditorShortcut(private val plugin: CV, private val currentPage: GUI) : BaseItem(
    VMaterial.SOUL_TORCH.get(),
    PMessage.VOTE_LINKS_ITEM_NAME.toString(),
    PMessage.VOTE_LINKS_ITEM_LORE.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        currentPage.cancelCloseEvent = true
        VoteLinksEditor(plugin).open(player)
    }
}