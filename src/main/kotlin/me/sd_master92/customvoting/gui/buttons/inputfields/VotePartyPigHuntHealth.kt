package me.sd_master92.customvoting.gui.buttons.inputfields

import me.sd_master92.core.input.PlayerNumberInput
import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.VMaterial
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VotePartyPigHuntHealth(
    private val plugin: CV,
    private val currentPage: GUI
) : BaseItem(VMaterial.PIG_HEAD.get(), PMessage.VOTE_PARTY_ITEM_NAME_PIG_HUNT_HEALTH.toString())
{
    val path = Setting.VOTE_PARTY_PIG_HUNT_HEALTH.path

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        currentPage.cancelCloseEvent = true
        player.closeInventory()
        player.sendMessage(PMessage.GENERAL_MESSAGE_NUMBER_ENTER.toString())
        player.sendMessage(PMessage.GENERAL_MESSAGE_CANCEL_BACK_X.with("cancel"))
        object : PlayerNumberInput(plugin, player)
        {
            override fun onNumberReceived(input: Int)
            {
                SoundType.SUCCESS.play(plugin, player)
                plugin.config[path] = input
                plugin.config.saveConfig()
                player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_SUCCESS_X.with(PMessage.HEALTH_UNIT.toString()))
                event.currentItem = VotePartyPigHuntHealth(plugin, currentPage)
                currentPage.open(player)
                cancel()
            }

            override fun onCancel()
            {
                SoundType.FAILURE.play(plugin, player)
                currentPage.open(player)
            }
        }
    }

    init
    {
        setLore(PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(PMessage.GREEN.getColor() + plugin.config.getDouble(path)))
    }
}