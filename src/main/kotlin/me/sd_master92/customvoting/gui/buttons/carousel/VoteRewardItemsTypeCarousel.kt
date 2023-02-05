package me.sd_master92.customvoting.gui.buttons.carousel

import me.sd_master92.core.appendWhenTrue
import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.*
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VoteRewardItemsTypeCarousel(private val plugin: CV, private val op: Boolean) : BaseItem(
    Material.REPEATER, PMessage.ITEM_REWARDS_ITEM_NAME_TYPE.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        val path = Setting.ITEM_REWARD_TYPE.path.appendWhenTrue(op, Data.OP_REWARDS)
        plugin.config.setNumber(path, ItemRewardType.next(plugin, op).value)
        event.currentItem = VoteRewardItemsTypeCarousel(plugin, op)
    }

    init
    {
        val value = plugin.config.getNumber(Setting.ITEM_REWARD_TYPE.path.appendWhenTrue(op, Data.OP_REWARDS))
        val type = ItemRewardType.valueOf(value).label
        setLore(PMessage.GENERAL_ITEM_LORE_STATUS_X.with(type))
    }
}