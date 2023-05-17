package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import org.bukkit.Material

class CrateKeyItem(plugin: CV, number: Int) :
    SimpleItem(
        Material.TRIPWIRE_HOOK,
        null,
        PMessage.CRATE_ITEM_LORE_KEY_X.with("$number"),
        true
    )
{
    init
    {
        val name = plugin.data.getString(Data.VOTE_CRATES.path + ".$number.name") ?: ""
        setName(PMessage.CRATE_ITEM_NAME_KEY_X.with(name))
    }
}