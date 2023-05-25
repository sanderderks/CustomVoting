package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.VMaterial
import me.sd_master92.customvoting.constants.enumerations.VoteSortType
import me.sd_master92.customvoting.constants.interfaces.Voter
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractEnumCarousel
import org.bukkit.inventory.ItemStack

class VotesSortTypeCarousel(private val plugin: CV) : AbstractEnumCarousel(
    plugin,
    VMaterial.SCULK_SENSOR.get(), VoteSortType, Setting.VOTES_SORT_TYPE.path,
    PMessage.VOTES_SORT_TYPE_ITEM_NAME
)
{
    override fun newInstance(): ItemStack
    {
        Voter.getTopVoters(plugin, true)
        return VotesSortTypeCarousel(plugin)
    }

    init
    {
        val type = VoteSortType.valueOf(plugin.config.getNumber(Setting.VOTES_SORT_TYPE.path)).label.toString()
        setLore(PMessage.GENERAL_ITEM_LORE_CURRENT_X.with(type))
    }
}