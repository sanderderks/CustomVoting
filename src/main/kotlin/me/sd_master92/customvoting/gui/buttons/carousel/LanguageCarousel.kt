package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.core.inventory.GUI
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Language
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.VMaterial
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class LanguageCarousel(private val plugin: CV, private val currentPage: GUI) : BaseItem(
    VMaterial.CRIMSON_SIGN.get(),
    PMessage.LANGUAGE_ITEM_NAME.toString()
)
{
    private val language = Language.get(plugin)

    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        language.switch(plugin)
        currentPage.cancelCloseEvent = true
        val newPage = currentPage.newInstance()
        newPage.backPage = currentPage.backPage?.newInstance()
        newPage.open(player)
    }

    init
    {
        setLore(PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(PMessage.LANGUAGE_ITEM_LORE_X.with(language.label)))
    }
}