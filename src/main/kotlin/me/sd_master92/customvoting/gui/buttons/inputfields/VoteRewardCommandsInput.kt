package me.sd_master92.customvoting.gui.buttons.inputfields

import me.sd_master92.core.appendWhenTrue
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractRewardCommandsButton
import me.sd_master92.customvoting.gui.pages.settings.RewardSettingsPage
import me.sd_master92.customvoting.listeners.PlayerCommandInput
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VoteRewardCommandsInput(private val plugin: CV, private val currentPage: GUI, private val power: Boolean) :
    AbstractRewardCommandsButton(
        plugin,
        Data.VOTE_COMMANDS.path.appendWhenTrue(power, Data.POWER_REWARDS),
        Material.COMMAND_BLOCK
    )
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        currentPage.cancelCloseEvent = true
        player.closeInventory()
        object :
            PlayerCommandInput(plugin, player, path)
        {
            override fun onCommandReceived()
            {
                SoundType.SUCCESS.play(plugin, player)
                RewardSettingsPage(plugin, currentPage, power).open(player)
            }

            override fun onCancel()
            {
                SoundType.FAILURE.play(plugin, player)
                RewardSettingsPage(plugin, currentPage, power).open(player)
            }
        }
    }
}