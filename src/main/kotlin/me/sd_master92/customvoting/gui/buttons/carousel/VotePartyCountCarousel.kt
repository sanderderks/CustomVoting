package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VotePartyCountCarousel(private val plugin: CV) : BaseItem(
    Material.ENDER_CHEST, PMessage.VOTE_PARTY_ITEM_NAME_COUNTDOWN.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        if (plugin.config.getNumber(Setting.VOTE_PARTY_COUNTDOWN.path) < 60)
        {
            plugin.config.addNumber(Setting.VOTE_PARTY_COUNTDOWN.path, 10)
        } else
        {
            plugin.config.setNumber(Setting.VOTE_PARTY_COUNTDOWN.path, 0)
        }
        event.currentItem = VotePartyCountCarousel(plugin)
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