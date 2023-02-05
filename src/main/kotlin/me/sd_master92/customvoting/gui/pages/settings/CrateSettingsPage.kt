package me.sd_master92.customvoting.gui.pages.settings

import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.buttons.actions.CrateDeleteAction
import me.sd_master92.customvoting.gui.buttons.actions.CrateGetKeyAction
import me.sd_master92.customvoting.gui.buttons.actions.CrateRenameAction
import me.sd_master92.customvoting.gui.buttons.shortcuts.CrateRewardItemsShortcut
import me.sd_master92.customvoting.gui.pages.overviews.CrateOverviewPage
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class CrateSettingsPage(private val plugin: CV, number: Int) : GUI(
    plugin,
    (plugin.data.getString(Data.VOTE_CRATES.path + ".$number.name")
        ?: PMessage.CRATE_NAME_DEFAULT_X.with("$number")),
    9
)
{
    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        cancelCloseEvent = true
        CrateOverviewPage(plugin).open(player)
    }

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
    }

    override fun onClose(event: InventoryCloseEvent, player: Player)
    {
        SoundType.CLOSE.play(plugin, player)
    }

    override fun onSave(event: InventoryClickEvent, player: Player)
    {
    }

    init
    {
        addItem(CrateRenameAction(plugin, this, number))
        addItem(CrateGetKeyAction(plugin, number))
        for (chance in Data.CRATE_REWARD_CHANCES)
        {
            addItem(CrateRewardItemsShortcut(plugin, this, number, chance))
        }
        setItem(7, CrateDeleteAction(plugin, this, number, name))
    }
}