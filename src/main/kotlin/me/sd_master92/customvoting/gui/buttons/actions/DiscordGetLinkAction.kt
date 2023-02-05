package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class DiscordGetLinkAction(private val plugin: CV, private val gui: GUI) : BaseItem(
    Material.ENCHANTED_BOOK,
    PMessage.SUPPORT_ITEM_NAME_DISCORD.toString(),
    PMessage.SUPPORT_ITEM_LORE_DISCORD.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        gui.cancelCloseEvent = true
        player.closeInventory()
        player.sendMessage(PMessage.SUPPORT_MESSAGE_DISCORD.toString())
        player.sendMessage(PMessage.SUPPORT_MESSAGE_DISCORD_URL.toString())
    }
}