package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VotePartyVotesCarousel(private val plugin: CV) : BaseItem(
    Material.ENCHANTED_BOOK,
    PMessage.VOTE_PARTY_ITEM_NAME_VOTES_UNTIL.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        if (plugin.config.getNumber(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path) < 100)
        {
            plugin.config.addNumber(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path, 10)
        } else
        {
            plugin.config.setNumber(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path, 10)
        }
        event.currentItem = VotePartyVotesCarousel(plugin)
    }

    init
    {
        val votesRequired = plugin.config.getNumber(Setting.VOTES_REQUIRED_FOR_VOTE_PARTY.path)
        val votesUntil = votesRequired - plugin.data.getNumber(Data.CURRENT_VOTES.path)
        setLore(PMessage.VOTE_PARTY_ITEM_LORE_VOTES_UNTIL_XY.with("$votesRequired", "$votesUntil"))
    }
}