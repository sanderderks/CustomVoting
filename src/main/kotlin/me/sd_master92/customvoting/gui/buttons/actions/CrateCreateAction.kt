package me.sd_master92.customvoting.gui.buttons.actions

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.overviews.CrateOverviewPage
import me.sd_master92.customvoting.stripColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class CrateCreateAction(private val plugin: CV, private val backPage: GUI) :
    BaseItem(Material.CRAFTING_TABLE, PMessage.CRATE_ITEM_NAME_ADD.toString())
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        val numbers = plugin.data.getConfigurationSection(Data.VOTE_CRATES.path)?.getKeys(false)?.map {
            try
            {
                it.toInt()
            } catch (e: Exception)
            {
                0
            }
        } ?: listOf()
        var i = 1
        while (numbers.contains(i))
        {
            i++
        }
        SoundType.SUCCESS.play(plugin, player)
        val name = PMessage.CRATE_NAME_DEFAULT_X.with("$i").stripColor()
        plugin.data.set(Data.VOTE_CRATES.path + ".$i.name", name)
        plugin.data.saveConfig()
        player.sendMessage(PMessage.GENERAL_MESSAGE_CREATE_SUCCESS_X.with(name))
        backPage.cancelCloseEvent = true
        CrateOverviewPage(plugin, backPage).open(player)
    }
}