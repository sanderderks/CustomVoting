package me.sd_master92.customvoting.gui.buttons.shortcuts

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.gui.pages.settings.CrateSettingsPage
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class CrateSettingsShortcut(
    private val plugin: CV,
    private val currentPage: GUI,
    private val key: Int
) : BaseItem(
    Material.SHULKER_BOX,
    null,
    PMessage.CRATE_ITEM_LORE_KEY_X.with("$key"), true
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CLICK.play(plugin, player)
        currentPage.cancelCloseEvent = true
        CrateSettingsPage(plugin, currentPage, key).open(player)
    }

    init
    {
        val name = plugin.data.getString(Data.VOTE_CRATES.path + ".$key.name")
        if (name != null)
        {
            setName(PMessage.CRATE_NAME_X.with(name))
        } else
        {
            setName(PMessage.CRATE_NAME_DEFAULT_X.with("$key"))
        }
    }
}