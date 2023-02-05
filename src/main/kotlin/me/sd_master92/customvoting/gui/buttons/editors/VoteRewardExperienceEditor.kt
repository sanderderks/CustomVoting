package me.sd_master92.customvoting.gui.buttons.editors

import me.sd_master92.core.appendWhenTrue
import me.sd_master92.core.inventory.BaseItem
import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.enumerations.Data
import me.sd_master92.customvoting.constants.enumerations.PMessage
import me.sd_master92.customvoting.constants.enumerations.Setting
import me.sd_master92.customvoting.constants.enumerations.SoundType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

class VoteRewardExperienceEditor(private val plugin: CV, private val op: Boolean) : BaseItem(
    Material.EXPERIENCE_BOTTLE, PMessage.XP_REWARD_ITEM_NAME.toString()
)
{
    override fun onClick(event: InventoryClickEvent, player: Player)
    {
        SoundType.CHANGE.play(plugin, player)
        val path = Setting.VOTE_REWARD_EXPERIENCE.path.appendWhenTrue(op, Data.OP_REWARDS)
        if (plugin.config.getNumber(path) < 10)
        {
            plugin.config.addNumber(path, 1)
        } else
        {
            plugin.config.setNumber(path, 0)
        }
        event.currentItem = VoteRewardExperienceEditor(plugin, op)
    }

    init
    {
        val number = plugin.config.getNumber(Setting.VOTE_REWARD_EXPERIENCE.path.appendWhenTrue(op, Data.OP_REWARDS))
        setLore(PMessage.GENERAL_ITEM_LORE_CURRENT_XY.with("$number", PMessage.XP_UNIT_LEVELS_MULTIPLE.toString()))
    }
}