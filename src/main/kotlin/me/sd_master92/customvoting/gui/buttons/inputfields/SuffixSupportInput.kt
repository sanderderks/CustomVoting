package me.sd_master92.customvoting.gui.buttons.inputfields

import me.sd_master92.core.input.PlayerStringInput
import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class SuffixSupportInput(
    private val plugin: CV,
    private val currentPage: GUI
) :
    BaseItem(
        Material.BEDROCK, PMessage.SUFFIX_SUPPORT_ITEM_NAME.toString()
    )
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        currentPage.cancelCloseEvent = true
        player.closeInventory()
        player.sendMessage(PMessage.SUFFIX_SUPPORT_MESSAGE_INPUT.toString())
        player.sendMessage(PMessage.GENERAL_MESSAGE_CANCEL_BACK_X.with("cancel"))
        object : PlayerStringInput(plugin, player)
        {
            override fun onInputReceived(input: String)
            {
                SoundType.SUCCESS.play(plugin, player)
                plugin.config.set(Setting.SUFFIX_SUPPORT.path, if (input == "off") null else input)
                plugin.config.saveConfig()
                player.sendMessage(PMessage.SUFFIX_SUPPORT_MESSAGE_INPUT_CHANGED.with(input))
                currentPage.backPage = currentPage.backPage?.newInstance()
                currentPage.newInstance().open(player)
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
        setLore(
            PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(
                plugin.config.getString(Setting.SUFFIX_SUPPORT.path) ?: PMessage.GENERAL_VALUE_OFF.toString()
            )
        )
    }
}