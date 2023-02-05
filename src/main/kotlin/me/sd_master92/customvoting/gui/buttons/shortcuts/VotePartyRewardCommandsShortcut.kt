package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.abstracts.CommandRewardsAbstractButton
import me.sd_master92.customvoting.gui.pages.settings.RewardSettingsPage
import me.sd_master92.customvoting.listeners.PlayerCommandInput
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VotePartyRewardCommandsShortcut(private val plugin: CV, private val gui: RewardSettingsPage) :
    CommandRewardsAbstractButton(
        plugin,
        Data.VOTE_PARTY_COMMANDS.path,
        Material.TNT,
        PMessage.COMMAND_REWARDS_ITEM_NAME_VOTE_PARTY.toString()
    )
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        gui.cancelCloseEvent = true
        player.closeInventory()
        object : PlayerCommandInput(plugin, player, Data.VOTE_PARTY_COMMANDS.path)
        {
            override fun onCommandReceived()
            {
                SoundType.SUCCESS.play(plugin, player)
                gui.open(player)
            }

            override fun onCancel()
            {
                SoundType.FAILURE.play(plugin, player)
                gui.open(player)
            }
        }
    }
}