package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.VoteFile
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class MergeDuplicatesAction(private val plugin: CV) : BaseItem(
    Material.HOPPER, PMessage.MERGE_DUPLICATES_ITEM_NAME.toString(),
    PMessage.MERGE_DUPLICATES_ITEM_LORE.with("" + VoteFile.getAll().size)
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        val deleted = VoteFile.mergeDuplicates()
        player.sendMessage(PMessage.MERGE_DUPLICATES_MESSAGE_DELETED_X.with("$deleted"))
        if (deleted > 0)
        {
            SoundType.SUCCESS
            event.currentItem = MergeDuplicatesAction(plugin)
        }
    }
}