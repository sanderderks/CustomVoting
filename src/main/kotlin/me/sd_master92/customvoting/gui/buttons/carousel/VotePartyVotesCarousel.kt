package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.gui.buttons.abstracts.AbstractNumberCarousel
import org.bukkit.Material

class VotePartyVotesCarousel(plugin: CV) : AbstractNumberCarousel(
    plugin,
    Material.ENCHANTED_BOOK,
    Setting.VOTES_REQUIRED_FOR_VOTE_PARTY,
    PMessage.VOTE_PARTY_ITEM_NAME_VOTES_UNTIL,
    IntRange(10, 100),
    10
)
{
    override fun newInstance(plugin: CV): VotePartyVotesCarousel
    {
        return VotePartyVotesCarousel(plugin)
    }

    init
    {
        val votesRequired = plugin.config.getNumber(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path)
        val votesUntil = votesRequired - plugin.data.getNumber(Data.CURRENT_VOTES.path)
        setLore(PMessage.VOTE_PARTY_ITEM_LORE_VOTES_UNTIL_XY.with("$votesRequired", "$votesUntil"))
    }
}