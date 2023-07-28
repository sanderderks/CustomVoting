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
import me.sd_master92.customvoting.gui.buttons.switches.AllowCrateClose
import me.sd_master92.customvoting.gui.buttons.switches.AlwaysCrateRewardSwitch
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class CrateSettingsPage(private val plugin: CV, backPage: GUI?, private val number: Int) : GUI(
    plugin,
    backPage,
    (plugin.data.getString(Data.VOTE_CRATES.path + ".$number.name")
        ?: PMessage.CRATE_NAME_DEFAULT_X.with("$number")),
    18
)
{
    override fun newInstance(): GUI
    {
        return CrateSettingsPage(plugin, backPage, number)
    }

    override fun onBack(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
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
        addItem(AlwaysCrateRewardSwitch(plugin, number))
        addItem(AllowCrateClose(plugin))
        setItem(16, CrateDeleteAction(plugin, this, number, name))
    }
}