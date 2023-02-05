package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class LuckyVoteChanceCarousel(private val plugin: CV) : BaseItem(
    Material.ENDER_EYE, PMessage.LUCKY_VOTE_ITEM_NAME_CHANCE.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        val chance: Int = plugin.config.getNumber(Setting.LUCKY_VOTE_CHANCE.path)
        if (chance < 10)
        {
            plugin.config.addNumber(Setting.LUCKY_VOTE_CHANCE.path, 1)
        } else if (chance < 100)
        {
            plugin.config.addNumber(Setting.LUCKY_VOTE_CHANCE.path, 5)
        } else
        {
            plugin.config.setNumber(Setting.LUCKY_VOTE_CHANCE.path, 1)
        }
        event.currentItem = LuckyVoteChanceCarousel(plugin)
    }

    init
    {
        val chance = plugin.config.getNumber(Setting.LUCKY_VOTE_CHANCE.path)
        setLore(PMessage.GENERAL_ITEM_LORE_CURRENT_X.with("$chance%"))
    }
}