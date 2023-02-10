package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.input.PlayerStringInput
import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import java.util.*

class CrateRenameAction(private val plugin: CV, private val currentPage: GUI, private val number: Int) :
    BaseItem(Material.OAK_SIGN, PMessage.CRATE_ITEM_NAME_RENAME.toString())
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        currentPage.cancelCloseEvent = true
        player.closeInventory()
        player.sendMessage(PMessage.CRATE_MESSAGE_NAME_ENTER.toString())
        player.sendMessage(PMessage.GENERAL_MESSAGE_CANCEL_BACK.toString())
        object : PlayerStringInput(plugin, player)
        {
            override fun onInputReceived(input: String)
            {
                SoundType.SUCCESS.play(plugin, player)
                plugin.data.set(Data.VOTE_CRATES.path + ".$number.name", input)
                plugin.data.saveConfig()
                player.sendMessage(PMessage.CRATE_MESSAGE_NAME_CHANGED_X.with(input))
                currentPage.newInstance().open(player)
                val uuid = plugin.data.getString(Data.VOTE_CRATES.path + ".$number.stand")
                if (uuid != null)
                {
                    val entity = Bukkit.getEntity(UUID.fromString(uuid))
                    if (entity is ArmorStand)
                    {
                        entity.customName = PMessage.CRATE_NAME_STAND_X.with(input)
                    }
                }
                cancel()
            }

            override fun onCancel()
            {
                SoundType.FAILURE.play(plugin, player)
                currentPage.newInstance().open(player)
            }
        }
    }
}