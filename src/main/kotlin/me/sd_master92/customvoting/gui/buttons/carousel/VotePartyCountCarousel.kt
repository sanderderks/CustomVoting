package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractNumberCarousel
import org.bukkit.Material

class VotePartyCountCarousel(plugin: CV) : AbstractNumberCarousel(
    plugin,
    Material.ENDER_CHEST,
    Setting.VOTE_PARTY_COUNTDOWN,
    PMessage.VOTE_PARTY_ITEM_NAME_COUNTDOWN,
    IntRange(0, 60),
    10
)
{
    override fun newInstance(plugin: CV): VotePartyCountCarousel
    {
        return VotePartyCountCarousel(plugin)
    }

    init
    {
        val countdown = plugin.config.getNumber(Setting.VOTE_PARTY_COUNTDOWN.path)
        setLore(
            PMessage.GENERAL_ITEM_LORE_CURRENT_XY.with(
                "$countdown",
                PMessage.TIME_UNIT_SECONDS_MULTIPLE.toString()
            )
        )
    }
}