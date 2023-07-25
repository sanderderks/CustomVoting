package me.sd_master92.customvoting.gui.buttons.switches

import me.sd_master92.core.inventory.StatusItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class AlwaysCrateRewardSwitch(private val plugin: CV, private val number: Int) : StatusItem(
    Material.DIAMOND,
    PMessage.CRATE_ITEM_NAME_ALWAYS_REWARD.toString(),
    plugin.data,
    Data.VOTE_CRATES.path + ".$number.always"
)
{
    private fun newInstance(): ItemStack
    {
        return AlwaysCrateRewardSwitch(plugin, number)
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        val path = Data.VOTE_CRATES.path + ".$number.always"
        plugin.data.set(path, !plugin.data.getBoolean(path))
        plugin.data.saveConfig()
        event.currentItem = newInstance()
    }
}