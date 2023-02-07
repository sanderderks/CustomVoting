package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.addToInventoryOrDrop
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.items.CrateKeyItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class CrateGetKeyAction(private val plugin: CV, private val number: Int) :
    BaseItem(
        Material.TRIPWIRE_HOOK,
        PMessage.CRATE_ITEM_NAME_KEY_GET.toString(),
        ";" + PMessage.CRATE_ITEM_LORE_PLACE,
        true
    )
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.SUCCESS.play(plugin, player)
        player.addToInventoryOrDrop(CrateKeyItem(plugin, number))
    }
}