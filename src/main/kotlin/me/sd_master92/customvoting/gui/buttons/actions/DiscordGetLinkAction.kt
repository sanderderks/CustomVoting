package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class DiscordGetLinkAction(private val plugin: CV, private val currentPage: GUI) : BaseItem(
    Material.ENCHANTED_BOOK,
    PMessage.DISCORD_ITEM_NAME.toString(),
    PMessage.DISCORD_ITEM_LORE.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        currentPage.cancelCloseEvent = true
        player.closeInventory()
        player.sendMessage(PMessage.DISCORD_MESSAGE.toString())
        player.sendMessage(PMessage.DISCORD_MESSAGE_URL.toString())
    }
}