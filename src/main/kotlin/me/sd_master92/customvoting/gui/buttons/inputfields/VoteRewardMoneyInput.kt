package me.sd_master92.customvoting.gui.buttons.inputfields

import me.sd_master92.core.appendWhenTrue
import me.sd_master92.core.input.PlayerNumberInput
import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.settings.RewardSettingsPage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VoteRewardMoneyInput(private val plugin: CV, private val currentPage: GUI, private val power: Boolean) : BaseItem(
    Material.GOLD_INGOT, PMessage.MONEY_REWARD_ITEM_NAME.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        if (CV.ECONOMY != null)
        {
            SoundType.CHANGE.play(plugin, player)
            currentPage.cancelCloseEvent = true
            player.closeInventory()
            player.sendMessage(PMessage.GENERAL_MESSAGE_NUMBER_ENTER.toString())
            player.sendMessage(PMessage.GENERAL_MESSAGE_CANCEL_BACK.toString())
            object : PlayerNumberInput(plugin, player)
            {
                override fun onNumberReceived(input: Int)
                {
                    SoundType.SUCCESS.play(plugin, player)
                    val path = Setting.VOTE_REWARD_MONEY.path.appendWhenTrue(power, Setting.POWER_REWARDS)
                    plugin.config[path] = input
                    plugin.config.saveConfig()
                    player.sendMessage(PMessage.GENERAL_MESSAGE_UPDATE_SUCCESS_X.with(PMessage.MONEY_REWARD_UNIT.toString()))
                    RewardSettingsPage(plugin, currentPage, power).open(player)
                    cancel()
                }

                override fun onCancel()
                {
                    SoundType.FAILURE.play(plugin, player)
                    RewardSettingsPage(plugin, currentPage, power).open(player)
                }
            }
        } else
        {
            SoundType.FAILURE.play(plugin, player)
        }
    }

    init
    {
        val path = plugin.config.getDouble(Setting.VOTE_REWARD_MONEY.path.appendWhenTrue(power, Setting.POWER_REWARDS))
        if (CV.ECONOMY != null)
        {
            setLore(PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(PMessage.GREEN.toString() + CV.ECONOMY!!.format(path)))
        } else
        {
            setLore(PMessage.GENERAL_VALUE_DISABLED.toString())
        }
    }
}