package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.VotePartyType
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractEnumCarousel
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class VotePartyTypeCarousel(private val plugin: CV) : AbstractEnumCarousel(
    plugin, Material.SPLASH_POTION, VotePartyType, Setting.VOTE_PARTY_TYPE.path, PMessage.VOTE_PARTY_ITEM_NAME_TYPE
)
{
    override suspend fun newInstance(): ItemStack
    {
        return VotePartyTypeCarousel(plugin)
    }

    init
    {
        val type = VotePartyType.valueOf(plugin.config.getNumber(Setting.VOTE_PARTY_TYPE.path)).label()
        setLore(PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(type))
    }
}