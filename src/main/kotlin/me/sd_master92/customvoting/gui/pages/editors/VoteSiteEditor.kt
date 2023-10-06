package me.sd_master92.customvoting.gui.pages.editors

import me.sd_master92.core.input.PlayerNumberInput
import me.sd_master92.core.input.PlayerStringInput
import me.sd_master92.core.inventory.GUI
import me.sd_master92.core.tasks.TaskTimer
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.sendTexts
import me.sd_master92.customvoting.subjects.VoteSite
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class VoteSiteEditor(private val plugin: CV, private val back: GUI) :
    GUI(
        plugin,
        null,
        PMessage.VOTE_SITES_INVENTORY_NAME_EDITOR.toString(),
        27,
        true,
        false
    )
{
    private var lastSlot = -1
    private var lastSlotIsEditor = false

    override fun newInstance(): GUI
    {
        return VoteSiteEditor(plugin, back)
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        if (event.click == ClickType.RIGHT)
        {
            val voteSite = VoteSite.getBySlot(plugin, event.slot)
            if (voteSite != null)
            {
                SoundType.CLICK.play(plugin, player)
                cancelCloseEvent = true
                player.closeInventory()
                enterTitle(player, voteSite)
            } else
            {
                SoundType.FAILURE.play(plugin, player)
                player.sendMessage(PMessage.GENERAL_ERROR.toString())
            }
        } else if (event.click == ClickType.MIDDLE)
        {
            SoundType.CLICK.play(plugin, player)
            cancelCloseEvent = true
            player.closeInventory()
            player.sendTexts(
                listOf(
                    PMessage.VOTE_SITES_MESSAGE_DEACTIVATE.toString(),
                    PMessage.GENERAL_MESSAGE_CONFIRM_X.with("confirm"),
                    PMessage.GENERAL_MESSAGE_CANCEL_BACK_X.with("cancel")
                )
            )
            object : PlayerStringInput(plugin, player)
            {
                override fun onInputReceived(input: String)
                {
                    if (input == "confirm")
                    {
                        val voteSite = VoteSite.getBySlot(plugin, event.slot)
                        voteSite?.active = false
                        SoundType.SUCCESS.play(plugin, player)
                        newInstance().open(player)
                        cancel()
                    }
                }

                override fun onCancel()
                {
                    SoundType.SUCCESS.play(plugin, player)
                    open(player)
                }
            }
        } else
        {
            when (event.action)
            {
                InventoryAction.PICKUP_ALL       ->
                {
                    lastSlot = event.slot
                    lastSlotIsEditor = true
                    event.isCancelled = false
                }

                InventoryAction.PLACE_ALL        ->
                {
                    if (lastSlotIsEditor)
                    {
                        val voteSite = VoteSite.getBySlot(plugin, lastSlot)
                        voteSite?.slot = event.slot
                        SoundType.CHANGE.play(plugin, player)
                        lastSlotIsEditor = false
                        event.isCancelled = false
                    } else
                    {
                        SoundType.FAILURE.play(plugin, player)
                        player.sendTexts(PMessage.VOTE_SITES_MESSAGE_SETUP.toString().split(";"))
                        event.isCancelled = true
                    }
                }

                InventoryAction.SWAP_WITH_CURSOR ->
                {
                    if (clickableItems.containsKey(event.slot) && !lastSlotIsEditor)
                    {
                        val voteSite = VoteSite.getBySlot(plugin, event.slot)
                        if (voteSite != null && event.cursor != null)
                        {
                            val originalItem = voteSite.item
                            voteSite.item = event.cursor!!
                            player.setItemOnCursor(ItemStack(originalItem.type))
                            SoundType.CHANGE.play(plugin, player)
                            event.isCancelled = false
                            cancelCloseEvent = true
                            newInstance().open(player)
                        }
                    }
                }

                else                             ->
                {
                }
            }
        }
    }

    @EventHandler
    override fun onInventoryClick(event: InventoryClickEvent)
    {
        if (isThisInventory(event))
        {
            event.isCancelled = true
            onClick(event, event.whoClicked as Player)
        } else if (event.view.title == name && (lastSlotIsEditor || event.isShiftClick))
        {
            event.isCancelled = true
        }
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        player.setItemOnCursor(null)
        TaskTimer.delay(plugin)
        {
            SoundType.CLICK.play(plugin, player)
            back.newInstance().open(player)
        }.run()
    }

    private fun enterTitle(player: Player, voteSite: VoteSite)
    {
        player.sendMessage(
            arrayOf(
                PMessage.VOTE_SITES_MESSAGE_TITLE_ENTER.toString(),
                PMessage.GENERAL_MESSAGE_CANCEL_CONTINUE_X.with("skip")
            )
        )
        object : PlayerStringInput(plugin, player)
        {
            override fun onInputReceived(input: String)
            {
                voteSite.title = ChatColor.translateAlternateColorCodes('&', input)

                SoundType.SUCCESS.play(plugin, player)
                enterDescription(player, voteSite)
                cancel()
            }

            override fun onCancel()
            {
                SoundType.SUCCESS.play(plugin, player)
                enterDescription(player, voteSite)
            }
        }
    }

    private fun enterDescription(player: Player, voteSite: VoteSite, add: Boolean = false)
    {
        if (!add)
        {
            player.sendMessage(
                arrayOf(
                    PMessage.VOTE_SITES_MESSAGE_DESCRIPTION_ENTER.toString(),
                    PMessage.GENERAL_MESSAGE_LIST_CLEAR_X.with("clear"),
                    PMessage.GENERAL_MESSAGE_CANCEL_CONTINUE_X.with("skip"),
                )
            )
        }
        object : PlayerStringInput(plugin, player)
        {
            override fun onInputReceived(input: String)
            {
                val message = ChatColor.translateAlternateColorCodes('&', input)

                if (message == "clear")
                {
                    voteSite.description = listOf()
                } else
                {
                    val description = voteSite.description.toMutableList()
                    description.add(message)
                    voteSite.description = description
                }

                SoundType.SUCCESS.play(plugin, player)
                player.sendMessage(
                    arrayOf(
                        PMessage.VOTE_SITES_MESSAGE_DESCRIPTION_ENTER_MORE.toString(),
                        PMessage.GENERAL_MESSAGE_CANCEL_CONTINUE_X.with("skip")
                    )
                )
                enterDescription(player, voteSite, true)
                cancel()
            }

            override fun onCancel()
            {
                SoundType.SUCCESS.play(plugin, player)
                enterLink(player, voteSite)
            }
        }
    }

    private fun enterLink(player: Player, voteSite: VoteSite)
    {
        player.sendMessage(
            arrayOf(
                PMessage.VOTE_SITES_MESSAGE_URL.toString(),
                PMessage.GENERAL_MESSAGE_CANCEL_CONTINUE_X.with("skip")
            )
        )
        object : PlayerStringInput(plugin, player)
        {
            override fun onInputReceived(input: String)
            {
                voteSite.url = ChatColor.translateAlternateColorCodes('&', input)

                SoundType.SUCCESS.play(plugin, player)
                enterInterval(player, voteSite)
                cancel()
            }

            override fun onCancel()
            {
                SoundType.SUCCESS.play(plugin, player)
                enterInterval(player, voteSite)
            }
        }
    }

    private fun enterInterval(player: Player, voteSite: VoteSite)
    {
        player.sendMessage(
            arrayOf(
                PMessage.VOTE_SITES_MESSAGE_INTERVAL.toString(),
                PMessage.GENERAL_MESSAGE_CANCEL_CONTINUE_X.with("skip")
            )
        )
        object : PlayerNumberInput(plugin, player, 1, 1000)
        {
            override fun onNumberReceived(input: Int)
            {
                voteSite.interval = input

                SoundType.SUCCESS.play(plugin, player)
                VoteSiteEditor(plugin, back).open(player)
                cancel()
            }

            override fun onCancel()
            {
                SoundType.SUCCESS.play(plugin, player)
                VoteSiteEditor(plugin, back).open(player)
            }
        }
    }

    init
    {
        for ((i, item) in VoteSite.getItems(plugin, true))
        {
            setItem(i, item)
        }
    }
}