package me.sd_master92.customvoting.gui.pages.menus

import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.capitalize
import me.sd_master92.customvoting.constants.enumerations.Message
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.subjects.VoteSite
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class VoteSiteMenu(private val plugin: CV) :
    GUI(plugin, null, Message.VOTE_COMMAND_TITLE.getMessage(plugin), 27, true, false)
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
        val voteSite = VoteSite.getBySlot(plugin, event.slot)
        if (voteSite != null)
        {
            TaskTimer.delay(plugin)
            {
                cancelCloseEvent = true
                if (voteSite.url != null)
                {
                    SoundType.SUCCESS.play(plugin, player)
                    player.sendMessage(
                        Message.VOTE_COMMAND_PREFIX.getMessage(
                            plugin,
                            mapOf(Pair("%SERVICE%", voteSite.url!!))
                        )
                    )
                } else
                {
                    SoundType.FAILURE.play(plugin, player)
                    player.sendMessage(PMessage.VOTE_SITES_MESSAGE_URL_DEFAULT.toString().capitalize())
                }
                player.closeInventory()
            }.run()
        }
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
        for ((i, item) in VoteSite.getItems(plugin))
        {
            setItem(i, item)
        }
    }
}