package me.sd_master92.customvoting.gui.items

import me.sd_master92.customvoting.CV
import me.sd_master92.customvoting.constants.Data
import me.sd_master92.customvoting.constants.Settings
import org.bukkit.ChatColor
import org.bukkit.Material

class ExperienceRewardItem private constructor(plugin: CV, path: String) : BaseItem(
    Material.EXPERIENCE_BOTTLE, ChatColor.LIGHT_PURPLE.toString() + "XP Reward",
    ChatColor.GRAY.toString() + "Currently: " + ChatColor.AQUA + plugin.config.getNumber(path) + ChatColor.GRAY + " levels"
)
{
    companion object
    {
        fun getInstance(plugin: CV, op: Boolean): ExperienceRewardItem
        {
            var path = Settings.VOTE_REWARD_EXPERIENCE
            if (op)
            {
                path += Data.OP_REWARDS
            }
            return ExperienceRewardItem(plugin, path)
        }
    }
}