package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import me.sd_master92.customvoting.constants.enumerations.VotePartyType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VotePartyTypeCarousel(private val plugin: CV) : BaseItem(
    Material.SPLASH_POTION, PMessage.VOTE_PARTY_ITEM_NAME_TYPE.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        plugin.config.setNumber(
            Setting.VOTE_PARTY_TYPE.path,
            VotePartyType.next(plugin).value
        )
        event.currentItem = VotePartyTypeCarousel(plugin)
    }

    init
    {
        val type = VotePartyType.valueOf(plugin.config.getNumber(Setting.VOTE_PARTY_TYPE.path)).label
        setLore(PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(type))
    }
}