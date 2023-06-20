package me.sd_master92.customvoting.gui.buttons.inputfields

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractRewardCommandsButton
import me.sd_master92.customvoting.listeners.PlayerCommandInput
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VotePartyCommandsInput(
    private val plugin: CV,
    private val currentPage: GUI
) : AbstractRewardCommandsButton(
    plugin,
    Data.VOTE_PARTY_COMMANDS.path,
    Material.COMMAND_BLOCK
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        currentPage.cancelCloseEvent = true
        player.closeInventory()
        object : PlayerCommandInput(plugin, player, path)
        {
            override fun onCommandReceived()
            {
                SoundType.SUCCESS.play(plugin, player)
                event.currentItem = VotePartyCommandsInput(plugin, currentPage)
                currentPage.open(player)
            }

            override fun onCancel()
            {
                SoundType.FAILURE.play(plugin, player)
                currentPage.open(player)
            }
        }
    }
}